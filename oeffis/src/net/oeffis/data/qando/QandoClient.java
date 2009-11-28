package net.oeffis.data.qando;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.oeffis.R;
import net.oeffis.data.DataClient;
import net.oeffis.data.DataClientException;
import net.oeffis.data.DataSource;
import net.oeffis.data.DateGuesser;
import net.oeffis.data.Departure;
import net.oeffis.data.XmlHttpDataSource;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

public class QandoClient implements DataClient<String, Departure> {

	private static final String TAG = QandoClient.class.getCanonicalName();
	
	private Context context;
	private DataSource<URL, Document> dataSource; 
	
	public QandoClient(Context context) {
		this(context, new XmlHttpDataSource());
	}
	
	public QandoClient(Context context, DataSource<URL, Document> dataSource) {
		this.context = context;
		this.dataSource = dataSource;
	}
	
	public Collection<Departure> getDepartures(String station) throws DataClientException {
		if(station == null || station.trim().equals("")) {
			throw new IllegalArgumentException();
		}
		
		try {
			Set<Departure> departures = new HashSet<Departure>();
			Document dom = dataSource.load(getUrl(station));
			
			if(dom == null) {
				throw new Exception("no data");
			}
			
			DateGuesser dateGuesser = new DateGuesser();
			NodeList departureNodes = dom.getElementsByTagName("departure");
			for(int i = 0; i < departureNodes.getLength(); i++) {
				String line = departureNodes.item(i).getAttributes().getNamedItem("line").getNodeValue();
				NodeList durationNodes = departureNodes.item(i).getChildNodes();
				for(int j = 0; j < durationNodes.getLength(); j++) {
					departures.add(new Departure(line,
						durationNodes.item(j).getAttributes().getNamedItem("towardssingle").getNodeValue(),
						dateGuesser.guessDate(durationNodes.item(j).getAttributes().getNamedItem("minutes").getNodeValue())));
				}
			}
			
			return departures;
			
		} catch(Exception ex) {
			throw new DataClientException(ex.getMessage(), ex);
		}
	}
	
	private URL getUrl(String station) {
		try {
			String url = context.getString(R.string.qando_url);
			String enc = context.getString(R.string.qando_enc);
			return new URL(String.format(url, URLEncoder.encode(station, enc)));
		} catch(UnsupportedEncodingException ex) {
			Log.e(TAG, "encoding not found or invalid");
		} catch(MalformedURLException ex) {
			Log.e(TAG, "url format or encoding not found or invalid");
		}
		return null;
	}
}