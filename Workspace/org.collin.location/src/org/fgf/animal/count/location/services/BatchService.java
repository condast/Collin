package org.fgf.animal.count.location.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

import org.collin.core.data.BatchEntry;
import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurement;
import org.condast.commons.Utils;
import org.condast.commons.authentication.user.ILoginUser;
import org.condast.commons.comparable.NullComparator;
import org.condast.commons.data.latlng.ILocation;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;
import org.fgf.animal.count.location.model.Batch;

public class BatchService extends AbstractEntityService<Batch>{

	public static final String S_QUERY_FIND_BATCH = "SELECT b FROM Batch b WHERE b.location.id = :location";
	public static final String S_QUERY_FIND_BATCH_FOR_USER = 
			"SELECT b FROM Batch b WHERE b.userid = :userid";
	public static final String S_QUERY_FIND_LOCATION_BATCH_FOR_USER = 
			"SELECT b FROM Batch b WHERE b.location.id = :location AND b.userid = :userid";
	public static final String S_QUERY_FIND_BATCH_IN_RANGE = 
			"SELECT b FROM Batch b INNER JOIN b.location l WHERE l.latitude >= :latmin AND l.latitude <= :latmax AND "
			+ "l.longitude >= :lonmin AND l.longitude <= :lonmax ";

	public static final String S_QUERY_TOTAL_BATCH_FOR_USER = "SELECT COUNT(b) FROM Batch b WHERE b.userid = :userid";
		
	private static final String S_USER_ID = "userid";
	private static final String S_LOCATION = "location";
	private static final String S_LON_MIN = "lonmin";
	private static final String S_LON_MAX = "lonmax";
	private static final String S_LAT_MIN = "latmin";
	private static final String S_LAT_MAX = "latmax";
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	public BatchService( IPersistenceService service ) {
		super( Batch.class, service );
	}

	private Batch create( ILocation location, Date date, ILoginUser user, String description, double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater ) {
		return this.create( location, date, user, IBatch.DEFAULT_RANGE, description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater );
	}
	
	private Batch create( ILocation location, Date date, ILoginUser user, int range, String description, double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater ) {
		Batch batch = new Batch( user, location, date, range, description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater );
		super.create(batch);
		return batch;
	}

	public synchronized Response addBatch( ILocation location, Date date, ILoginUser user, int morpho, long amount, String description,
			double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater ) {
		Map<Integer, Long> measurement = new HashMap<Integer, Long>();
		measurement.put(morpho, amount);
		return addBatch(location, date, user, measurement, description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater);
	}

	public synchronized Response addBatch( ILocation location, Date date, ILoginUser user, BatchEntry entry ) {
		return this.addBatch( location, date, user, entry.getMeasurements(), entry.getDescription(), 
				entry.getSecchiDepth(), entry.getMudthickness(), entry.isPlantOnWater(), entry.isPlantUnderwater() );
	}
	
	public synchronized Response addBatch( ILocation location, Date date, ILoginUser user, Map<Integer, Long> measurements, String description,
			double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater ) {
		if( Utils.assertNull(measurements ))
			return Response.noContent().build();
		BatchService service = new BatchService( super.getService() );
		IBatch batch = service.create(location, date, user, description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater);
		MeasurementService ms = new MeasurementService( super.getService());
		StringBuffer buffer = new StringBuffer();
		try {
			ms.open();
			Collection<IMeasurement> results = ms.addMeasurements( batch, measurements);
			Collection<Long> ids = new ArrayList<>();
			for( IMeasurement measurement: results ) {
				batch.addMeasurement(measurement);
				ids.add(measurement.getId());
			}
			return Response.ok( String.valueOf( ids.toArray( new Long[ ids.size() ]))).build();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			logger.info( buffer.toString());
			ms.close();
		}
	}

	public List<? extends IBatch> findBatch( long locationId ){
		TypedQuery<Batch> query = super.getTypedQuery( S_QUERY_FIND_BATCH );
		query.setParameter( S_LOCATION, locationId );
		return query.getResultList();
	}

