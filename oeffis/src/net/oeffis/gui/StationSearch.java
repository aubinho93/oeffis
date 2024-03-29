package net.oeffis.gui;

import net.oeffis.R;
import net.oeffis.data.db.DbAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class StationSearch extends StationListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected Cursor fetchData(DbAdapter db) {
		return db.fetchStations();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		MenuItem search = menu.findItem(R.id.menu_search);
		if(search != null) {
			search.setEnabled(false);
		}
		return ret;
	}
}