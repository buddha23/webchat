package com.lld360.cnc.core.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
/**
 * 本类应用了Apache POI系统库，POI 是 Apache 下的 Jakata 项目的一个子项目，主要用于提供 Java 操作 Microsoft Office 办公套件如 Excel，Word，Powerpoint 等文件的 API。
 * @author 杨少波
 *
 */
public class ConvertPPTFileToImage {

    private int maxPages = 0;

    public ConvertPPTFileToImage() {
    }

    /**
     * convertPPTXtoImage方法完成将PPTX格式文件转换为指定格式的图片，本方法需要如下的第3方的系统库：
     * xmlbeans-2.6.0.jar、poi-scratchpad-3.11-20141221.jar、 poi-ooxml-3.11-20141221.jar、poi-3.11-20141221.jar、poi-ooxml-schemas-3.11-20141221.jar
     * @param orignalPPTFile	代表需要转换的PPT图片的文件路径和文件名称，如"D:/软件项目程序/Demo/demo1.pptx"
     * @param targetImageFileDir	代表转换后的各个图片文件的保存路径，如"D:/软件项目程序/Demo/pptImg/"
     * @param imageFormatNameString 代表转换的图片格式的字符串， 如“gif”、“jpg”
     * @return 返回文档总页数
     */
    public int convertPPTXtoImage(File orignalPPTFile,String targetImageFileDir, String imageFormatNameString, List<String> imageNames) throws Exception {
        int totalPages = 0;

        FileInputStream orignalPPTFileInputStream =null;
        FileOutputStream orignalPPTFileOutputStream =null;
        /**
         *  org.apache.poi.hslf, 这个包只支持 PowerPoint 2007 以前的 ppt 文档解析. 因为 Office 2007 的文件底层实现方案（OOXML）和以前的版本（OLE2）有根本的变化。
         *  支持 office 2007 的包为: org.apache.poi.xslf
         */
        XMLSlideShow oneSlideShow =null;
        try{
            orignalPPTFileInputStream = new FileInputStream(orignalPPTFile);
            oneSlideShow = new XMLSlideShow(orignalPPTFileInputStream);
            /**
             * 获得PPT每页的尺寸大小（宽和高度）
             */
            Dimension onePPTPageSize = oneSlideShow.getPageSize();
            /**
             * 获得PPT文件中的所有的PPT页面，并转换为一张张的播放片
             */
            XSLFSlide[] pptPageXSLFSlideArray = oneSlideShow.getSlides();

            totalPages = pptPageXSLFSlideArray.length;

            /**
             * 下面的XML配置文件定义转换后的图片内的文字字体，否则将会出现转换后的图片内的中文为乱码
             */
            String xmlFontFormat1="<xml-fragment xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">";
            String xmlFontFormat2=		"<a:rPr lang=\"zh-CN\" altLang=\"en-US\" dirty=\"0\" smtClean=\"0\"> ";
            String xmlFontFormat3=			"<a:latin typeface=\"+mj-ea\"/> ";
            String xmlFontFormat4=		"</a:rPr>";
            String xmlFontFormat5="</xml-fragment>";
            StringBuffer xmlFontFormatStringBuffer=new StringBuffer();
            xmlFontFormatStringBuffer.append(xmlFontFormat1);
            xmlFontFormatStringBuffer.append(xmlFontFormat2);
            xmlFontFormatStringBuffer.append(xmlFontFormat3);
            xmlFontFormatStringBuffer.append(xmlFontFormat4);
            xmlFontFormatStringBuffer.append(xmlFontFormat5);

            for (int pptPageXSLFSlideIndex = 0;  pptPageXSLFSlideIndex <  Math.min(totalPages, this.maxPages);  pptPageXSLFSlideIndex++) {
                /**
                 *设置字体为宋体，解决中文乱码问题
                 */
                CTSlide oneCTSlide=pptPageXSLFSlideArray[pptPageXSLFSlideIndex].getXmlObject();

                CTGroupShape oneCTGroupShape = oneCTSlide.getCSld().getSpTree();
                CTShape[] oneCTShapeArray = oneCTGroupShape.getSpArray();
                for (CTShape oneCTShape : oneCTShapeArray) {
                    CTTextBody oneCTTextBody = oneCTShape.getTxBody();
                    if (null == oneCTTextBody){
                        continue;
                    }
                    CTTextParagraph[] oneCTTextParagraph = oneCTTextBody.getPArray();
                    CTTextFont oneCTTextFont =null;
                    oneCTTextFont = CTTextFont.Factory.parse(xmlFontFormatStringBuffer.toString());

                    for (CTTextParagraph textParagraph : oneCTTextParagraph) {
                        CTRegularTextRun[] oneCTRegularTextRunArray = textParagraph.getRArray();
                        for (CTRegularTextRun oneCTRegularTextRun : oneCTRegularTextRunArray) {
                            CTTextCharacterProperties oneCTTextCharacterProperties=oneCTRegularTextRun.getRPr();
                            oneCTTextCharacterProperties.setLatin(oneCTTextFont);
                        }
                    }
                }

                /**
                 * 创建BufferedImage对象，图象的尺寸为原来PPT的每页的尺寸
                 */
                BufferedImage image = new BufferedImage(onePPTPageSize.width, onePPTPageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                try {
                    pptPageXSLFSlideArray[pptPageXSLFSlideIndex].draw(g);
                }catch (Exception e){//遇到无法处理的页面
                    continue;
                }
                g.dispose();

                String imgFile = String.format("%s/%02d_%s.jpg", targetImageFileDir, pptPageXSLFSlideIndex + 1, RandomStringUtils.randomAlphanumeric(6));
                File img = new File(imgFile);
                ImageIO.write(image, imageFormatNameString, img);
                image.flush();
                imageNames.add(img.getName());

            }
        }
        finally{
            if(orignalPPTFileInputStream != null ){
                orignalPPTFileInputStream.close();
            }
            if(orignalPPTFileOutputStream != null ){
                orignalPPTFileOutputStream.close();
            }
        }
        return totalPages;
    }

    /**
     * convertPPTXtoImage方法完成将PPTX格式文件转换为指定格式的图片，本方法需要如下的第3方的系统库：
     * xmlbeans-2.6.0.jar、poi-scratchpad-3.11-20141221.jar、 poi-ooxml-3.11-20141221.jar、poi-3.11-20141221.jar、poi-ooxml-schemas-3.11-20141221.jar
     * @param orignalPPTFile	代表需要转换的PPT图片的文件路径和文件名称，如"D:/软件项目程序/Demo/demo1.pptx"
     * @param targetImageFileDir	代表转换后的各个图片文件的保存路径，如"D:/软件项目程序/Demo/pptImg/"
     * @param imageFormatNameString 代表转换的图片格式的字符串， 如“gif”、“jpg”
     * @return 返回文档总页数
     */
    public int convertPPTtoImage(File orignalPPTFile,String targetImageFileDir, String imageFormatNameString, List<String> imageNames) throws Exception {
        int totalPages = 0;
        /**
         * convertReturnResult代表转换的结果，返回 false则表示转换不成功
         */
        boolean convertReturnResult =false;
        FileInputStream orignalPPTFileInputStream =null;
        FileOutputStream orignalPPTFileOutputStream =null;
        /**
         *  org.apache.poi.hslf, 这个包只支持 PowerPoint 2007 以前的 ppt 文档解析. 因为 Office 2007 的文件底层实现方案（OOXML）和以前的版本（OLE2）有根本的变化。
         *  支持 office 2007 的包为: org.apache.poi.xslf。
         *  下面的SlideShow类表示PPT文档，而Slide则表示PPT文档中的某一张幻灯片。
         */
        SlideShow oneSlideShow =null;
        try{
            orignalPPTFileInputStream = new FileInputStream(orignalPPTFile);
            oneSlideShow = new SlideShow(orignalPPTFileInputStream);

            /**
             * 获得PPT每页的尺寸大小（宽和高度）
             */
            Dimension onePPTPageSize = oneSlideShow.getPageSize();
            /**
             * 获得PPT文件中的所有的PPT页面（获得每一张幻灯片），并转换为一张张的播放片
             */
            Slide[] pptPageSlideArray = oneSlideShow.getSlides();

            totalPages = pptPageSlideArray.length;

            /**
             * 下面的循环的主要功能是实现对PPT文件中的每一张幻灯片进行转换和操作。
             */
            for (int pptPageSlideIndex = 0;  pptPageSlideIndex < Math.min(totalPages, this.maxPages) ;  pptPageSlideIndex++) {

                TextRun[] textRunsArray = pptPageSlideArray[pptPageSlideIndex].getTextRuns();

                for (int textRunsArrayIndex = 0; textRunsArrayIndex < textRunsArray.length; textRunsArrayIndex++) {
                    RichTextRun[] pptRichTextRunsArray = textRunsArray[textRunsArrayIndex].getRichTextRuns();
                    for (int pptRichTextRunsArrayIndex = 0; pptRichTextRunsArrayIndex < pptRichTextRunsArray.length; pptRichTextRunsArrayIndex++) {
                        /**原来是直接设置为宋体，这样会使得转换后的图片上的文字与原PPT的文字不一致，应该改变为应用PPT本身的字体名
                         * pptRichTextRunsArray[pptRichTextRunsArrayIndex].setFontIndex(1);
                         * pptRichTextRunsArray[pptRichTextRunsArrayIndex].setFontName("宋体");
                         */
                        /**
                         * 获得某个文本框内的字体名字符串，并识别是否为null（未正确地获得相关的字体名字符串），则设置为默认的字体名——宋体
                         * 但如果PPT文件在WPS中保存过，则pptRichTextRunsArray[pptRichTextRunsArrayIndex].getFontSize()的值可能为0或者26040。因此，
                         * 首先识别当前文本框内的字体尺寸是否为0或者大于26040，则设置默认的字体尺寸。
                         */
                        int currentFontSize = pptRichTextRunsArray[pptRichTextRunsArrayIndex].getFontSize();
                        if((currentFontSize <=0)||(currentFontSize >=26040)){
                            pptRichTextRunsArray[pptRichTextRunsArrayIndex].setFontSize(30);
                        }

                        String currentFontName=pptRichTextRunsArray[pptRichTextRunsArrayIndex].getFontName();
                        String text = pptRichTextRunsArray[pptRichTextRunsArrayIndex].getText();
//						if(text.length()==1 && text.equals("*")){
//							continue;
//						}
                        if(currentFontName == null){

                            pptRichTextRunsArray[pptRichTextRunsArrayIndex].setFontName("宋体");
                        }
                        else{
                            pptRichTextRunsArray[pptRichTextRunsArrayIndex].setFontName(currentFontName);
                        }
                        /**
                         * 应用 pptRichTextRunsArray[pptRichTextRunsArrayIndex].getText() 可以获得其中的文字
                         * int pptRichTextRunsNumberIndex = pptRichTextRunsArray[pptRichTextRunsArrayIndex].getFontIndex();
                         String fontNameForPPTText = pptRichTextRunsArray[pptRichTextRunsArrayIndex].getFontName();
                         */
                    }
                }

                /**
                 * 创建BufferedImage对象，图象的尺寸为原来PPT的每页的尺寸
                 */
                BufferedImage image = new BufferedImage(onePPTPageSize.width, onePPTPageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                try {
                    pptPageSlideArray[pptPageSlideIndex].draw(g);
                }catch (Exception e){//遇到无法处理的页面
                    continue;
                }
                g.dispose();

                String imgFile = String.format("%s/%02d_%s.jpg", targetImageFileDir, pptPageSlideIndex + 1, RandomStringUtils.randomAlphanumeric(6));
                File img = new File(imgFile);
                ImageIO.write(image, imageFormatNameString, img);
                image.flush();
                imageNames.add(img.getName());
            }
        }
        finally{
            if(orignalPPTFileInputStream != null ){
                orignalPPTFileInputStream.close();
            }
            if(orignalPPTFileOutputStream != null ){
                orignalPPTFileOutputStream.close();
            }
        }
        return totalPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }
}
