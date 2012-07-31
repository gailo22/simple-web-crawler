package com.gailo22;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		// URI uri = buildUriForGoogle();
		URI uri = buildUriForReddit();
		HttpGet httpget = new HttpGet(uri);
		System.out.println(httpget.getURI());
		HttpResponse response = httpclient.execute(httpget);
		printResponse(response);

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setXmlOut(true);
		tidy.setForceOutput(true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				// do something useful
				Document document = tidy.parseDOM(instream, null);
				
//				System.out.println(document.getElementsByTagName("body"));

				XPath xpath = XPathFactory.newInstance().newXPath();
				// XPath Query for showing all nodes value
				XPathExpression expr = xpath.compile("/html/body");

				Object result = expr.evaluate(document, XPathConstants.NODESET);

				System.out.println("------------------------------");
				NodeList nodes = (NodeList) result;
				for (int i = 0; i < nodes.getLength(); i++) {
					System.out.println(nodes.item(i).getNodeValue());
				}

				// entity = new BufferedHttpEntity(entity);
				// System.out.println(EntityUtils.toString(entity));
			} finally {
				instream.close();
			}
		}

	}

	private static URI buildUriForGoogle() throws URISyntaxException {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.google.com").setPath("/search")
				.setParameter("q", "httpclient")
				.setParameter("btnG", "Google Search").setParameter("aq", "f")
				.setParameter("oq", "");
		URI uri = builder.build();
		return uri;
	}

	private static URI buildUriForReddit() throws URISyntaxException {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.reddit.com")
				.setPath("/r/programming");
		URI uri = builder.build();
		return uri;
	}

	private static void printResponse(HttpResponse response) {
		System.out.println(response.getProtocolVersion());
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getStatusLine().getReasonPhrase());
		System.out.println(response.getStatusLine().toString());
	}
}
