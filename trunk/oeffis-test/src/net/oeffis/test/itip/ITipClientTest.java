package net.oeffis.test.itip;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import net.oeffis.data.DataClientException;
import net.oeffis.data.DataSource;
import net.oeffis.data.Departure;
import net.oeffis.data.itip.ITipClient;

import org.w3c.dom.Document;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class ITipClientTest extends InstrumentationTestCase {

	private ITipClient target;
	
	protected Context getContext() {
		return getInstrumentation().getContext();
	}
	
	protected Context getTargetContext() {
		return getInstrumentation().getTargetContext();
	}
	
	@Override
	protected void setUp() throws Exception {
		target = new ITipClient(getTargetContext(), new DataSource<URL, Document>() {
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
			target = new ITipClient(getTargetContext(), new DataSource<URL, Document>() {
				public Document load(URL url) {
					return null;
				}
			});
			target.getDepartures("Albertgasse");
			fail();
		} catch(DataClientException ex) {
		}
	}
		
	public void testSingleDeparture() {
		try {
			target = new ITipClient(getTargetContext(),
				new ITipAssetDataSource(getContext(), "single_departure.html"));
			Collection<Departure> departures = target.getDepartures("Albertgasse");
			assertEquals(1, departures.size());
			assertDeparture("5", "Praterstern SU", "in 6 min", departures.iterator().next());
		} catch(DataClientException ex) {
			fail();
		}
	}
	
	public void testMultipleDepartures() {
		try {
			target = new ITipClient(getTargetContext(),
				new ITipAssetDataSource(getContext(), "multiple_departures.html"));
			Collection<Departure> departures = target.getDepartures("Albertgasse");
			assertEquals(5, departures.size());
			Iterator<Departure> it = departures.iterator();
			assertDeparture("2", "Friedrich-Engels-Platz", "in 2 min", it.next());
			assertDeparture("5", "Praterstern SU", "21:49", it.next());
			assertDeparture("2", "Friedrich-Engels-Platz", "in 12 min", it.next());
			assertDeparture("5", "Praterstern SU", "in 18 min", it.next());
			assertDeparture("2", "Friedrich-Engels-Platz", "in 23 min", it.next());
		} catch(DataClientException ex) {
			fail();
		}
	}
	
	public void testSoonDeparture() {
		try {
			target = new ITipClient(getTargetContext(),
				new ITipAssetDataSource(getContext(), "soon_departure.html"));
			Collection<Departure> departures = target.getDepartures("Albertgasse");
			assertEquals(1, departures.size());
			assertDeparture("2", "Friedrich-Engels-Platz", "soon", departures.iterator().next());
		} catch(DataClientException ex) {
			fail();
		}
	}
	
	public void testNoDepartures() {
		try {
			target = new ITipClient(getTargetContext(),
				new ITipAssetDataSource(getContext(), "no_departures.html"));
			Collection<Departure> departures = target.getDepartures("Albertgasse");
			assertEquals(0, departures.size());
		} catch(DataClientException ex) {
			fail();
		}
	}
	
	private void assertDeparture(String line, String destination, String departure, Departure actual) {
		assertEquals(line, actual.getLine());
		assertEquals(destination, actual.getDestination());
		//assertEquals(departure, actual.getDeparture());
	}
}