package net.oeffis.test.qando;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.oeffis.data.DataSource;

import org.w3c.dom.Document;

import android.content.Context;
import android.content.res.AssetManager;

public class AssetDataSource implements DataSource<URL, Document> {

	private Context context;
	private String fileName;
	
	public AssetDataSource(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}
	
	public Document load(URL url) {
		try {
			AssetManager assetManager = context.getAssets();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return docBuilder.parse(assetManager.open(fileName));

		} catch(Exception ex) {
			throw new IllegalArgumentException("could not read test-data-file, probably wrong name");
		}
	}
}
