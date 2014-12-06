package com.pramati.crawler;

public class RunCrawler {
	
	public static void main(String[] args) throws Exception {
		Crawler.parseUrls();
		for (String link : Crawler.unvisitedUrls) {
			if (!Crawler.visitedUrls.contains(link)) {
				
				Downloader.downloadData(link.replace("/date", ""));
			}
		}

	}

}
