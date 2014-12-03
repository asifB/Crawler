package com.pramati.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public static List<String> visitedUrls = new ArrayList<String>();
	public static List<String> unvisitedUrls = new ArrayList<String>();
	public static final String baseUrl = "http://mail-archives.apache.org/mod_mbox/maven-users/";

	public static void parseUrls(String url) {
		if (visitedUrls.contains(url)) {
			return;
		}

		System.out.println("parsing: " + url);
		Document doc = null;
		String absoluteURL = "";
		try {
			doc = Jsoup.connect(url).get();
			Elements mailLinks = doc.select("a[href]");
			for (Element link : mailLinks) {
				absoluteURL = link.attr("abs:href");
				if (absoluteURL.contains("2014")
						&& (absoluteURL.contains("date") || absoluteURL
								.endsWith("%3e"))) {
					System.out.println("absoluteURL: " + absoluteURL);
					if (!visitedUrls.contains(absoluteURL)
							|| !unvisitedUrls.contains(absoluteURL)) {
						unvisitedUrls.add(absoluteURL);
						System.out.println(unvisitedUrls.size());
						if (!absoluteURL.endsWith("%3e")) {
							parseUrls(absoluteURL);
						}
					}

				}
				if (!visitedUrls.contains(url)) {
					visitedUrls.add(url);
				}
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public static void main(String[] args) {
		parseUrls(baseUrl);
		for (String link : visitedUrls) {
			System.out.println(link);
		}
		System.out.println(visitedUrls.size());
	}

}
