package net.oeffis.data.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqlScriptExecutor {
	
	public static void execSql(SQLiteDatabase db, InputStream script) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(script));
		StringBuilder sb = new StringBuilder();
		String line;
		
		while((line = rd.readLine()) != null) {
			// poor man's script execution (can't handle literal and comment ';'s..)
			if(line.contains(";")) {
				sb.append(line.substring(0, line.indexOf(';') + 1));
				Log.i("x", "sql: " + sb.toString());
				db.execSQL(sb.toString());
				sb = new StringBuilder();
			} else {
				sb.append(line);
			}
		}
		
		rd.close();
	}
}
