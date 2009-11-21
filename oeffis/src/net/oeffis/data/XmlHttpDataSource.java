package net.oeffis.data;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.util.Log;

public class XmlHttpDataSource implements DataSource<URL, Document> {

	private static final String TAG = XmlHttpDataSource.class.getCanonicalName();
	
	public Document load(URL url) {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			URLConnection connection = url.openConnection();
			return docBuilder.parse(connection.getInputStream());
		} catch(Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
			return null;
		}
	}
}
