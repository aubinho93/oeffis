package net.oeffis.data;

public class Station {

	private long id;
	private String name;
	private boolean favorite;
	
	public Station(long id, String name, boolean favorite) {
		super();
		this.id = id;
		this.name = name;
		this.favorite = favorite;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isFavorite() {
		return favorite;
	}
}
