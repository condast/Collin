package org.collin.core.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.collin.core.authentication.ILoginUser;
import org.collin.core.data.BatchData;
import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.model.IWaterTypes;
import org.condast.commons.Utils;
import org.condast.commons.data.latlng.ILocation;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class LocationMeasurements {

	private long locationId;
	private Date startDate;
	private Date changeDate;
	private String userName;
	
	private double latitude, longitude;
	
	private IWaterTypes.TypeOfWater watertype;
	
	private double quality;
	
	private Collection<BatchData> batches;
	
	private Sum sum;
	
	public LocationMeasurements( ILoginUser user, ILocation location, Collection<BatchData> batches ) {
		super();
		this.userName = user.getUserName();
		this.locationId = location.getId();
		this.latitude = location.getLocation().getLatitude();
		this.longitude = location.getLocation().getLongitude();
		this.batches = batches;
		this.quality = 0;
		if( Utils.assertNull(this.batches))
			return;
		for( BatchData batch: batches ) {
			this.quality += batch.getAverageQuality(); 
		}
		this.quality /= this.batches.size();
		this.sum = new Sum( this.batches );
	}
	
	public String toJson() {
		Gson gson = new Gson();
		String result = gson.toJson( this, LocationMeasurements.class );
		return result;
	}

	private class Sum{
		
		private int measurements;
		private int animals;
		private int species;

		public Sum( Collection<BatchData> batches ) {
			if( Utils.assertNull( batches))
				return;
			for( BatchData batch: batches ) {
				this.animals += batch.getTotalAnimals();
				this.species = batch.getTotalSpecies();
				this.measurements += batch.getMeasurements().length;
			}
		}

		public Sum(int measurements, int animals, int species) {
			super();
			this.measurements = measurements;
			this.animals = animals;
			this.species = species;
		}
	}	
}
