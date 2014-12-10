package com.pramati.crawler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

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
			log.info("Ping Succesful....Resuming Crawler");
			Crawler.currentThread().notify();
		}
	}

	public boolean ping() {

		InetAddress address;
		boolean isPingSuccessful = false;
		try {
			log.info(pingUrl.getHost());

			address = InetAddress.getByName(pingUrl.getHost());
			isPingSuccessful = address.isReachable(20000);
			log.info("isPingSuccessful: " + isPingSuccessful);

		} catch (UnknownHostException e) {
			log.error("UnknownHostException at Pinger!!");
			log.info("Waiting to ping again...");
		} catch (IOException e1) {
			log.error(e1.getMessage(), e1);
			log.info("Waiting to ping again...");
		}
		return isPingSuccessful;
	}

}
