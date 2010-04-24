package net.oeffis.gui.adapter;

import net.oeffis.R;
import net.oeffis.data.Station;
import net.oeffis.data.db.DbAdapter;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

public class StationCursorAdapter extends SimpleCursorAdapter implements OnClickListener {
	
	private DbAdapter db;
	private Cursor cursor;
	
	public StationCursorAdapter(Context context, DbAdapter db, Cursor c) {
		super(context, R.layout.station_listitem, c,
			new String[] { "station", "favorite" }, new int[] { R.id.station, R.id.favorite });
		
		this.db = db;
		this.cursor = c;
		
		this.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(columnIndex == 2) {
					CheckBox cb = (CheckBox) view;
					cb.setChecked(cursor.getInt(2) > 0);
					return true;
				}	
				return false;
			}
		});	
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		Station station = DbAdapter.fromCursor(cursor);
		view.setTag(station);
		
		CheckBox cb = (CheckBox) view.findViewById(R.id.favorite);
		cb.setTag(station);
		cb.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		CheckBox checkbox = (CheckBox) v;
		Station s = (Station) v.getTag();
		db.setFavorite(s.getId(), checkbox.isChecked());
		cursor.requery();
	}
}