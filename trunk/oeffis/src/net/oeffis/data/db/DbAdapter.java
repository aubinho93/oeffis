package net.oeffis.data.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.oeffis.Preferences;
import net.oeffis.data.Station;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbAdapter {
	
	private static final String TAG = DbAdapter.class.getCanonicalName();
	
	private static final String DB_NAME = "oeffis";
	
	private Context context;
	private SQLiteDatabase db;
	private Preferences preferences;
	
	public DbAdapter(Context context) {
		this.context = context;
		preferences = new Preferences(context);
	}
	
	public void open() throws SQLException {
		
		File dbFile = context.getDatabasePath(DB_NAME);
		if(!dbFile.exists()) {
			try {
				copyFile(context.getAssets().open("oeffis.db"), dbFile);
			} catch(IOException ex) {
				Log.e(TAG, "could not copy database", ex);
			}
		}
		
		db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	private static void copyFile(InputStream in, File dst) throws IOException {
		if(!dst.exists()) {
			dst.getParentFile().mkdirs();
			dst.createNewFile();
		}
		
		FileOutputStream out = new FileOutputStream(dst);
		try {
			byte[] buf = new byte[4096];
			int len;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} finally {
			in.close();
			out.close();
		}
	}
	
	public Station fetchStation(long id) {
		Station station = null;
		Cursor c = db.rawQuery("select " +
				"_id," +
				"name," +
				"favorite " +
			"from stations " +
			"where _id = " + id, null);
		if(c.moveToNext()) {
			station = fromCursor(c);
		}
		
		c.close();
		
		return station;
	}
	
	public String fetchClientStringForStation(Class<?> client, long station) {
		String string = null;
		Cursor c = db.rawQuery("select str.string " +
			"from station_client_string scs " +
				"join clients cli on cli._id = scs.client " +
				"join strings str on str._id = scs.string " +
			"where cli.class = ? and scs.station = " + station,
			new String[] { client.getName() });
		if(c.moveToNext()) {
			string = c.getString(0);
		}
		c.close();
		return string;
	}
	
	public Cursor fetchStations() {
		return fetchStations(preferences.getDataClientClass());
	}
	
	public Cursor fetchStations(Class<?> client) {
		return db.rawQuery("select " +
				"sta._id," +
				"sta.name as station," +
				"sta.favorite as favorite " +
			"from station_client_string scs " +
				"join stations sta on sta._id = scs.station " +
				"join clients cli on cli._id = scs.client " +
			"where cli.class = ?", new String[] { client.getName() });
	}
	
	public Cursor fetchFavorites() {
		return fetchFavorites(preferences.getDataClientClass());
	}
	
	public Cursor fetchFavorites(Class<?> client) {
		return db.rawQuery("select " +
				"sta._id," +
				"sta.name as station," +
				"sta.favorite as favorite " +
			"from station_client_string scs " +
				"join stations sta on sta._id = scs.station " +
				"join clients cli on cli._id = scs.client " +
			"where " +
				"cli.class = ? " +
				"and favorite = 1", new String[] { client.getName() });
	}
	
	public void setFavorite(long stationId, boolean isFavorite) {
		db.execSQL("update stations set favorite = ? where _id = ?",
			new Object[] { isFavorite ? 1 : 0, stationId });
	}
	
	public void close() {
		db.close();
	}
	
	public static Station fromCursor(Cursor c) {
		return new Station(c.getLong(0), c.getString(1), c.getInt(2) > 0);
	}
}