package net.oeffis.gui;

import net.oeffis.R;
import net.oeffis.data.db.DbAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Favorites extends StationListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected Cursor fetchData(DbAdapter db) {
		Cursor c = db.fetchFavorites();
		if(c.getCount() < 1) {
			c.close();
			Toast.makeText(this, getString(R.string.nofavorites), 2000).show();
			startActivity(new Intent(this, StationSearch.class));
		}
		return c;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		MenuItem favorites = menu.findItem(R.id.menu_favorites);
		if(favorites != null) {
			favorites.setEnabled(false);
		}
		return ret;
	}
}
