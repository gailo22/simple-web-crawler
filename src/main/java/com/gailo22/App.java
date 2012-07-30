package com.gailo22;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.tidy.Tidy;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.google.com").setPath("/search")
				.setParameter("q", "httpclient")
				.setParameter("btnG", "Google Search").setParameter("aq", "f")
				.setParameter("oq", "");
		URI uri = builder.build();
		HttpGet httpget = new HttpGet(uri);
		System.out.println(httpget.getURI());
		HttpResponse response = httpclient.execute(httpget);
		printResponse(response);
		
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setXmlOut(true);
		tidy.setForceOutput(true);
		
		HttpEntity entity = response.getEntity();
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    try {
		        // do something useful
		    	tidy.parseDOM(instream, System.out);
//		    	entity = new BufferedHttpEntity(entity);
//		    	System.out.println(EntityUtils.toString(entity));
		    } finally {
		        instream.close();
		    }
		}
		
	}

	private static void printResponse(HttpResponse response) {
		System.out.println(response.getProtocolVersion());
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getStatusLine().getReasonPhrase());
		System.out.println(response.getStatusLine().toString());
	}
}
