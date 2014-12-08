package com.pramati.crawler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class Downloader {

	
	public void downloadData(String url, Crawler crawler) {
		try {
			final String fileName = getFileName(url);
			URL finalUrl = new URL(url);
			final File filePath = new File(fileName);

			FileUtils.copyURLToFile(finalUrl, filePath);

			crawler.visitedUrls.add(url);

		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFileName(String url) throws IOException {

		final File parentDir = new File("./2014mails");
		if (!parentDir.exists()) {
			parentDir.mkdir();
		}

		final String[] spliturl = url.split("/");
		final String fileName = spliturl[5];

		String finalPath = parentDir + "/" + fileName;
		return finalPath;
	}
}
