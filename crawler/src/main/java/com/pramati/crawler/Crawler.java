package com.pramati.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author asifb
 */

public class Crawler {

	public List<String> visitedUrls; /*= new ArrayList<String>();*/
	public List<String> unvisitedUrls; /*= new ArrayList<String>();*/
	public static String BASEURL ;

	/**
	 * 
	 * @param url
	 */
	
	public Crawler(){
		readProperties();
		visitedUrls = new ArrayList<String>();
		unvisitedUrls = new ArrayList<String>();
	}
	
	private void readProperties() {
		Properties crawlerProperties = new Properties();
		try {
			InputStream fileInputStream = new FileInputStream(new File(
					"src/main/java/com/pramati/crawler/crawler.properties"));
			crawlerProperties.load(fileInputStream);
			BASEURL = crawlerProperties.getProperty("BASEURL");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void parseUrls() {
		readProperties();
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
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}
