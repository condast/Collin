package org.collin.core.data;

import java.util.Date;

import org.collin.core.model.IMeasurement;
import org.collin.core.model.IWaterAnimal;

public class MeasurementData{

	private long id;

	private int amount;
	
	private IWaterAnimal waterAnimal;

	private Date createDate;
	
	public MeasurementData( IMeasurement measurement) {
		this( measurement.getAmount());
		this.waterAnimal = new WaterAnimalData( measurement.getWaterAnimal());
		this.createDate = measurement.getCreateDate();
	}
	
	protected MeasurementData() {
		this( 0 );
	}
	
	protected MeasurementData( int amount) {
		super();
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public IWaterAnimal getWaterAnimal() {
		return this.waterAnimal;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date create) {
		this.createDate = create;
	}

	public static MeasurementData create( IMeasurement measurement ) {
		return ( measurement == null )? null: new MeasurementData(measurement);
	}
}
