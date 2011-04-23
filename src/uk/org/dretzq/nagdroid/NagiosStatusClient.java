package uk.org.dretzq.nagdroid;

import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class NagiosStatusClient {
	private String url = "";
	private String username = "";
	private String password = "";
	
	public NagiosStatusClient(String pUrl, String pUsername, String pPassword) {
		url = pUrl;
		username = pUsername;
		password = pPassword;
	}
	
	public NagiosStatusClient(String pUrl) {
		url = pUrl;
	}
	
	public NagiosStatusClient() {
				
	}
	
	public Document getStatus() {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			if (username != null && password != null) {
							
				client.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials(username, password)
				);
			}
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Log.i("network", "Network transfer completed.");
			
			Document doc = builder.parse(response.getEntity().getContent());
			return doc;
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public String[] getHosts() {
		Vector<String> entries = new Vector<String>();
		
		Document doc = getStatus();
		
		Element el = doc.getDocumentElement();
		NodeList nodelist = el.getChildNodes();
		
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) node;
				entries.add( child.getAttribute("name") );
			}
		}
		return entries.toArray(new String[0]);
	}
}
