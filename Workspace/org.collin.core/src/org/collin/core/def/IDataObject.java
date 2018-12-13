package org.collin.core.def;

public interface IDataObject<D extends Object> {

	public boolean addDatum( D data );
	
	public D[] getData();
	
	public long getTotalTime();
}
