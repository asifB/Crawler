package com.pramati.crawler;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pinger implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(Pinger.class
			.getName());

	URL pingUrl = null;

	public Pinger(String url) {
		try {
			pingUrl = new URL(url);
		} catch (MalformedURLException e) {
			log.error(e.getMessage(),e);
		}
	}

	public void run() {
		boolean isPingSuccessful = false;
		while (!isPingSuccessful) {
				isPingSuccessful = ping();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

		if (isPingSuccessful) {
			Crawler.currentThread().notify();
			log.info("Ping Succesful....Resuming Crawler");
		}
	}

	public boolean ping() {

		boolean isPingSuccessful = true;
		try {
			log.info("Pinging... "+pingUrl);
			
			HttpURLConnection urlConnect = (HttpURLConnection) pingUrl
					.openConnection();
			urlConnect.setConnectTimeout(60000);
			Object objData = urlConnect.getContent();

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			isPingSuccessful = false;
		}
		log.info("isPingSuccessful: "+isPingSuccessful);
		return isPingSuccessful;
	}

}
