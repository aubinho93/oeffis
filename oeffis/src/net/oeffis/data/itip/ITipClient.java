package net.oeffis.data.itip;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import net.oeffis.R;
import net.oeffis.data.DataClient;
import net.oeffis.data.DataClientException;
import net.oeffis.data.DataSource;
import net.oeffis.data.DateGuesser;
import net.oeffis.data.Departure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import android.content.Context;
import android.util.Log;

public class ITipClient implements DataClient<String, Departure> {
	
	private static final String TAG = ITipClient.class.getCanonicalName();
	
	private Context context;
	private DataSource<URL, Document> dataSource; 
	
	public ITipClient(Context context) {
		this(context, new ITipDataSource());
	}
	
	public ITipClient(Context context, DataSource<URL, Document> dataSource) {
		this.context = context;
		this.dataSource = dataSource;
	}

	public Collection<Departure> getDepartures(String station) throws DataClientException {
		
		if(station == null || station.trim().equals("")) {
			throw new IllegalArgumentException();
		}
		
		try {
			ArrayList<Departure> departures = new ArrayList<Departure>();
			Document dom = dataSource.load(getUrl(station));
			
			if(dom == null) {
				throw new Exception("no data");
			}
			
			DateGuesser dateGuesser = new DateGuesser();
			NodeList rows = dom.getElementsByTagName("tr");
			for(int i = 1; i < rows.getLength(); i++) {
				NodeList fields = rows.item(i).getChildNodes();
				departures.add(new Departure(
					getInnerText(fields.item(0)),
					getInnerText(fields.item(1)),
					dateGuesser.guessDate((getInnerText(fields.item(2)))
				)));
			}
			
			return departures;
			
		} catch(Exception ex) {
			throw new DataClientException(ex.getMessage(), ex);
		}
	}
	
	private URL getUrl(String station) {
		try {
			String url = context.getString(R.string.itip_url);
			String enc = context.getString(R.string.itip_enc);
			return new URL(String.format(url, URLEncoder.encode(station, enc)));
		} catch(UnsupportedEncodingException ex) {
			Log.e(TAG, "encoding not found or invalid");
		} catch(MalformedURLException ex) {
			Log.e(TAG, "url format or encoding not found or invalid");
		}
		return null;
	}
	
	private static String getInnerText(Node node) {
		
		StringBuffer sb = new StringBuffer();
		NodeList children = node.getChildNodes();
		
		if(children == null || children.getLength() == 0) {
			if(node instanceof Text) {
				return ((Text)node).getData();
			}
		}
		
		for(int i = 0; i < children.getLength(); i++) {
			sb.append(getInnerText(children.item(i)));
		}
		return sb.toString();
	}
}
