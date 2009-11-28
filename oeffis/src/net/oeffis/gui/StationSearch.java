package net.oeffis.gui;

import net.oeffis.Preferences;
import net.oeffis.R;
import net.oeffis.data.DataClient;
import net.oeffis.data.db.DbAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StationSearch extends ActivityBase {

	private DataClient<?,?> dataClient;
	private Preferences preferences;
	private DbAdapter db;
	private ListView stations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stationsearch);
		
		preferences = new Preferences(this);
		db = new DbAdapter(this);
		db.open();
		
		stations = (ListView) findViewById(R.id.stations);
		stations.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView station = (TextView) view.findViewById(R.id.station);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("oeffis:" + station.getText()));
				startActivity(intent);
			}
		});
		
		reloadList();
	}
	
	private void reloadList() {
		dataClient = preferences.getDataClient();
		Cursor c = db.fetchStations(dataClient.getClass());
		startManagingCursor(c);
		stations.setAdapter(new SimpleCursorAdapter(this, R.layout.station_listitem, c,
			new String[] { "station"}, new int[] { R.id.station }));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(dataClient != preferences.getDataClient()) {
			reloadList();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	class StationAdapter extends SimpleCursorAdapter {
		
		private Context context;
		
		public StationAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(view == null) {
				LayoutInflater layoutInflater = (LayoutInflater)
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(R.layout.station_listitem, null);
			}
			
			String listItem = getItem(position).toString();
			if(listItem != null) {
				TextView station = (TextView) view.findViewById(R.id.station);
				station.setText(listItem);
			}
			return view;
		}
	}
}