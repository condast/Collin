package org.collin.core.def;

import java.util.Collection;

import org.condast.commons.strings.StringStyler;

public interface IDataObject<D extends Object> {

	public enum AttributeNames{
		DURATION,
		POLL_TIME;

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}

	public String getValue( String key );
	
	public boolean addDatum( D data );
	
	public Collection<D> getData();
	
	public int getDuration();
}
