package net.oeffis.gui;

import java.util.ArrayList;

import net.oeffis.Preferences;
import net.oeffis.R;
import net.oeffis.data.DataClient;
import net.oeffis.data.Departure;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StationSearch extends Activity {

	private DataClient<String, Departure> dataClient;
	private Preferences preferences;
	private ArrayList<String> stations;
	private ArrayAdapter<String> stationAdapter;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			stationAdapter.notifyDataSetChanged();
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stationsearch);
		
		stations = new ArrayList<String>();
		stationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stations);
		
		preferences = new Preferences(this);
		dataClient = (DataClient<String, Departure>) preferences.getDataClient();
		
		ListView stations = (ListView) findViewById(R.id.stations);
		stations.setAdapter(stationAdapter);
		stations.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("oeffis:" + ((TextView)view).getText()));
				startActivity(intent);
			}
		});
		
		reloadList();
	}
	
	private void reloadList() {
		stations.clear();
		stations.addAll(dataClient.getStations());
		handler.sendEmptyMessage(0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		preferences = new Preferences(this);
		if(dataClient != preferences.getDataClient()) {
			reloadList();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}