package com.pramati.crawler;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunCrawler {

	private static final Logger log = LoggerFactory.getLogger(RunCrawler.class
			.getName());

	public static void main(String[] args) {

		Crawler crawler = Crawler.getCrawlerInstance();
		Downloader download = new Downloader();
		try {
			crawler.parseUrls();
			List<String> visitedUrls = crawler.getVisitedUrlList();
			for (String link : crawler.getUnvisitedUrlList()) {
				if (!visitedUrls.contains(link)) {
					String url = download.downloadData(link
							.replace("/date", ""));
					visitedUrls.add(url);
				}

			}
		} catch (IOException ie) {
			log.error(ie.getMessage(),ie);
			return;
		}
	}

}