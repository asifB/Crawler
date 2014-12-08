package com.pramati.crawler;

public class RunCrawler {
	
	public static void main(String[] args) throws Exception {
		Crawler crawler = new Crawler();
		Downloader download = new Downloader();
		crawler.parseUrls();
		for (String link : crawler.unvisitedUrls) {
			if (!crawler.visitedUrls.contains(link)) {
				download.downloadData(link.replace("/date", ""),crawler);
			}
		}

	}

}
