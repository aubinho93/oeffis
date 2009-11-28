package net.oeffis.gui;

import java.util.Date;
import java.util.List;

import net.oeffis.R;
import net.oeffis.data.Departure;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DepartureAdapter extends ArrayAdapter<Departure> {
	
	public DepartureAdapter(Context context, int textViewResourceId, List<Departure> objects) {
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if(view == null) {
			LayoutInflater layoutInflater = (LayoutInflater)
				getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.departure_listitem, null);
		}
		
		Departure listItem = getItem(position);
		if(listItem != null) {
			TextView line = (TextView) view.findViewById(R.id.line);
			line.setText(listItem.getLine());
			TextView destination = (TextView) view.findViewById(R.id.destination);
			destination.setText(listItem.getDestination());
			TextView departure = (TextView) view.findViewById(R.id.departure);
			departure.setText(prettyTimeUntil(listItem.getDeparture()));
		}
		return view;
	}
	
	private static String prettyTimeUntil(Date date) {
		
		if(date == null) {
			return "?";
		}
		
		Date now = new Date();
		if(date.getTime() - now.getTime() < 30 * 60 * 1000) {
			return String.format("%d min", (date.getTime() - now.getTime()) / (60 * 1000));
		}
		
		return String.format("%02d:%02d", date.getHours(), date.getMinutes());
	}
}
