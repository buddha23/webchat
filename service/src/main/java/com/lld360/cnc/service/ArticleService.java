package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.ArticleDao;
import com.lld360.cnc.repository.ArticleFileDao;
import com.lld360.cnc.repository.SettingDao;
import com.lld360.cnc.repository.TagDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService extends BaseService {

    @Autowired
    private MonthReportService monthReportService;

    @Autowired
    private Configer configer;

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private ArticleFileDao articleFileDao;

    @Autowired
    private SynonymService synonymService;

    @Autowired
    private TaskExecutor taskExecutor;

    int prevArticleId = 0;

    //######################################## 业务实现
    //根据关键字查询文章
    public List<Article> getArticlesByKeyWord(String keyword) {
        return articleDao.findByKeyWord(synonymService.getSynonymsRegexp(keyword));
    }

    //分页查询
    public Page<Article> getArticles(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = articleDao.countByMap(params);
        List<Article> articles = count > 0 ? articleDao.findByMap(params) : new ArrayList<>();
        return new PageImpl<>(articles, pageable, count);
    }

    // 添加文章
    public Article add(Article article) {
        article.setViews(RandomUtils.nextInt(100, 1000));
        article.setCreateTime(new Date());
        articleDao.create(article);
        setArticleTags(article);
        return article;
    }

    //查看文章详细内容
    public Article getArticle(long id) {
        //增加文章访问量
        articleDao.addViews(id);


        //增加文章月统计访问信息
        monthReportService.addArticleViews();


        return articleDao.find(id);
    }

    // 更新文章
    public void update(Article article) {
        setArticleTags(article);
        articleDao.update(article);
    }

    // 删除旧PDF
    private boolean delOldPdf(String relativePath) {
        File file = new File(configer.getFileBasePath() + relativePath);
        return !file.exists() || file.delete();
    }

    // 删除文章旧图片
    private void delOldImages(String images) {
        if (StringUtils.isEmpty(images)) {
            return;
        }
        String[] imgs = images.split(",");
        File file;
        for (String img : imgs) {
            file = new File(configer.getFileBasePath() + img);
            if (file.exists() && !file.delete()) {
                logger.warn("删除文件失败：" + img);
            }
        }
    }

    // 上传pdf并更新文章图片
    @Transactional
    public Article uploadArticlePdf(long id, MultipartFile file) throws IOException {
        Article article = articleDao.find(id);
        if (article == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404, "指定的文档不存在！");
        }
        String pdfPath = "article/" + article.getId() + "/" + article.getTitle() + ".pdf";
        File pdfFile = new File(configer.getFileBasePath() + pdfPath);
        File dir = pdfFile.getParentFile();
        if (dir.exists() || dir.mkdirs()) {
            file.transferTo(pdfFile);

            // 保存Doc数据
            ArticleFile articleFile;
            if (article.getFileId() == null) {
                articleFile = new ArticleFile(article.getTitle(), pdfPath, RandomUtils.nextInt(100, 10000));
                articleFileDao.create(articleFile);
                article.setFileId(articleFile.getId());
            } else {
                articleFile = articleFileDao.find(article.getFileId());
                articleFile.setName(article.getTitle());
                articleFile.setPath(pdfPath);
                articleFileDao.update(articleFile);
            }
            article.setPdf(pdfPath);
            delOldImages(article.getImages());

            List<String> imgs = new LinkedList<>();
            try (PDDocument document = PDDocument.load(pdfFile)) {
                for (int i = 0; i < Math.min(5, document.getNumberOfPages()); i++) {
                    BufferedImage image = new PDFRenderer(document).renderImageWithDPI(i, 100, ImageType.ARGB);
                    String imgPath = "article/" + article.getId() + "/" + RandomStringUtils.randomAlphanumeric(8) + ".png";
                    ImageIO.write(image, "png", new File(configer.getFileBasePath() + imgPath));
                    imgs.add(imgPath);
                }
                document.close();
            } catch (Exception e) {
                logger.warn("解析PDF出错", e);
            }
            if (!imgs.isEmpty()) {
                article.setImages(String.join(",", imgs));
            } else {
                article.setImages(null);
            }
            articleDao.update(article);
        }
        article.setPdf(pdfPath);
        return article;
    }


    public void download() throws IOException {
        // 获取上次最后的文章ID
        prevArticleId = Integer.parseInt(settingDao.find(Const.SETTING_CNC_ARTICLE_LAST_ID).getContent());
        Article article;
        int inexistent = 0;
        while (inexistent < 5 && prevArticleId < 150) {
            int id = prevArticleId + inexistent + 1;
            article = getArticle(id);
            if (article == null) {
                inexistent++;
            } else {
                store(article);
                prevArticleId = id;
                settingDao.update(new Setting(Const.SETTING_CNC_ARTICLE_LAST_ID, String.valueOf(prevArticleId)));
            }
        }
    }

    //异步下载
    public void ansyDownload() {
        taskExecutor.execute(() -> {
            try {
                context.setAttribute(Const.CTX_KEY_CNC_DOWNLOAD, true);
                download();
            } catch (Exception e) {
                logger.warn("下载数据异常：" + e.getMessage() + "，当前数据ID：" + prevArticleId, e);
            } finally {
                context.removeAttribute(Const.CTX_KEY_CNC_DOWNLOAD);
            }
        });
    }

    //######################################## 爬数据
    private Article getArticle(int id) throws IOException {
        Document document = Jsoup.connect("http://www.cncengineer.cn/home.php/Home/Document/showDocDetail/doc_id/" + getCryptographicId(id) + ".html").timeout(5000).get();
        if (document.getElementsByClass("error").size() > 0) {          //文章不存在
            return null;
        }
        Article article = new Article();
        article.setTitle(document.getElementsByClass("docTitle").get(0).text().replaceAll("[\"',\\\\#/\\s]", ""));
        article.setSummary(document.getElementsByClass("docContent").get(0).getElementsByTag("p").get(3).text().trim());
        article.setTags(StringUtils.join(getTags(document), ','));
        article.setPdf("http://www.cncengineer.cn" + document.getElementsByClass("header_link").get(0).getElementsByTag("a").get(0).attr("href"));
        article.setImages(getImages(document));
        return article;
    }

    //获取文章的标签
    private String[] getTags(Document document) {
        String[] alarmKeywords = document.getElementsByClass("docContent").get(0).getElementsByTag("p").get(0).text().replaceAll("报警关键字：", "").split(",");
        String[] contentKeywords = document.getElementsByClass("docContent").get(0).getElementsByTag("p").get(1).text().replaceAll("内容关键字：", "").split(",");
        return Arrays.stream(ArrayUtils.addAll(alarmKeywords, contentKeywords)).distinct().toArray(String[]::new);
    }

    //获取文章的图片链接
    private String getImages(Document document) throws IOException {
        List<String> hrefs = document.getElementsByClass("imgContent").stream().map(element -> "http://www.cncengineer.cn" + element.attr("src")).collect(Collectors.toList());
        return StringUtils.join(hrefs, ",");
    }

    // 文章ID加密
    private String getCryptographicId(int id) throws UnsupportedEncodingException {
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(String.valueOf(id).getBytes());
        str = encoder.encode(str.getBytes());
        return URLEncoder.encode(str, "UTF-8");
    }

    //######################################## 数据入库
    @Transactional
    public void store(Article article) throws IOException {
        article.setCreateTime(new Date());
        article.setViews(0);
        articleDao.create(article);
        downloadPdf(article);             //下载PDF
        downloadImages(article);          //下载图片
        setArticleTags(article);
        articleDao.update(article);
    }

    // 下载PDF
    private Article downloadPdf(Article article) throws IOException {
        String pdfPath = "article/" + article.getId() + "/" + new Date().getTime() + ".pdf";
        FileUtils.copyURLToFile(new URL(article.getPdf()), new File(configer.getFileBasePath() + pdfPath));
        ArticleFile articleFile = new ArticleFile(article.getTitle(), pdfPath, (int) (Math.random() * 2000));
        articleFileDao.create(articleFile);
        article.setFileId(articleFile.getId());
        return article;
    }

    // 下载图片
    private Article downloadImages(Article article) throws IOException {
        List<String> paths = new ArrayList<>();
        String[] urls = article.getImages().split(",");
        String path;
        for (int i = 0; i < urls.length; i++) {
            path = "article/" + article.getId() + "/" + (i + 1) + urls[i].substring(urls[i].lastIndexOf("."));
            FileUtils.copyURLToFile(new URL(urls[i]), new File(configer.getFileBasePath() + path));
            paths.add(path);
        }
        article.setImages(StringUtils.join(paths, ","));
        return article;
    }

    // 设置文档标签
    @Transactional
    private void setArticleTags(Article article) {
        tagDao.deleteArticleXTagByArticleId(article.getId());
        if (StringUtils.isNotEmpty(article.getTags())) {
            String[] tags = Arrays.stream(article.getTags().split(",")).distinct().toArray(String[]::new);
            Tag tag;
            List<ArticleXTag> axts = new ArrayList<>();
            for (String t : tags) {
                tag = tagDao.find(t);
                if (tag == null) {
                    tag = new Tag(t);
                    tagDao.create(tag);
                }
                axts.add(new ArticleXTag(article.getId(), tag.getId()));
            }
            if (!axts.isEmpty()) {
                tagDao.addArticleXTags(axts);
            }
        }
    }

    // 删除Article
    @Transactional
    public void delete(long id) {
        Article article = articleDao.find(id);
        if (article != null) {
            tagDao.deleteArticleXTagByArticleId(id);
            articleDao.delete(id);
            if (article.getFileId() != null) {
                articleFileDao.delete(id);
            }
            try {
                FileUtils.deleteDirectory(new File(configer.getFileBasePath() + "article/" + id));
            } catch (IOException e) {
                logger.warn("删除Article文件夹失败", e);
            }
        }
    }

    public void updateArticleFile(ArticleFile articleFile) {
        articleFileDao.update(articleFile);
    }
}
