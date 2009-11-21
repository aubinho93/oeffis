package net.oeffis;

import net.oeffis.data.DataClient;
import net.oeffis.data.qando.QandoClient;

import android.content.Context;

public class Settings {

	private Context context;
	
	public Settings(Context context) {
		this.context = context;
	}
	
	public DataClient<?,?> getDataClient() {
		return new QandoClient(context);
	}
	
	public int getUpdateRate() {
		return 15;
	}
}
