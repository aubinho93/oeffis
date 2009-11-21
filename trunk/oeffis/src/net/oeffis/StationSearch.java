package net.oeffis;

import java.util.ArrayList;

import net.oeffis.data.DataClient;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StationSearch extends Activity {

	private DataClient<String, Departure> dataClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stationsearch);
		
		if(dataClient == null) {
			dataClient = (DataClient<String, Departure>) new Settings(this).getDataClient();
		}
		
		ListView stations = (ListView) findViewById(R.id.stations);
		stations.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
			new ArrayList<String>(dataClient.getStations())));
		stations.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("oeffis:" + ((TextView)view).getText()));
				startActivity(intent);
			}
		});
	}
}
