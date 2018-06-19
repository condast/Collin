package org.collin.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.collin.core.authentication.ILoginUser;

public interface IBatch {

	int DEFAULT_RANGE = 10000; //10 seconds

	public enum PlantGrowth{
		CLEAR(0),
		ON_WATER(1),
		UNDERWATER(2);
		
		private int index;
		
		private PlantGrowth( int index ) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}

		public static PlantGrowth[] getPlantGrowth( int pg ) {
			Collection<PlantGrowth> results = new ArrayList<PlantGrowth>();
			for( PlantGrowth p: values()){
				if(( p.getIndex() & pg ) > 0)
					results.add(p);
			}
			return ( results.toArray( new PlantGrowth[ results.size()]));
		}

		public static boolean onWater( int value) {
			int mask = PlantGrowth.ON_WATER.getIndex();
			return ( mask & value) > 0;
		}

		public static boolean underWater( int value ) {
			int mask = PlantGrowth.UNDERWATER.getIndex();
			return ( mask & value ) >0;
		}
		
		public static int setPlantGrowthIndex( boolean plantsOnWater, boolean plantsUnderwater ) {
			int value = 0;
			if( plantsOnWater)
				value += PlantGrowth.ON_WATER.getIndex();
			if( plantsUnderwater)
				value += PlantGrowth.UNDERWATER.getIndex();
			return value;
		}
	}

	long getId();

	String getUserName();

	IMeasurementLocation getLocation();

	IMeasurement[] getMeasurements();

	void addMeasurement(IMeasurement measurement);

	void removeMeasurement(IMeasurement measurement);

	String getDescription();

	void setDescription(String description);

	double getSecchiDepth();

	void setSecchiDepth(double depth);

	double getMudThickness();

	void setMudThickness(double thickness);

	int getPlantGrowth();

	void addPlantGrowth(PlantGrowth plantGrowth);

	void removePlantGrowth(PlantGrowth plantGrowth);

	Date getMeasurementDate();

	void setMeasurementDate(Date measurementDate);

	Date getCreateDate();

	Date getChangeDate();

	int size();

	int getTotalAnimals();

	ILoginUser getLoginUser();

	long getUserId();

}