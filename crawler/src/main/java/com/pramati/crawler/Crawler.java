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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asifb
 */

public class Crawler extends Thread{

	private static final Logger log = LoggerFactory.getLogger(Crawler.class
			.getName());

	private static List<String> visitedUrls; 
	private static List<String> toBeDownloadedUrls; 
	private static Crawler crawler;
	public static String BASEURL;

	private Crawler() {
		readProperties();
		visitedUrls = new ArrayList<String>();
		toBeDownloadedUrls = new ArrayList<String>();
	}

	public static Crawler getCrawlerInstance() {
		if (crawler == null) {
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
			log.error(e.getMessage(), e);
			throw new RuntimeException(
					"Could not find file crawler.properties. Cannot start application without base url");
		}
	}

	public int parseUrls() throws IOException {
		int downloadableUrlCount =0;
		
		if (visitedUrls.contains(BASEURL)) {
			return downloadableUrlCount;
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
					log.info("Parsing.. "+absoluteURL);
					if (!visitedUrls.contains(absoluteURL)
							|| !toBeDownloadedUrls.contains(absoluteURL)) {
						toBeDownloadedUrls.add(absoluteURL);
						downloadableUrlCount++;
					}
				}
			}
		} catch (IOException ie) {
			throw new IOException(ie.getMessage());
		}
		return downloadableUrlCount;
	}

	public void run() {
		try {
			log.info("Parsing urls....");
			parseUrls();
		} catch (IOException ie) {
			log.error(ie.getMessage(), ie);
			Thread pingerThread = new Thread(new Pinger(BASEURL), "Pinger");
			log.info("Crawler got Interupted...Starting Pinger...");
			pingerThread.run();
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getUnvisitedUrlList() {
		return new ArrayList<String>(toBeDownloadedUrls);
	}

	public List<String> getVisitedUrlList() {
		return new ArrayList<String>(visitedUrls);
	}
}