	public Collection<Batch> findbatch( LatLng latlng, int range ){
		TypedQuery<Batch> query = super.getTypedQuery( S_QUERY_FIND_BATCH_IN_RANGE );
		query.setParameter( S_LAT_MIN, latlng.getLatitude() - range);
		query.setParameter( S_LAT_MAX, latlng.getLatitude() + range);
		query.setParameter( S_LON_MIN, latlng.getLongitude() - range);
		query.setParameter( S_LON_MAX, latlng.getLongitude() + range);
		List<Batch> results = query.getResultList();
		Collections.sort(results, new BatchComparator());
		return results;
	}

	/**
	 * Get all the measurements for given location id. Group
	 * all the measurements within a given range. Sort by date
	 * @param userId
	 * @param locationId
	 * @return
	 */
	public Collection<? extends IBatch> findBatch( long locationId, long range ){
		List<? extends IBatch> batches = findBatch( locationId);
		Collections.sort(batches, new BatchComparator());
		return batches;
	}

	public Collection<? extends IBatch> findBatchForUser( long userId ){
		TypedQuery<Batch> query = super.getTypedQuery( S_QUERY_FIND_BATCH_FOR_USER );
		query.setParameter( S_USER_ID, userId );
		return query.getResultList();
	}

	/**
	 * Get all the batchs for the given user and location id. sort by date
	 * @param userId
	 * @param locationId
	 * @return
	 */
	public List<? extends IBatch> findBatchForLocation( long userId,  long locationId ){
		String queryStr = ( userId == 0 )? S_QUERY_FIND_BATCH: S_QUERY_FIND_LOCATION_BATCH_FOR_USER;
		TypedQuery<Batch> query = super.getTypedQuery(queryStr );
		query.setParameter(S_LOCATION, locationId );
		if( userId != 0 )
			query.setParameter( S_USER_ID, userId );
		List<Batch> batchs = query.getResultList();
		return batchs;
	}

	/**
	 * Get all the measurements for the given user and location id. sort by date
	 * @param userId
	 * @param locationId
	 * @return
	 */
	public List<? extends IBatch> findBatchForUser( long userId,  long locationId, long range ){
		TypedQuery<Batch> query = super.getTypedQuery( S_QUERY_FIND_BATCH_IN_RANGE );
		query.setParameter( S_LOCATION, locationId );
		List<Batch> batches = query.getResultList();
		List <IBatch> results = new ArrayList<>();
		for( IBatch batch: batches ) {
			if( batch.getLoginUser().getId() == userId )
				results.add(batch);
		}
		return results;
	}

	/**
	 * Get the total count for the given user 
	 * @param userId
	 * @return
	 */
	public long getTotalCountForUser( long userId ){
		TypedQuery<Long> query = super.getManager().createQuery( S_QUERY_TOTAL_BATCH_FOR_USER, Long.class );
		query.setParameter( S_USER_ID, userId );
		return query.getSingleResult();
	}

	public boolean remove( ILoginUser user, long id) {
		IBatch batch = super.find( id );
		if(( batch == null ) ||( batch.getLoginUser().getId() != user.getId() ))
			return false;
		super.remove(id);
		return true;
	}

	private static class BatchComparator implements Comparator<IBatch>{

		public BatchComparator() {
			super();
		}

		@Override
		public int compare(IBatch arg0, IBatch arg1) {
			NullComparator<IBatch> nullc = new NullComparator<IBatch>();
			int result = nullc.compare(arg0, arg1);
			if( result != 0 )
				return result;
			if( arg0.getLoginUser().getId() != arg1.getLoginUser().getId() )
				return ( arg0.getLoginUser().getId() > arg1.getLoginUser().getId() )?1: -1; 
			
			long time0 = arg0.getCreateDate().getTime();
			long time1 = arg1.getCreateDate().getTime();
			return time0 < time1? -1: time0> time1?1: 0;
		}
	}
}