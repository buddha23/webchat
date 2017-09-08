package com.lld360.cnc.core.utils;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.aspose.cells.*;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lld360.cnc.core.ServerException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * Author: dhc
 * Date: 2016-09-01 17:22
 */
public class DocUtils {
    /**
     * 将Office文档转换为PDF文件
     *
     * @param converter 文档转换器
     * @param docFile   Office文件（doc,docx,xls,xlsx,ppt,pptx,txt等)
     * @param pdfFile   需要生成的PDF文件
     * @return 生成后的PDF文件
     */
    public static File convertOffice2Pdf(DocumentConverter converter, File docFile, String pdfFile) {
        if (docFile.exists()) {
            File pf = new File(pdfFile);
            if ((pf.getParentFile().exists() && pf.getParentFile().isDirectory()) || pf.getParentFile().mkdirs()) {
                DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
                DocumentFormat doc = formatReg.getFormatByFileExtension("odt");
                DocumentFormat pdf = formatReg.getFormatByFileExtension("pdf");
                converter.convert(docFile, doc, pf, pdf);
                return pf;
            }
        }
        return null;
    }

    /**
     * 使用POI转换PPT文件为图片
     *
     * @param imageNames 空图片空列表，用于输出
     * @param pptFile    PPT文件
     * @param imagePath  图片保存目录
     * @param maxPages   最多生成页面
     * @return 文档总页数
     */
    public static int convertPPT2ImageByPoi(List<String> imageNames, File pptFile, String imagePath, int maxPages, String suffix) throws Exception {
        int totalPages;
        //图像文件的格式字符串为“jpg”、“jpeg”、"bmp"、"png"、 "gif"、"tiff"等
        String imageFormatNameString = "jpg";
        ConvertPPTFileToImage oneConvertPPTFileToImage = new ConvertPPTFileToImage();
        oneConvertPPTFileToImage.setMaxPages(maxPages);
        if (suffix.equalsIgnoreCase("ppt")) {
            totalPages = oneConvertPPTFileToImage.convertPPTtoImage(pptFile, imagePath, imageFormatNameString, imageNames);
        } else { /* 如果为PPTX格式的文件*/
            totalPages = oneConvertPPTFileToImage.convertPPTXtoImage(pptFile, imagePath, imageFormatNameString, imageNames);
        }
        return totalPages;

    }

    // Aspose的使用授权对象
    private static com.aspose.pdf.License pdfLicense;
    private static com.aspose.words.License wordsLicense;
    private static com.aspose.cells.License cellsLicense;
    private static com.aspose.slides.License slidesLicense;

    // 检查或启动Aspose.Pdf的使用授权对象
    private static void checkOrInitAsposePdfLicense() {
        if (pdfLicense == null) {
            pdfLicense = new com.aspose.pdf.License();
            try {
                pdfLicense.setLicense(DocUtils.class.getResourceAsStream("/aspose.license.xml"));
            } catch (Exception e) {
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Aspose.Pdf license set fail:" + e.getMessage());
            }
        }
    }

    // 检查或启动Aspose.Words的使用授权对象
    private static void checkOrInitAsposeWordsLicense() {
        if (wordsLicense == null) {
            wordsLicense = new com.aspose.words.License();
            try {
                wordsLicense.setLicense(DocUtils.class.getResourceAsStream("/aspose.license.xml"));
            } catch (Exception e) {
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Aspose.Words license set fail:" + e.getMessage());
            }
        }
    }

    // 检查或启动Aspose.Cells的使用授权对象
    private static void checkOrInitAsposeCellsLicense() {
        if (cellsLicense == null) {
            cellsLicense = new com.aspose.cells.License();
            try {
                cellsLicense.setLicense(DocUtils.class.getResourceAsStream("/aspose.license.xml"));
            } catch (Exception e) {
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Aspose.Cells license set fail:" + e.getMessage());
            }
        }
    }

    // 检查或启动Aspose.Slides的使用授权对象
    private static void checkOrInitAsposeSlidesLicense() {
        if (slidesLicense == null) {
            slidesLicense = new com.aspose.slides.License();
            try {
                slidesLicense.setLicense(DocUtils.class.getResourceAsStream("/aspose.license.xml"));
            } catch (Exception e) {
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Aspose.Slides license set fail:" + e.getMessage());
            }
        }
    }

