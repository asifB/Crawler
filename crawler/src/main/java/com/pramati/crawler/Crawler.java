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

public class Crawler extends Thread {

	private static final Logger log = LoggerFactory.getLogger(Crawler.class
			.getName());

	private static List<String> visitedUrls;
	private static List<String> toBeDownloadedUrls;
	private static int parsedUrlCount = 0;

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

	public int parseUrls(int index) {
		int downloadableUrlCount = index;

		if (visitedUrls.contains(BASEURL)) {
			return downloadableUrlCount;
		}

		Document doc = null;
		String absoluteURL = "";
		try {
			doc = Jsoup.connect(BASEURL).get();
			final Elements mailLinks = doc.select("a[href]");
			List<String> elementList = new ArrayList<String>();

			for (final Element link : mailLinks) {
				absoluteURL = link.absUrl("href");
				elementList.add(absoluteURL);
			}
			int i = 0;
			for (i = index; i < elementList.size(); i++) {
				if ((elementList.get(i)).contains("2014")
						&& (elementList.get(i)).contains("date")) {
					log.info("Parsing.. " + absoluteURL);
					if (!visitedUrls.contains(absoluteURL)
							|| !toBeDownloadedUrls.contains(absoluteURL)) {
						toBeDownloadedUrls.add(elementList.get(i));
					}
				}
				downloadableUrlCount++;
			}
		} catch (IOException ie) {
			Thread pingerThread = new Thread(new Pinger(BASEURL), "Pinger");
			log.error(ie.getMessage(), ie);
			log.info("Crawler got Interupted...Starting Pinger...");
			synchronized (pingerThread) {
				pingerThread.start();
				try {
					pingerThread.wait();
					this.parseUrls(parsedUrlCount);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return downloadableUrlCount;
	}

	public void run() {
		parsedUrlCount = parseUrls(parsedUrlCount);
	}

	public List<String> getUnvisitedUrlList() {
		return new ArrayList<String>(toBeDownloadedUrls);
	}

	public List<String> getVisitedUrlList() {
		return new ArrayList<String>(visitedUrls);
	}
}
