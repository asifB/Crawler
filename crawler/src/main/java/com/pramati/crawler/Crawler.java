package com.pramati.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asifb
 */

public class Crawler {

	private static final Logger log = LoggerFactory.getLogger(Crawler.class.getName());

	
	private  static List<String> visitedUrls; /*= new ArrayList<String>();*/
	private  static List<String> unvisitedUrls; /*= new ArrayList<String>();*/
	private  static Crawler crawler;
	public   static String BASEURL ;
	
	private Crawler(){
		readProperties();
		visitedUrls = new ArrayList<String>();
		unvisitedUrls = new ArrayList<String>();
	}
	
	public static Crawler getCrawlerInstance(){
		if(crawler==null){
			crawler = new Crawler();
		}
		return crawler;
		
	}
	
	private void readProperties() {
		Properties crawlerProperties = new Properties();
		try {
			InputStream fileInputStream = new FileInputStream(new File(
					"src/main/java/com/pramati/crawler/crawler.properties"));
			crawlerProperties.load(fileInputStream);
			BASEURL = crawlerProperties.getProperty("BASEURL");
			
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}
	public void parseUrls() throws UnknownHostException {
		if (visitedUrls.contains(BASEURL)) {
			return;
		}

		Document doc = null;
		String absoluteURL = "";
		try {
			doc = Jsoup.connect(BASEURL).get();
			final Elements mailLinks = doc.select("a[href]");
			for (final Element link : mailLinks) {
				absoluteURL = link.absUrl("href");
				if (absoluteURL.contains("2014")
						&& absoluteURL.contains("date")) {
					if (!visitedUrls.contains(absoluteURL)
							|| !unvisitedUrls.contains(absoluteURL)) {
						unvisitedUrls.add(absoluteURL);

					}

				}
			}
		} catch(UnknownHostException ue){
			log.error(ue.getMessage(),ue);
			throw new UnknownHostException("Connection Establishment failed While parsing Urls");
		}catch (IOException ie) {
			log.error(ie.getMessage(),ie);
		}catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	public List<String> getUnvisitedUrlList(){
		return new ArrayList<String>(unvisitedUrls);
	}
	
	public List<String> getVisitedUrlList(){
		return new ArrayList<String>(visitedUrls);
	}
}
