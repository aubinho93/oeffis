package net.oeffis.data.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.oeffis.data.DataClient;
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
	
	public DbAdapter(Context context) {
		this.context = context;
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
	
	public Cursor fetchStations(Class<? extends DataClient> client) {
		return db.rawQuery("select s._id, s.string as station " +
			"from client_string cs " +
				"join clients c on c._id = cs.client " +
				"join strings s on s._id = cs.string " +
			"where c.class = ?", new String[] { client.getName() });
	}
	
	public void close() {
		db.close();
	}
}