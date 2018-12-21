package org.collin.core.def;

import java.util.Collection;

public interface IDataObject<D extends Object> {

	public boolean addDatum( D data );
	
	public Collection<D> getData();
	
	public long getTotalTime();
}
