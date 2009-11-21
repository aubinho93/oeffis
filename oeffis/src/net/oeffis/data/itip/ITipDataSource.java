package net.oeffis.data.itip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.oeffis.data.DataSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.util.Log;

class ITipDataSource implements DataSource<URL, Document> {
	
	private static final String TAG = ITipDataSource.class.getCanonicalName();

	public Document load(URL url) {
		try {
			URLConnection connection = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), Charset.forName("ISO-8859-1")));
			StringBuffer sb = new StringBuffer();
			String l;
			while((l = rd.readLine()) != null) {
				sb.append(l);
			}
			rd.close();

			String html = sanitizeDeparturesMarkup(sb.toString());
			
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return docBuilder.parse(new InputSource(new StringReader(html)));
			
		} catch(Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
			return null;
		}
	}
	
	private static String sanitizeDeparturesMarkup(String html) {
		
		if(html.contains("Keine Abfahrten gefunden.")) {
			return "<table/>";
		}
		
		int tableStart = html.indexOf("<table width=\"95%\" border=\"0\" cellspacing=\"2\" align=\"center\" cellpadding=\"0\">");
		return html.substring(tableStart, html.indexOf("</table>", tableStart) + 8)
			.replaceAll("<br>", "")
			.replaceAll("<img([^>]+)>", "<img\1/>");
	}
}
