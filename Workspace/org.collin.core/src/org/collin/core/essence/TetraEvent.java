package org.collin.core.essence;

import java.util.EventObject;

public class TetraEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private D data;
	
	private int selected;
	
	public TetraEvent(Object source, D data, int selected ) {
		super(source);
		this.data = data;
		this.selected = selected;
	}

	public D getData() {
		return data;
	}

	public int getSelected() {
		return selected;
	}
}
