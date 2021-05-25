package org.collin.core.data;

import java.util.Date;

import org.condast.commons.IUpdateable;
import org.condast.commons.data.latlng.ILocation;

@SuppressWarnings("unused")
public class LocationData {

	private long locationId;
	private String name, description;;
	private double latitude, longitude;
	
	private Date createDate;
	
	public LocationData( ILocation location ) {
		this.locationId = location.getId();
		this.name = location.getLocation().getId();
		this.description = location.getLocation().getDescription();
		this.latitude = location.getLocation().getLatitude();
		this.longitude = location.getLocation().getLongitude();
		if( location instanceof IUpdateable ) {
			IUpdateable u = (IUpdateable) location;
			this.createDate = u.getCreateDate();
		}
	}

}
