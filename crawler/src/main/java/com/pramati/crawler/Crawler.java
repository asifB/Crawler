package com.pramati.crawler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author asifb
 */

public class Crawler {

	public static List<String> visitedUrls = new ArrayList<String>();
	public static List<String> unvisitedUrls = new ArrayList<String>();
	public static final String BASEURL = "http://mail-archives.apache.org/mod_mbox/maven-users/";

	/**
	 * 
	 * @param url
	 */
	public static void parseUrls(final String url) {

		if (visitedUrls.contains(url)) {
			return;
		}

		Document doc = null;
		String absoluteURL = "";
		try {
			doc = Jsoup.connect(url).get();
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

	/**
	 * @return void
	 * @param url
	 */
	public static void downloadData(String url) {
		try {
			final String fileName = getFileName(url);
			URL finalUrl = new URL(url);
			final File filePath = new File(fileName);

			FileUtils.copyURLToFile(finalUrl, filePath);

			visitedUrls.add(url);

		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getFileName(String url) throws IOException {

		final File parentDir = new File("./2014mails");
		if (!parentDir.exists()) {
			parentDir.mkdir();
		}

		final String[] spliturl = url.split("/");
		final String fileName = spliturl[5];

		String finalPath = parentDir + "/" + fileName;
		return finalPath;
	}

	public static void main(String[] args) throws Exception {
		parseUrls(BASEURL);
		for (String link : unvisitedUrls) {
			if (!visitedUrls.contains(link)) {
				
				downloadData(link.replace("/date", ""));
			}
		}

	}

}
