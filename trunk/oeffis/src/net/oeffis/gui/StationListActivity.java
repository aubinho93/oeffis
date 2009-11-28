package net.oeffis.gui;

import net.oeffis.data.Station;
import net.oeffis.data.db.DbAdapter;
import net.oeffis.gui.adapter.StationCursorAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public abstract class StationListActivity extends ActivityBase {

	private DbAdapter db;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		listView = new ListView(this);
		listView.setTextFilterEnabled(true);
		listView.setFastScrollEnabled(true);
		
		addContentView(listView, new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Station station = (Station) view.getTag();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("oeffis://station/" + station.getId()));
				startActivity(intent);
			}
		});
		
		db = new DbAdapter(this);
		db.open();
		fillData();
	}
	
	private void fillData() {
		Cursor cursor = fetchData(db);
		startManagingCursor(cursor);
		listView.setAdapter(new StationCursorAdapter(this, db, cursor));
	}
	
	protected abstract Cursor fetchData(DbAdapter db);
	
	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}
}
