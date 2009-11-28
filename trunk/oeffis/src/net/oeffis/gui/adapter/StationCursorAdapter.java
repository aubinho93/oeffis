package net.oeffis.gui.adapter;

import net.oeffis.R;
import net.oeffis.data.Station;
import net.oeffis.data.db.DbAdapter;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class StationCursorAdapter extends SimpleCursorAdapter {
	
	private DbAdapter db;
	
	public StationCursorAdapter(Context context, DbAdapter db, Cursor c) {
		super(context, R.layout.station_listitem, c,
			new String[] { "station" }, new int[] { R.id.station });
		this.db = db;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		final Station station = DbAdapter.fromCursor(cursor);
		final CheckBox favorite = (CheckBox) view.findViewById(R.id.favorite);
		favorite.setChecked(station.isFavorite());
		favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				db.setFavorite(station.getId(), favorite.isChecked());
			}
		});
		view.setTag(station);
	}
}