package org.fgf.animal.count.location.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;

import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.model.IWaterAnimal;
import org.collin.core.model.IWaterTypes;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;
import org.fgf.animal.count.location.model.Measurement;

public class MeasurementService extends AbstractEntityService<Measurement>{

	public static final String S_QUERY_FIND_MEASUREMENT = "SELECT m FROM Measurement m WHERE m.batch.location.id = :location";
	public static final String S_QUERY_FIND_MEASUREMENTS_FOR_USER = 
			"SELECT m FROM Measurement m WHERE m.batch.userid = :userid";
	public static final String S_QUERY_FIND_LOCATION_MEASUREMENT_FOR_USER = 
			"SELECT m FROM Measurement m WHERE m.batch.location.id = :location AND m.batch.userid = :userid";
	public static final String S_QUERY_FIND_MEASUREMENTS_IN_RANGE = 
			"SELECT m FROM Measurement m, Location l JOIN m.batch.location=l WHERE l.latitude >= :latmin AND l.latitude <= :latmax AND "
			+ "l.longitude >= :lonmin AND l.longitude <= :lonmax ";

	public static final String S_QUERY_TOTAL_MEASUREMENT_FOR_USER = "SELECT COUNT(m) FROM Measurement m WHERE m.userid = :userid";
	public static final String S_QUERY_TOTAL_MEASUREMENT = "SELECT SUM(m.amount) FROM Measurement m WHERE m.batch.location.id = :location";
	
	//We multiply by 1.0 in order to cast the query to double
	//@See: https://stackoverflow.com/questions/10544307/how-can-i-use-typcasting-inside-a-jpql-statement/10571783
	public static final String S_QUERY_AVERAGE_QUALITY_FOR_WATER_TYPE = "SELECT (1.0 * SUM(w.qualityIndicator * m.amount))/SUM(m.amount) FROM Measurement m JOIN m.waterAnimal w WHERE m.batch.location.waterType.waterType= :watertype";
	public static final String S_QUERY_AVERAGE_QUALITY_FOR_WATER_FLOW = "SELECT (1.0 * SUM(w.qualityIndicator * m.amount))/SUM(m.amount) FROM Measurement m JOIN m.waterAnimal w WHERE m.batch.location.waterType.waterFlow= :waterflow";
	
	public static final String S_QUERY_WATER_QUALITY = "SELECT AVG(w.qualityIndicator) FROM  Measurement m JOIN m.waterAnimal w WHERE m.batch.location.id = :location";

	public static final String S_ERR_INVALID_MORPHO = "Invalid morpho code: ";

	private static final String S_USER_ID = "userid";
	private static final String S_LOCATION = "location";
	private static final String S_LON_MIN = "lonmin";
	private static final String S_LON_MAX = "lonmax";
	private static final String S_LAT_MIN = "latmin";
	private static final String S_LAT_MAX = "latmax";
	private static final String S_WATER_TYPE = "watertype";
	private static final String S_WATER_FLOW = "waterflow";
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	public MeasurementService( IPersistenceService service ) {
		super( Measurement.class, service );
	}

	public Measurement create( IBatch batch, IWaterAnimal waterAnimal, int amount ) {
		Measurement measurement = new Measurement( batch, waterAnimal, amount );
		super.create(measurement);
		return measurement;
	}

	public synchronized IMeasurement addMeasurement( IBatch batch, int morpho, long amount ) {
		Map<Integer, Long> measurement = new HashMap<Integer, Long>();
		measurement.put(morpho, amount);
		return addMeasurements(batch, measurement).iterator().next();
	}
	
	public synchronized Collection<IMeasurement> addMeasurements( IBatch batch, Map<Integer, Long> measurements ) {
		WaterAnimalService was = new WaterAnimalService( super.getService());
		StringBuffer buffer = new StringBuffer();
		Collection<IMeasurement> results = new ArrayList<IMeasurement>();
		try {
			Iterator<Map.Entry<Integer, Long>> iterator = measurements.entrySet().iterator();
			buffer.append("Adding measurements: ");
			while( iterator.hasNext()) {
				Map.Entry<Integer, Long> entry = iterator.next();
				IWaterAnimal wateranimal = was.find( entry.getKey());
				if( wateranimal == null ) {
					logger.severe( S_ERR_INVALID_MORPHO + entry.getKey() );
					continue;
				}
				buffer.append("{" + entry.getKey() + ", " + entry.getValue() + "}");
				int amount = (int)entry.getValue().doubleValue();
				IMeasurement measurement = create( batch, wateranimal, amount);
				results.add( measurement);
			}
		}
		finally {
			logger.info( buffer.toString());
		}
		return results;
	}

