package net.oeffis.test.qando;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import net.oeffis.Departure;
import net.oeffis.data.DataClientException;
import net.oeffis.data.DataSource;
import net.oeffis.data.qando.QandoClient;

import org.w3c.dom.Document;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class QandoClientTest extends InstrumentationTestCase {

	private QandoClient target;
	
	protected Context getContext() {
		return getInstrumentation().getContext();
	}
	
	protected Context getTargetContext() {
		return getInstrumentation().getTargetContext();
	}
	
	@Override
	protected void setUp() throws Exception {
		target = new QandoClient(getTargetContext(), new DataSource<URL, Document>() {
			public Document load(URL url) {
				return null;
			}
		});
	}
	
	public void testInvalidArguments() {
		try {
			target.getDepartures(null);
			fail("should have thrown IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
		} catch(DataClientException ex) {
			fail("threw ParseException instead of IllegalArgumentException");
		}
		
		try {
			target.getDepartures("");
			fail("should have thrown IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
		} catch(DataClientException ex) {
			fail("threw ParseException instead of IllegalArgumentException");
		}
		
		try {
			target.getDepartures(" ");
			fail("should have thrown IllegalArgumentException");
		} catch(IllegalArgumentException ex) {
		} catch(DataClientException ex) {
			fail("threw ParseException instead of IllegalArgumentException");
		}
	}
	
	public void testNullSource() {
		
		try {
			target = new QandoClient(getTargetContext(), new DataSource<URL, Document>() {
				public Document load(URL url) {
					return null;
				}
			});
			target.getDepartures("Albertgasse");
			fail();
		} catch(DataClientException ex) {
		}
	}
		
	public void testMultipleDepartures() {
		try {
			target = new QandoClient(getTargetContext(),
				new AssetDataSource(getContext(), "fluidtime_response.xml"));
			ArrayList<Departure> departures = new ArrayList<Departure>(target.getDepartures("Albertgasse"));
			assertEquals(8, departures.size());
			
			Collections.sort(departures, new Comparator<Departure>() {
				public int compare(Departure a, Departure b) {
					if(a.getDeparture() == null) {
						return -1;
					}
					if(b.getDeparture() == null) {
						return 1;
					}
					return a.getDeparture().before(b.getDeparture()) ? -1 : 1;
				}
			});
			
			Iterator<Departure> it = departures.iterator();
			
			assertDeparture("O", "Praterstern", null, it.next()); // 2min
			assertDeparture("74A", "Leberstra§e/Sankt Marx", null, it.next()); // 6min
			assertDeparture("O", "Migerkastra§e", null, it.next()); // 10min
			assertDeparture("O", "Praterstern", null, it.next()); // 18min
			assertDeparture("74A", "Leberstra§e/Sankt Marx", null, it.next()); // 21min
			assertDeparture("O", "Migerkastra§e", null, it.next()); // 25min
			assertDeparture("O", "Praterstern", null, it.next()); // 34min
			assertDeparture("O", "Praterstern", null, it.next()); // 49min
			
		} catch(DataClientException ex) {
			fail();
		}
	}
	
	
	
	private void assertDeparture(String line, String destination, Date departure, Departure actual) {
		assertEquals(line, actual.getLine());
		assertEquals(destination, actual.getDestination());
		//assertEquals(departure, actual.getDeparture());
	}
}
