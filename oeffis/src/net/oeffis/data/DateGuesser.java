package net.oeffis.data;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateGuesser {

	private static final Pattern MINUTES = Pattern.compile(".*?(\\d+).*");
	private static final Pattern TIME = Pattern.compile(".*?(\\d{1,2}):(\\d{2}).*");
	
	private Date offset;
	
	public DateGuesser() {
		this(new Date());
	}
	
	public DateGuesser(Date offset) {
		this.offset = offset;
	}
	
	public Date guessDate(String input) {
		
		if(input == null || input.trim().length() < 1) {
			return null;
		}
		
		Calendar now = Calendar.getInstance();
		now.setTime(offset);
		
		Matcher m = TIME.matcher(input);
		if(m.matches()) {
			now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group(1)));
			now.set(Calendar.MINUTE, Integer.parseInt(m.group(2)));
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			
			if(now.getTime().before(offset)) {
				now.add(Calendar.HOUR, 24);
			}
			
			return now.getTime();
		}
		
		m = MINUTES.matcher(input);
		if(m.matches()) {
			now.add(Calendar.MINUTE, Integer.parseInt(m.group(1)));
			return now.getTime();
		}
		
		return null;
	}
}
