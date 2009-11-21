package net.oeffis.test.itip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.oeffis.data.DataSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.content.Context;
import android.content.res.AssetManager;

public class ITipAssetDataSource implements DataSource<URL, Document> {

	private Context context;
	private String fileName;
	
	public ITipAssetDataSource(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}
	
	public Document load(URL url) {
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader rd = new BufferedReader(new InputStreamReader(assetManager.open(fileName), Charset.forName("ISO-8859-1")));
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
			throw new IllegalArgumentException("could not read test-data-file, probably wrong name");
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
