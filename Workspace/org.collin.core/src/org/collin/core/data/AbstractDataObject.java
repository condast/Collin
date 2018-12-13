package org.collin.core.data;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.def.IDataObject;

public abstract class AbstractDataObject<D extends Object> implements IDataObject<D>{

	private Collection<D> data;
	
	protected AbstractDataObject() {
		data  = new ArrayList<>();
	}

	@Override
	public boolean addDatum(D data) {
		return this.data.add(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public D[] getData() {
		return (D[]) data.toArray();
	}

	
}
