package com.pramati.crawler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Downloader {

	private static final Logger log = LoggerFactory.getLogger(Downloader.class
			.getName());

	public String downloadData(String url) {

		URL finalUrl = null;
		try {
			finalUrl = new URL(url);
			final String fileName = getFileName(url);
			final File filePath = new File(fileName);

			FileUtils.copyURLToFile(finalUrl, filePath);

		} catch (IOException ie) {
			log.error(ie.getMessage(),ie);
			finalUrl = null;
		}
		return finalUrl.toString();
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
