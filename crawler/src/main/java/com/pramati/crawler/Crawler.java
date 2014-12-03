package com.pramati.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public static List<String> visitedUrls = new ArrayList<String>();
	public static List<String> unvisitedUrls = new ArrayList<String>();
	public static final String baseUrl = "http://mail-archives.apache.org/mod_mbox/maven-users/";

	public static void parseUrls(String url) {
		if (visitedUrls.contains(url)) {
			return;
		}

		Document doc = null;
		String absoluteURL = "";
		try {
			doc = Jsoup.connect(url).get();
			Elements mailLinks = doc.select("a[href]");
			for (Element link : mailLinks) {
				absoluteURL = link.attr("abs:href");
				if (absoluteURL.contains("2014")
						&& (absoluteURL.contains("date") || absoluteURL
								.endsWith("%3e"))) {
					if (!visitedUrls.contains(absoluteURL)
							|| !unvisitedUrls.contains(absoluteURL)) {
						unvisitedUrls.add(absoluteURL);
						if (!absoluteURL.endsWith("%3e")) {
							parseUrls(absoluteURL);
						}
					}

				}
				if (!visitedUrls.contains(url)) {
					visitedUrls.add(url);
				}
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	
	public static void downloadData(String url) {
		try{
/*		String url = "http://mail-archives.apache.org/mod_mbox/maven-users/201411.mbox/%3cCAKumxBwW6ArNEFSUUW=Q4bOvVXv=86GcB9JnX++JTz3Wbc04Bg@mail.gmail.com%3e";
*/		Document doc = Jsoup.connect(url).get();
		String fileName = "";
		Elements elements = doc.select("td");
		for(Element ele : elements){
			if((ele.ownText()).equalsIgnoreCase("Subject")){
				Element e = ele.nextElementSibling();
				fileName = e.text();
			}
			if((ele.ownText()).equalsIgnoreCase("Date")){
				Element e = ele.nextElementSibling();
				fileName = fileName+"_"+e.text();
			}
			fileName = fileName.replaceAll("[:/\\*?\"<>,| ]", "_");
		}
		File parentDir = new File("./2014mails");
		if(!parentDir.exists()){
			boolean isDirectoryCreated = parentDir.mkdir();
			if(!isDirectoryCreated){
				return;
			}
		}
		
		String [] spliturl = url.split("/");
		String monthName = spliturl[5].substring(0, 6);
		
		File monthlyDirName = new File(parentDir.getCanonicalPath()+"/"+monthName);
		if(!monthlyDirName.exists()){
			monthlyDirName.mkdir();
		}
		
		File filePath = new File(monthlyDirName.getCanonicalPath()+"/"+fileName+".txt");
		
	/*	if(!filePath.exists()){
			filePath.createNewFile();
		}*/
		
		FileWriter fw = new FileWriter(filePath.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(doc.text());
		bw.close();

		visitedUrls.add(url);

		}catch(IOException ie){
			ie.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		parseUrls(baseUrl);
		for (String link : unvisitedUrls) {
			if(!visitedUrls.contains(link)){
			downloadData(link);
			}
		}
		
	}
}
