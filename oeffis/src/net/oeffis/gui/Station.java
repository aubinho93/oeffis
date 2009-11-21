package net.oeffis.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import net.oeffis.R;
import net.oeffis.Preferences;
import net.oeffis.data.DataClient;
import net.oeffis.data.DataClientException;
import net.oeffis.data.Departure;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

public class Station extends Activity {

	private static final String TAG = Station.class.getCanonicalName();
	
	private static final int UPDATE_MSG = 1;
	private static final int TITLE_MSG = 2;
	private static final String TITLE_MSG_KEY = "title";
	
	private Timer timer = new Timer(true);
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case TITLE_MSG:
				setTitle(String.format("%s (%s)", station, msg.getData().getString(TITLE_MSG_KEY)));
				break;
			case UPDATE_MSG:
				departureAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	
	private String station;
	private DepartureAdapter departureAdapter;
	private ArrayList<Departure> departures = new ArrayList<Departure>();
	private DataClient<String, Departure> dataClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		station = getIntent().getDataString().substring("oeffis:".length());
		setContentView(R.layout.station);
		
		final Preferences settings = new Preferences(this);
		
		if(dataClient == null) {
			dataClient = (DataClient<String, Departure>) settings.getDataClient();
		}
		
		departureAdapter = new DepartureAdapter(this, R.layout.departure_listitem, departures);
		ListView departureView = (ListView) findViewById(R.id.departures);
		departureView.setAdapter(departureAdapter);
		
		timer.schedule(new TimerTask() {
			
			private int seconds;
			
			@Override
			public void run() {
				
				Message msg = new Message();
				msg.what = TITLE_MSG;
				Bundle bundle = new Bundle();
				msg.setData(bundle);
				
				if(--seconds < 1) {
					bundle.putString(TITLE_MSG_KEY, "Updating..");
					try {
						handler.sendMessage(msg);
						ArrayList<Departure> res = new ArrayList<Departure>(dataClient.getDepartures(station));
						Collections.sort(res, new Comparator<Departure>() {
							public int compare(Departure a, Departure b) {
								if(a.getDeparture() == null) {
									return -1;
								}
								if(b.getDeparture() == null) {
									return 1;
								}
								return a.getDeparture().before(b.getDeparture()) ? -1 : 1;
							}
						});
						departures.clear();
						departures.addAll(res);
						handler.sendEmptyMessage(UPDATE_MSG);
					} catch(DataClientException ex) {
						Log.e(TAG, ex.getMessage(), ex);
					}
					seconds = settings.getUpdateRate();
				} else {
					bundle.putString(TITLE_MSG_KEY, "Updating in " + seconds + "s");
					handler.sendMessage(msg);
				}
			}
		}, 0, 1000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}
}