    /**
     * 使用Aspose.Words转换Word文件为图片
     *
     * @param images    空图片空列表，用于输出
     * @param wordsFile Words文件
     * @param imagePath 图片保存目录
     * @param pages     最多生成页面
     * @return 文档总页数
     */

    public static int convertWords2ImageByAsposeWord(List<String> images, File wordsFile, String imagePath, int pages) throws Exception {
        checkOrInitAsposeWordsLicense();

        int totalPages = 1;
        if (wordsFile.exists()) {
            com.aspose.words.Document doc = new com.aspose.words.Document(wordsFile.getAbsolutePath());
            ImageSaveOptions options = new ImageSaveOptions(SaveFormat.JPEG);
            options.setJpegQuality(100);
            options.setResolution(100);
            options.setUseHighQualityRendering(true);

            totalPages = doc.getPageCount();

            File imgDir = new File(imagePath);
            if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                for (int i = 0; i < Math.min(totalPages, pages); i++) {
                    options.setPageIndex(i);
                    String imgFile = String.format("%s/%02d_%s.jpg", imagePath, i + 1, RandomStringUtils.randomAlphanumeric(6));
                    doc.save(imgFile, options);
                    images.add(FilenameUtils.getName(imgFile));
                }
            }
        }
        return totalPages;
    }

    /**
     * 使用Aspose.Cells转换Excel文件为图片
     *
     * @param images    空图片空列表，用于输出
     * @param excelFile Excel文件
     * @param imagePath 图片保存目录
     * @param pages     最多生成页面
     * @return 文档总页数
     */

    public static int convertExcel2ImageByAsposeCells(List<String> images, File excelFile, String imagePath, int pages) throws Exception {
        checkOrInitAsposeCellsLicense();

        int totalPages = 0;
        if (excelFile.exists()) {
            Workbook workbook = new Workbook(excelFile.getAbsolutePath());
            ImageOrPrintOptions options = new ImageOrPrintOptions();
            options.setImageFormat(ImageFormat.getJpeg());

            File imgDir = new File(imagePath);
            if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                    Worksheet sheet = workbook.getWorksheets().get(i);
                    SheetRender sr = new SheetRender(sheet, options);
                    for (int j = 0; j < sr.getPageCount(); j++) {
                        if (totalPages < pages) {
                            String imgFile = String.format("%s/s%d_%d_%s.jpg", imagePath, i + 1, j + 1, RandomStringUtils.randomAlphanumeric(6));
                            sr.toImage(j, imgFile);
                            images.add(FilenameUtils.getName(imgFile));
                        }
                        totalPages++;
                    }
                }
            }
        }
        return totalPages;
    }

    /**
     * 使用Aspose.Slides转换PPT文件为图片
     *
     * @param images    空图片空列表，用于输出
     * @param pptFile   PPT文件
     * @param imagePath 图片保存目录
     * @param pages     最多生成页面
     * @return 文档总页数
     */

    public static int convertPPT2ImageByAsposeSlides(List<String> images, File pptFile, String imagePath, int pages) throws Exception {
        checkOrInitAsposeSlidesLicense();

        int totalPages = 0;
        if (pptFile.exists()) {
            Presentation doc = new Presentation(pptFile.getAbsolutePath());
            totalPages = doc.getSlides().size();

            Dimension2D size = doc.getSlideSize().getSize();
            int w = (int) size.getWidth();
            int h = (int) size.getHeight();

            File imgDir = new File(imagePath);
            BufferedImage image;
            if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                for (int i = 0; i < Math.min(totalPages, pages); i++) {
                    ISlide slide = doc.getSlides().get_Item(i);

                    image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = image.createGraphics();
                    slide.renderToGraphics(false, g, 0.75f);
                    g.dispose();
                    String imgFile = String.format("%s/%02d_%s.jpg", imagePath, i, RandomStringUtils.randomAlphanumeric(6));
                    ImageIO.write(image, "jpg", new File(imgFile));
                    image.flush();
                    images.add(FilenameUtils.getName(imgFile));
                }
            }
        }
        return totalPages;
    }

    /**
     * 使用Aspose.Pdf转换PDF文件为图片
     *
     * @param images    图片空列表，用于填充
     * @param pdfFile   PDF文件
     * @param imagePath 图片文件目录
     * @param pages     最多生成页
     * @return 文档总页数
     */
    public static int convertPdf2ImagesByAsposePdf(List<String> images, File pdfFile, String imagePath, int pages) {
        checkOrInitAsposePdfLicense();

        int totalPages = 1;
        if (pdfFile.exists()) {
            float scale = 1.5f; // 放大倍数
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(pdfFile.getAbsolutePath());

            File imgDir = new File(imagePath);
            if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                totalPages = doc.getPages().size();
                JpegDevice jpegDevice;
                for (int i = 1; i <= Math.min(totalPages, pages); i++) {
                    String imgFile = String.format("%s/%02d_%s.jpg", imagePath, i, RandomStringUtils.randomAlphanumeric(6));
                    com.aspose.pdf.Page page = doc.getPages().get_Item(i);
                    int width = (int) (page.getPageInfo().getWidth() * scale);
                    int height = (int) (page.getPageInfo().getHeight() * scale);
                    jpegDevice = new JpegDevice(width, height, new Resolution(100), 90);
                    jpegDevice.process(page, imgFile);
                    images.add(FilenameUtils.getName(imgFile));
                }
            }
            doc.close();
        }
        return totalPages;
    }

    /**
     * 使用PDFBox转换PDF文件为图片
     *
     * @param images    图片空列表，用于填充
     * @param pdfFile   PDF文件
     * @param imagePath 图片文件目录
     * @param pages     最多生成页
     * @return 文档总页数
     */
    public static int convertPdf2ImagesByPDFBox(List<String> images, File pdfFile, String imagePath, int pages) throws IOException {
        int totalPages = 1;
        if (pdfFile.exists()) {
            float scale = 2f; // 放大倍数

            File imgDir = new File(imagePath);
            if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                PDDocument document = PDDocument.load(pdfFile);
                try {
                    totalPages = document.getNumberOfPages();
                    PDFRenderer renderer = new PDFRenderer(document);
                    for (int i = 0; i < Math.min(totalPages, pages); i++) {
                        BufferedImage image = renderer.renderImage(i, scale, ImageType.RGB);
                        String imgFile = String.format("%s/%02d_%s.jpg", imagePath, i + 1, RandomStringUtils.randomAlphanumeric(6));
                        File img = new File(imgFile);
                        ImageIO.write(image, "jpg", img);
                        images.add(img.getName());
                        image.flush();
                    }
                } finally {
                    if (document != null) {
                        document.close();
                    }
                }
            }
        }
        return totalPages;
    }

    /**
     * 使用ICEPdf转换PDF文件为图片
     *
     * @param images    图片空列表，用于填充
     * @param pdfFile   PDF文件
     * @param imagePath 图片文件目录
     * @param pages     最多生成页数
     * @return 文档总页数
     */
    public static int convertPdf2ImagesByICEPdf(List<String> images, File pdfFile, String imagePath, int pages) throws IOException {
        int totalPages = 1;
        if (pdfFile.exists()) {
            float scale = 1.5f; // 放大倍数
            Document document = new Document();
            File imgDir = new File(imagePath);
            if ((imgDir.exists() && imgDir.isDirectory()) || imgDir.mkdirs()) {
                document.setFile(pdfFile.getAbsolutePath());
                totalPages = document.getNumberOfPages();
                BufferedImage image;
                for (int i = 0; i < Math.min(totalPages, pages); i++) {
                    image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0f, scale);
                    String imgFile = String.format("%s/%02d_%s.jpg", imagePath, i + 1, RandomStringUtils.randomAlphanumeric(6));
                    File img = new File(imgFile);
                    ImageIO.write(image, "jpg", img);
                    images.add(img.getName());
                    image.flush();
                }
                document.dispose();
            }
        }
        return totalPages;
    }

    /**
     * 将Office文档转换为图片
     *
     * @param images     图片空列表，用于填充
     * @param converter  文档转换器
     * @param docFile    Office文件（doc,docx,xls,xlsx,ppt,pptx,txt等)
     * @param imagesPath 图片文件目录
     * @param pages      最多生成页
     * @param keepPdf    是否保留中间PDF文件
     * @return 总页数
     */
    public static int convertOffice2Images(List<String> images, DocumentConverter converter, File docFile, String imagesPath, int pages, boolean keepPdf) {
        if (docFile.exists()) {
            String pdfFile = docFile.getParentFile().getAbsolutePath() + "/" + docFile.getName() + ".pdf";
            File pdf = convertOffice2Pdf(converter, docFile, pdfFile);
            int totalPages = convertPdf2ImagesByAsposePdf(images, pdf, imagesPath, pages);
            if (!keepPdf) {
                FileUtils.deleteQuietly(pdf);
            }
            return totalPages;
        }
        return 1;
    }

    /**
     * 将Txt文件内容转换为图片
     *
     * @param txtFile    TXT文件
     * @param imagesPath 图片文件目录
     * @param pages      最多生成页面
     * @return 图片列表
     * @throws IOException 文件读写异常
     */
    public static int convertTxt2Images(List<String> images, File txtFile, String imagesPath, int pages) throws IOException {
        String str = FileUtils.readFileToString(txtFile, getTxtFileCharset(txtFile)) + "\n";

        Font font = new Font("宋体", Font.PLAIN, 24);
        FontRenderContext frc = new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false);

        int width = 1240, height = 1754;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);

        char c;
        Rectangle2D r;
        int w = width / 14, h = width / 10, p = 1;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append(c);
            r = font.getStringBounds(sb.toString(), frc);
            if (c == '\n' || c == '\r' || r.getWidth() >= width - w * 2) {
                g.drawString(sb.toString(), w, h);
                h += r.getHeight() + 5;
                sb.delete(0, sb.length());
            }
            if (h >= height - width / 10) {
                File img = new File(String.format("%s/%02d_%s.jpg", imagesPath, p, RandomStringUtils.randomAlphanumeric(6)));
                ImageIO.write(image, "jpg", img);
                images.add(img.getName());
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height);
                g.setColor(Color.BLACK);
                h = width / 10;
                p++;
            }
            if (p >= pages) {
                break;
            }
        }
        if (p < pages) {
            File img = new File(String.format("%s/%02d_%s.jpg", imagesPath, p, RandomStringUtils.randomAlphanumeric(6)));
            ImageIO.write(image, "jpg", img);
            images.add(img.getName());
        }
        g.dispose();
        return p;
    }

    /**
     * 获取TXT文件的字符编码
     *
     * @param txt txt文件
     * @return 文件编码
     * @throws IOException 文件不存在或读取失败
     */
    public static String getTxtFileCharset(File txt) throws IOException {
        try (FileInputStream fis = new FileInputStream(txt)) {
            BufferedInputStream bin = new BufferedInputStream(fis);
            int p = (bin.read() << 8) + bin.read();
            bin.close();
            fis.close();
            switch (p) {
                case 12783:
                case 58240:
                case 61371:
                    return "UTF-8";
                case 2608:
                    return "Unicode";
                case 12298:
                    return "UTF-16BE";
                case 33139:
                    return "Shift-JIS";
                default:
                    return "GBK";
            }
        }
    }

    /**
     * 给PDF添加水印
     *
     * @param pdfFile       PDF源文件
     * @param targetPdfFile 目标保存文件
     * @param watermarkFile 水印文件
     * @param opacity       透明度
     */
    public static boolean addWatermarkToPdf(File pdfFile, String targetPdfFile, URL watermarkFile, float opacity) {
        PdfReader reader = null;
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetPdfFile));) {
            com.itextpdf.text.Image watermark = com.itextpdf.text.Image.getInstance(watermarkFile);
            watermark.setRotationDegrees(30);
            reader = new PdfReader(pdfFile.getAbsolutePath());
            PdfStamper stamper = new PdfStamper(reader, bos);
            PdfContentByte content;
            PdfGState state = new PdfGState();
            state.setFillOpacity(opacity);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                com.itextpdf.text.Rectangle pr = reader.getPageSize(i);
                watermark.scalePercent((pr.getWidth() - 100) / watermark.getWidth() * 100);
                watermark.setAbsolutePosition(0, pr.getHeight() * 0.3f);

                content = stamper.getOverContent(i);
                content.setGState(state);
                content.addImage(watermark);
            }
            stamper.close();
            return true;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            File file = new File(targetPdfFile);
            if (file.exists()) {
                FileUtils.deleteQuietly(file);
            }
            return false;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
