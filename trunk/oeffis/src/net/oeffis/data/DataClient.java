package net.oeffis.data;

import java.util.Collection;

public interface DataClient<S,D> {
	public Collection<D> getDepartures(S station) throws DataClientException;
}
