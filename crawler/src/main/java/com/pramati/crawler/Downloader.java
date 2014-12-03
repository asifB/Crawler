package com.pramati.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Downloader 
{
    public static final String url = "http://mail-archives.apache.org/mod_mbox/maven-users/";
    
	public static void getAbsoluteURL(String url) throws IOException {
		Elements questions = filterUrls(url);
		for (Element link : questions) {
			String absoluteURL = link.attr("abs:href");
			if (absoluteURL.contains("2014") && absoluteURL.contains("date")) {
				Elements newUrls = filterUrls(absoluteURL);
				for (Element link1 : newUrls) {
					String absolute = link1.attr("abs:href");
					if (absolute.endsWith("%3e")) {
						System.out.println("Downloaded from: " + absolute);
					} else if (absolute.contains("date?")) {
						System.out.println("page: " + absolute);
						Elements elements = filterUrls(absolute);
						questions.addAll(elements);
					}
				}
			}
		}
	}

public static Elements filterUrls(String url){
	
	Document doc= null;
    try {
    	System.out.println("Filtering "+url);
	 doc = Jsoup.connect(url).get();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    Elements questions = doc.select("a[href]");
    return questions;
}

public static void main(String[] args) throws IOException {
	getAbsoluteURL(url);
}
	
	
}
