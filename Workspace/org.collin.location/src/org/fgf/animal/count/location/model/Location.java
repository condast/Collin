package org.fgf.animal.count.location.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.collin.core.model.IMeasurementLocation;
import org.collin.core.model.IWaterTypes;
import org.condast.commons.data.latlng.ILocation;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.persistence.def.IUpdateable;

@Entity
public class Location implements IMeasurementLocation, IUpdateable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column( nullable=true)
	private String name;
	
	@Column( nullable=true)
	private String description;
	
	private double latitude;

	private double longitude;
	
	@OneToOne
	private WaterTypes waterType;
	
	private double waterQuality;		

	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public Location() {
		super();
	}

	public Location( LatLng latlng, IWaterTypes type ) {
		this();
		this.latitude = latlng.getLatitude();
		this.longitude = latlng.getLongitude();
		this.waterType = (WaterTypes) type;
	}

	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public LatLng getLocation() {
		return new LatLng( this.name, this.latitude, this.longitude );
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getUserName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public IWaterTypes getWaterType() {
		return this.waterType;
	}

	@Override
	public void setWaterType( IWaterTypes waterType) {
		this.waterType = (WaterTypes) waterType;
	}

	@Override
	public double getWaterQualityIndex() {
		return waterQuality;
	}

	@Override
	public void setWaterQualityIndex( double waterQuality) {
		this.waterQuality = waterQuality;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date create) {
		this.createDate = create;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date update) {
		this.updateDate = update; 
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(ILocation arg0) {
		LatLng latlng = getLocation();
		return latlng.compareTo(arg0.getLocation());
	}	
}
