package net.oeffis;

import java.util.Date;

public class Departure {
	
	private String line;
	private String destination;
	private Date departure;
	
	public Departure(String line, String destination, Date departure) {
		this.line = line;
		this.destination = destination;
		this.departure = departure;
	}

	public String getLine() {
		return line;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public Date getDeparture() {
		return departure;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((departure == null) ? 0 : departure.hashCode());
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Departure other = (Departure) obj;
		if (departure == null) {
			if (other.departure != null)
				return false;
		} else if (!departure.equals(other.departure))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s", line, destination, departure);
	}
}
