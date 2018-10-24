package org.collin.core.xml;

import java.util.EventObject;

public class SequenceEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private String id;
	
	private int index;
	private int offset;
	private String url;

	public SequenceEvent(Object source, String id, String url, int index ) {
		this( source, id, url, index, 0 );
	}

	public SequenceEvent(Object source, String id, String url, int index, int offset ) {
		super(source);
		this.id = id;
		this.index = index;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public int getIndex() {
		return index;
	}

	public int getOffset() {
		return offset;
	}
}
