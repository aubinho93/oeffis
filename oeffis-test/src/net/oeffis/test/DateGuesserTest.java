package net.oeffis.test;

import java.util.Calendar;
import java.util.Date;

import net.oeffis.data.DateGuesser;

import android.test.AndroidTestCase;

public class DateGuesserTest extends AndroidTestCase {

	private final static Date OFFSET = createDate(1984, 5, 27, 10, 0);
	
	private Calendar expected;
	private DateGuesser dateGuesser;
	
	@Override
	protected void setUp() throws Exception {
		dateGuesser = new DateGuesser(OFFSET);
		expected = Calendar.getInstance();
		expected.setTime(OFFSET);
	}
	
	public void testEmpty() {
		assertEquals(null, dateGuesser.guessDate(null));
		assertEquals(null, dateGuesser.guessDate(""));
		assertEquals(null, dateGuesser.guessDate(" "));
	}
	
	public void testMinutes() throws Exception {
		testMinutesInternal("0 min", 0); setUp();
		testMinutesInternal("3 min", 3); setUp();
		testMinutesInternal("10 min", 10); setUp();
		testMinutesInternal("11min", 11); setUp();
		testMinutesInternal("in 21 min", 21); setUp();
		testMinutesInternal("9", 9); setUp();
		testMinutesInternal("2 9", 2); setUp();
		testMinutesInternal(" 7 ", 7);
	}
	
	private void testMinutesInternal(String input, int minutes) {
		expected.add(Calendar.MINUTE, minutes);
		assertEquals(expected.getTime(), dateGuesser.guessDate(input));
	}
	
	public void testTime() {
		expected.set(Calendar.HOUR, 10);
		expected.set(Calendar.MINUTE, 19);
		assertEquals(expected.getTime(), dateGuesser.guessDate("10:19"));
	}
	
	public void testTimeNextDay() {
		expected.add(Calendar.HOUR, 24);
		expected.set(Calendar.HOUR, 0);
		expected.set(Calendar.MINUTE, 19);
		assertEquals(expected.getTime(), dateGuesser.guessDate("00:19"));
	}
	
	private static Date createDate(int year, int month, int day, int hour, int minutes) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.YEAR, year);
		now.set(Calendar.MONTH, month);
		now.set(Calendar.DAY_OF_MONTH, day);
		now.set(Calendar.HOUR_OF_DAY, hour);
		now.set(Calendar.MINUTE, minutes);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return now.getTime();
	}
}