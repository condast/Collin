package org.collin.core.data;

import java.util.HashMap;
import java.util.Map;

public class BatchEntry {
	
	private double secchi_depth;
	
	private double mudthickness;
	
	private boolean plantonwater;
	private boolean plantunderwater;

	private String description;
	
	private Map<Integer, Long> measurements;

	public BatchEntry() {
		super();
	}

	public BatchEntry( double secchi_depth, double mudthickness, boolean plantonwater, boolean plantunderwater,String description) {
		this( new HashMap<Integer, Long>(), secchi_depth, mudthickness, plantonwater, plantunderwater, description );
	}
	
	public BatchEntry( Map<Integer, Long> measurements, double secchi_depth, double mudthickness, boolean plantonwater, boolean plantunderwater,
			String description) {
		super();
		this.measurements = measurements;
		this.secchi_depth = secchi_depth;
		this.mudthickness = mudthickness;
		this.plantonwater = plantonwater;
		this.plantunderwater = plantunderwater;
		this.description = description;
	}

	public void addMeasurement( int morphocode, long amount ) {
		this.measurements.put( morphocode, amount);
	}
	
	
	public Map<Integer, Long> getMeasurements() {
		return measurements;
	}

	public double getSecchiDepth() {
		return secchi_depth;
	}

	public double getMudthickness() {
		return mudthickness;
	}

	public void setMudthickness(double mudthickness) {
		this.mudthickness = mudthickness;
	}

	public boolean isPlantOnWater() {
		return plantonwater;
	}

	public boolean isPlantUnderwater() {
		return plantunderwater;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
