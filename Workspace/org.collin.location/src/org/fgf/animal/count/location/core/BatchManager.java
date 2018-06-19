package org.fgf.animal.count.location.core;

import com.google.gson.Gson;

import java.util.Date;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.core.authentication.ILoginUser;
import org.collin.core.data.BatchEntry;
import org.collin.core.data.MeasurementData;
import org.collin.core.model.IMeasurement;
import org.condast.commons.data.latlng.ILocation;
import org.fgf.animal.count.location.ds.Dispatcher;
import org.fgf.animal.count.location.services.BatchService;
import org.fgf.animal.count.location.services.LocationService;
import org.fgf.animal.count.location.services.MeasurementService;

public class BatchManager {

	public static final String S_ERR_INVALID_USER = "The provided user is invalid. Please login first:";
	public static final String S_ERR_INVALID_LOCATION = "The provided location has not been found:";

	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	public synchronized Response addMeasurement( long userId, int token, long locationId, Date date, int morpho, int amount ) {
		return addMeasurement(userId, token, locationId, date, morpho, amount, null, 1.0, 0.0, false, false );
	}
	public synchronized Response addMeasurement( long userId, int token, long locationId, Date date, int morpho, int amount, String description,
			double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater ) {
		Dispatcher dispatcher = Dispatcher.getInstance();
		if( !dispatcher.isAuthenticated(userId, token))
			return Response.status( Status.UNAUTHORIZED ).build();
		
		ILoginUser user = dispatcher.getLoginUser( userId);
		try {
			LocationService ls = new LocationService( dispatcher );
			ILocation location = ls.find( locationId);
			if( location == null )
				return Response.status( Status.BAD_REQUEST).build();
			BatchService bs = new BatchService( dispatcher );
			return bs.addBatch(location, date, user, morpho, amount, description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	public synchronized Response addMeasurements( long userId, long token, long locationId, 
			Date date, BatchEntry entry ) {
		Dispatcher dispatcher = Dispatcher.getInstance();
		//if( !dispatcher.isAuthenticated(userId, token))
		//	return Response.status( Status.UNAUTHORIZED ).build();
		ILoginUser user = dispatcher.getLoginUser( userId);
		try {
			LocationService ls = new LocationService( dispatcher );
			ILocation location = ls.find( locationId);
			if( location == null ) {
				logger.severe( S_ERR_INVALID_LOCATION + locationId);
				return Response.status( Status.BAD_REQUEST).build();
			}
			BatchService bs = new BatchService( dispatcher );
			return bs.addBatch(location, date, user, entry);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	public synchronized Response removeMeasurement( long measurementId ) {
		Dispatcher dispatcher = Dispatcher.getInstance();
		MeasurementService ms = new MeasurementService( dispatcher );
		ms.remove(measurementId);
		return Response.ok().build();
	}

	public synchronized Response getMeasurement( long userId, long token, long measurementId ) {
		Dispatcher dispatcher = Dispatcher.getInstance();
		if( !dispatcher.isAuthenticated(userId, token))
			return Response.status( Status.UNAUTHORIZED ).build();
		MeasurementService ms = new MeasurementService( dispatcher );
		IMeasurement measurement = ms.find( measurementId);
		Gson gson = new Gson();
		String result = gson.toJson( MeasurementData.create(measurement ), MeasurementData.class);
		return Response.ok( result ).build();
	}

}
