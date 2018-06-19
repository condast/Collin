package org.collin.core.view;

import java.util.ArrayList;
import java.util.Collection;

import org.collin.core.data.BatchData;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.view.LocationView.Sum;
import org.condast.commons.data.latlng.ILocation;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class LocationsView<M extends IMeasurement> {

	private LocationView[] locations;
	
	private Sum sum;

	public LocationsView( Collection<BatchData> batches ) {
		int animals = 0;
		Collection<LocationView> results = new ArrayList<LocationView>();
		for( BatchData batch: batches)
			results.add( new LocationView( batch ));
		locations = results.toArray( new LocationView[ results.size() ]);
		sum = new Sum( batches);
	}

	public static LocationsView<IMeasurement> show( Collection<BatchData> batches ) {
		return new LocationsView<IMeasurement>( batches);
	}

	public static String showJson( Collection<BatchData> batchData) {
		LocationsView<?> view = show( batchData );
		Gson gson = new Gson();
		return gson.toJson( view, LocationsView.class );
	}

	public class Sum{

		private int measurements;
		private int animals;

		public Sum( Collection<BatchData> batches ) {
			super();
			this.measurements = 0;
			this.animals = 0;
			for( BatchData batch: batches ) {
				this.measurements += batch.getMeasurements().length;
				this.animals += batch.getTotalAnimals();
			}
		}
	}
}
