package net.oeffis.data;

public interface DataSource<Q,R> {
	public R load(Q query);
}
