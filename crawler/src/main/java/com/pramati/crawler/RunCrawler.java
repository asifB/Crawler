package com.pramati.crawler;

public class RunCrawler {
	
	public static void main(String[] args) throws Exception {
		Crawler crawler = new Crawler();
		crawler.parseUrls();
		for (String link : crawler.unvisitedUrls) {
			if (!crawler.visitedUrls.contains(link)) {
				Downloader download = new Downloader();
				download.downloadData(link.replace("/date", ""),crawler);
			}
		}

	}

}
