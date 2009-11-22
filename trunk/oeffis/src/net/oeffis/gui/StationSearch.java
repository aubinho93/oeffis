package net.oeffis.gui;

import java.util.ArrayList;

import net.oeffis.Preferences;
import net.oeffis.R;
import net.oeffis.data.DataClient;
import net.oeffis.data.Departure;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StationSearch extends ActivityBase {

	private DataClient<String, Departure> dataClient;
	private Preferences preferences;
	private ArrayList<String> stations;
	private ArrayAdapter<String> stationAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stationsearch);
		
		stations = new ArrayList<String>();
		stationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stations);
		preferences = new Preferences(this);
		
		ListView stations = (ListView) findViewById(R.id.stations);
		stations.setAdapter(stationAdapter);
		stations.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("oeffis:" + ((TextView)view).getText()));
				startActivity(intent);
			}
		});
	}
	
	private void reloadList() {
		
		if(dataClient != preferences.getDataClient()) {
			dataClient = (DataClient<String, Departure>) preferences.getDataClient();
		}
		
		stations.clear();
		stations.addAll(dataClient.getStations());
		stationAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(dataClient != preferences.getDataClient()) {
			reloadList();
		}
	}
}