package net.oeffis.gui;

import net.oeffis.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public abstract class ActivityBase extends Activity {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_search:
			startActivity(new Intent(this, StationSearch.class));
			return true;
		case R.id.menu_favorites:
			startActivity(new Intent(this, Favorites.class));
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
