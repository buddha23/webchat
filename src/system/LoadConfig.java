package system;

import java.io.File;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import crawler.search.SearchBKeyword;
import util.FileUtils;
import util.XmlUtil;

/**
 * @author Herman.Xiong
 * @date: 2014年6月19日 13:40:01 
 * @see 此类用于加载配置文件
 */
public class LoadConfig {

	private static Logger log4j = Logger.getLogger(LoadConfig.class.getName());
	private static String userDir;
	private static final String demoConfigPath = "config"+File.separator+"demoConfig.xml";
	
	public static void main(String[] args) {
		loadDemoConfig();
		System.out.println(SearchBKeyword.pagesize);
	}

	/**
	 * @author Herman.Xiong
	 * @date 2011-12-21 下午02:58:50 description:初始化方法，加载相关配置文件
	 */
	public static void init() {
		userDir = System.getProperty("user.dir");
		loadDemoConfig();
	}


	/**
	 * @author Herman.Xiong
	 * @date 2014年6月19日 13:39:31
	 */
	public static String getDir() {
		return userDir == null ? System.getProperty("user.dir") : userDir;
	}

	/**
	 * @author Herman.Xiong
	 * @date 2011-12-21 下午02:59:29 description:
	 */
	public static void reload() {
		
	}

	/**
	 * @author Herman.Xiong
	 * @date 2014年6月19日 13:33:23
	 */
	public static void loadDemoConfig() {
		try {
			Document document = XmlUtil.loadXML(FileUtils.makeFilePath(getDir(), demoConfigPath));
			Element rootElement = XmlUtil.getRootElement(document);
			Element collecterRrootElement = XmlUtil.getElementChildElement(rootElement, "demo-config");
			/**加载采集关键词*/
			String []keywords= ((Element) XmlUtil.getElementChildElement(
					collecterRrootElement, "demo-keywords")).getStringValue().split(",");
			if(keywords!=null&&keywords.length>0){
				for (int i = 0; i < keywords.length; i++) {
					SearchBKeyword.keywordList.add(keywords[i]);
				}
			}
			/**加载采集页数*/
			SearchBKeyword.pagesize=Integer.parseInt(((Element) XmlUtil.getElementChildElement(
					collecterRrootElement, "demo-pagesize")).getStringValue());
		} catch (Exception e) {
			log4j.info("Load demo config fail!" + e.getMessage(),e);
		}
	}
}
