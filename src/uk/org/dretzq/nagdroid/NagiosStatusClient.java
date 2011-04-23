package uk.org.dretzq.nagdroid;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
		} catch (ClientProtocolException e) {
			Log.e("neterror", e.toString());
		} catch (IOException e) {
			Log.e("neterror", e.toString());
		} catch (SAXException e) {			
		} catch (ParserConfigurationException e) {		
		}
		return null;
		
	}
	
	public HostData[] getHosts() {
		Vector<HostData> entries = new Vector<HostData>();
		
		Document doc = getStatus();
		
		if (doc == null) {
			return new HostData[0];
		}
		
		Element el = doc.getDocumentElement();
		NodeList nodelist = el.getChildNodes();
		
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) node;
				
				NodeList servicenodelist = child.getChildNodes();
				Vector<ServiceData> servicedata = new Vector<ServiceData>();
				for (int j = 0; j < servicenodelist.getLength(); j ++) {
					Node servicenode = servicenodelist.item(j);
					if (servicenode.getNodeType() == Node.ELEMENT_NODE) {
						Element servicechild = (Element) servicenode;
						servicedata.add( new ServiceData( 
								servicechild.getAttribute("name"), 
								servicechild.getAttribute("output"), 
								Integer.parseInt(servicechild.getAttribute("state"))								
								));
					}										
				}
				
				entries.add( new HostData(child.getAttribute("name"), servicedata.toArray(new ServiceData[0]) ) );
							
			}
		}
		return entries.toArray(new HostData[0]);
	}
}