	public List<Measurement> findMeasurement( long locationId ){
		TypedQuery<Measurement> query = super.getTypedQuery( S_QUERY_FIND_MEASUREMENT );
		query.setParameter( S_LOCATION, locationId );
		return query.getResultList();
	}


	public Collection<Measurement> findMeasurement( LatLng latlng, int range ){
		TypedQuery<Measurement> query = super.getTypedQuery( S_QUERY_FIND_MEASUREMENTS_IN_RANGE );
		query.setParameter( S_LAT_MIN, latlng.getLatitude() - range);
		query.setParameter( S_LAT_MAX, latlng.getLatitude() + range);
		query.setParameter( S_LON_MIN, latlng.getLongitude() - range);
		query.setParameter( S_LON_MAX, latlng.getLongitude() + range);
		return query.getResultList();
	}

	/**
	 * Get all the measurements for the given user and location id. sort by date
	 * @param userId
	 * @param locationId
	 * @return
	 */
	public List<Measurement> findMeasurementForUser( long userId ){
		TypedQuery<Measurement> query = super.getTypedQuery( S_QUERY_FIND_MEASUREMENTS_FOR_USER );
		query.setParameter( S_USER_ID, userId );
		List<Measurement> measurements = query.getResultList();
		return measurements;
	}

	/**
	 * Get all the measurements for the given user and location id. sort by date
	 * @param userId
	 * @param locationId
	 * @return
	 */
	public List<Measurement> findMeasurementForUser( long userId,  long locationId ){
		TypedQuery<Measurement> query = super.getTypedQuery( S_QUERY_FIND_LOCATION_MEASUREMENT_FOR_USER );
		query.setParameter( S_USER_ID, userId );
		query.setParameter(S_LOCATION, locationId );
		List<Measurement> measurements = query.getResultList();
		return measurements;
	}
	
	/**
	 * Get the total count for the given type of water
	 * @param userId
	 * @return
	 */
	public double getAverageWaterQuality( IWaterTypes.TypeOfWater tow ){
		TypedQuery<Double> query = super.getManager().createQuery( S_QUERY_AVERAGE_QUALITY_FOR_WATER_TYPE, Double.class );
		query.setParameter( S_WATER_TYPE, tow.getIndex());
		double result = 0;
		if( query.getSingleResult() != null )
			result = query.getSingleResult();
		return result;
	}

	/**
	 * Get the total count for the given type of water
	 * @param userId
	 * @return
	 */
	public double getAverageWaterFlowQuality( IWaterTypes.WaterFlow waterflow ){
		TypedQuery<Double> query = super.getManager().createQuery( S_QUERY_AVERAGE_QUALITY_FOR_WATER_FLOW, Double.class );
		query.setParameter( S_WATER_FLOW, waterflow.getIndex());
		double result = 0;
		if( query.getSingleResult() != null )
			result = query.getSingleResult();
		return result;
	}

	/**
	 * Get the total count for the given user 
	 * @param userId
	 * @return
	 */
	public double getWaterQuality( long locationId ){
		TypedQuery<Double> query = super.getManager().createQuery( S_QUERY_WATER_QUALITY, Double.class );
		query.setParameter( S_LOCATION, locationId );
		Double result = query.getSingleResult();
		return (result == null )?0: result;
	}

	public void setWaterQuality( IMeasurementLocation location ){
		double quality = this.getWaterQuality(location.getId());
		location.setWaterQualityIndex(quality);
	}

	/**
	 * Get the total count for the given user 
	 * @param userId
	 * @return
	 */
	public long getTotalCountForLocation( long locationId ){
		TypedQuery<Long> query = super.getManager().createQuery( S_QUERY_TOTAL_MEASUREMENT, Long.class );
		query.setParameter( S_LOCATION, locationId );
		return query.getSingleResult();
	}

}