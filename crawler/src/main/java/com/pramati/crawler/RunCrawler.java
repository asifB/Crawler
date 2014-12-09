package com.pramati.crawler;

import java.net.UnknownHostException;
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
		} catch (UnknownHostException ue) {
			log.error(ue.getMessage(), ue);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		for (String link : crawler.getUnvisitedUrlList()) {
			List<String> visitedUrls = crawler.getVisitedUrlList();
			if (!visitedUrls.contains(link)) {
				String url = download.downloadData(link.replace("/date", ""));
				visitedUrls.add(url);
			}

		}
	}

}