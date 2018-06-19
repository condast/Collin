package org.collin.core.view;

import java.util.Date;

import org.collin.core.data.BatchData;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.model.IWaterTypes;

@SuppressWarnings("unused")
public class LocationView {

	private long id;

	private String name;

	private String userName;
	private String description;
	
	private double latitude;

	private double longitude;

	private IWaterTypes waterType;
	
	private double waterQuality;		

	private Date createDate;

	private Date updateDate;
	
	private Sum sum;

	public LocationView() {
		super();
	}

	public LocationView( BatchData batch ) {
		this();
		IMeasurementLocation location = batch.getLocation();
		this.id = location.getId();
		this.userName = batch.getUserName();
		this.name = location.getName();
		this.description = location.getDescription();
		this.latitude = location.getLocation().getLatitude();
		this.longitude = location.getLocation().getLongitude();
		this.waterType = location.getWaterType();
		this.waterQuality = location.getWaterQualityIndex();
		this.createDate = location.getCreateDate();
		this.updateDate = location.getUpdateDate();
		this.sum = new Sum( batch );
	}

	
	public class Sum{
		
		private int measurements;
		private int animals;
		
		public Sum( BatchData batch ) {
			super();
			this.measurements = batch.getMeasurements().length;
			this.animals = batch.getTotalAnimals();
		}
	}
}
