package net.oeffis;

import net.oeffis.data.DataClient;
import net.oeffis.data.qando.QandoClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences {

	private static final String TAG = Preferences.class.getCanonicalName();
	private static final String KEY_UPDATE_RATE = "updaterate";
	private static final String KEY_DATA_CLIENT = "dataclient";
	
	private Context context;
	private SharedPreferences sharedPrefs;
	
	public Preferences(Context context) {
		this.context = context;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public DataClient<?,?> getDataClient() {
		try {
			return getDataClientClass().getConstructor(Context.class).newInstance(context);
		} catch(Exception ex) {
			Log.e(TAG, "could not instantiate dataclient, using default", ex);
			return new QandoClient(context);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends DataClient<?,?>> getDataClientClass() {
		String className = sharedPrefs.getString(KEY_DATA_CLIENT, QandoClient.class.getName());
		try {
			return (Class<? extends DataClient<?,?>>) Class.forName(className);
		} catch(ClassNotFoundException ex) {
			Log.e(TAG, "could not find " + className + ", using default");
			return QandoClient.class;
		}
	}
	
	public int getUpdateRate() {
		try {
			return Integer.parseInt(sharedPrefs.getString(KEY_UPDATE_RATE, "23"));
		} catch(Exception ex) {
			Log.e(TAG, "failed to read updaterate setting, using failsafe");
			return 23;
		}
	}
}
