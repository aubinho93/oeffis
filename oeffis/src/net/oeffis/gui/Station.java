package net.oeffis.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import net.oeffis.Preferences;
import net.oeffis.R;
import net.oeffis.data.DataClient;
import net.oeffis.data.DataClientException;
import net.oeffis.data.Departure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

public class Station extends ActivityBase {

	private static final String TAG = Station.class.getCanonicalName();
	
	private static final int UPDATE_MSG = 1;
	private static final int TITLE_MSG = 2;
	private static final String TITLE_MSG_KEY = "title";
	
	private String station;
	private DepartureAdapter departureAdapter;
	private ArrayList<Departure> departures = new ArrayList<Departure>();
	private Preferences preferences;
	private Timer timer;
	
	private int seconds;
	
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
	
	private class Countdown extends TimerTask {
		@Override
		public void run() {
			if(--seconds < 1) {
				update();
				seconds = preferences.getUpdateRate();
			} else {
				sendTitleMessage(handler, "Updating in " + seconds + "s");
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		station = getIntent().getDataString().substring("oeffis:".length());
		setContentView(R.layout.station);
		
		preferences = new Preferences(this);
		
		departureAdapter = new DepartureAdapter(this, R.layout.departure_listitem, departures);
		ListView departureView = (ListView) findViewById(R.id.departures);
		departureView.setAdapter(departureAdapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		timer.cancel();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		seconds = 0;
		timer = new Timer(true);
		timer.schedule(new Countdown(), 0, 1000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}
	
	private void update() {
		sendTitleMessage(handler, "Updating..");
		try {
			DataClient<String, Departure> dataClient = (DataClient<String, Departure>) preferences.getDataClient();
			ArrayList<Departure> res = new ArrayList<Departure>(dataClient.getDepartures(station));
			Collections.sort(res, Departure.SORTBY_TIME);
			departures.clear();
			departures.addAll(res);
			handler.sendEmptyMessage(UPDATE_MSG);
		} catch(DataClientException ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
	}
	
	private static void sendTitleMessage(Handler handler, String title) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		msg.what = TITLE_MSG;
		bundle.putString(TITLE_MSG_KEY, title);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
}
