package com.pramati.crawler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunCrawler {


	private static final Logger log = LoggerFactory.getLogger(RunCrawler.class
			.getName());

	public static void main(String[] args) {
		Crawler crawler = Crawler.getCrawlerInstance();
		Downloader download = new Downloader();
		log.info("Starting Crawler...");
			crawler.run();
			List<String> visitedUrls = crawler.getVisitedUrlList();
			for (String link : crawler.getUnvisitedUrlList()) {
				log.info("Downloading...."+link);
				if (!visitedUrls.contains(link)) {
					String url = download.downloadData(link
							.replace("/date", ""));
					visitedUrls.add(url);
				}
		} 
	}
}