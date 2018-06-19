package org.fgf.animal.count.location.rest;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.core.authentication.AuthenticationUtils;
import org.collin.core.authentication.ILoginUser;
import org.collin.core.data.BatchData;
import org.collin.core.data.BatchEntry;
import org.collin.core.data.MeasurementData;
import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.view.LocationMeasurements;
import org.condast.commons.Utils;
import org.condast.commons.data.latlng.ILocation;
import org.condast.commons.date.DateFormatter;
import org.fgf.animal.count.location.core.BatchManager;
import org.fgf.animal.count.location.ds.Dispatcher;
import org.fgf.animal.count.location.model.Measurement;
import org.fgf.animal.count.location.services.BatchService;
import org.fgf.animal.count.location.services.LocationService;
import org.fgf.animal.count.location.services.MeasurementService;

import com.google.gson.Gson;


@Path("/measurement")
public class MeasurementResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	
	private static Logger logger = Logger.getLogger( MeasurementResource.class.getName());
	
	public MeasurementResource() {
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addMeasurement( @QueryParam("userid") long userId, @QueryParam("token") int token, 
			@QueryParam("locationid") long locationId, @QueryParam("date") String datestr, @QueryParam("morpho") int morpho, @QueryParam("amount") int amount )
	{
		Response retval = null;
		try{
			logger.info( "Adding measurement for user " + userId + " Date: " + datestr);
			BatchManager manager = new BatchManager();
			Date date = DateFormatter.convert("dd-MM-YYYY:HH-MM-SS", datestr);
			retval = manager.addMeasurement(userId, token, locationId, date, morpho, amount);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addall")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMeasurements( @QueryParam("userid") long userId, @QueryParam("token") long token, 
			@QueryParam("locationid") long locationId, @QueryParam("date") String datestr, String measurementData )
	{
		Response retval = null;
		try{
			String postData = URLDecoder.decode( measurementData, "UTF-8" );
			Date date = DateFormatter.convert("dd-MM-yyyy:HH-mm-ss", datestr);
			logger.info( "Adding measurement for user " + userId + " Date: " + datestr + ":\n"  + postData);
			BatchManager manager = new BatchManager();
			Gson gson = new Gson();
			BatchEntry batch = gson.fromJson( postData, BatchEntry.class);
			retval = manager.addMeasurements(userId, token, locationId, date, batch);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		logger.info( "Adding measurement completed");
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public Response findMeasurement( @QueryParam("userid") long userId, @QueryParam("token") long token,
			@QueryParam("locationid") long locationId )
	{
		Response retval = null;
		try{
			logger.info( "Finding measurements for user " + userId + " and location " + locationId );
			Dispatcher dispatcher = Dispatcher.getInstance();
			if( !dispatcher.isAuthenticated(userId, token))
				return Response.status( Status.UNAUTHORIZED ).build();
			
			BatchService service = new BatchService( dispatcher);
			Collection<? extends IBatch> batches = service.findBatchForLocation(userId, locationId);
			if(( batches == null ) || ( batches.size() == 0 )) {
				retval = Response.noContent().build();
			}else {
				LocationService ls = new LocationService( dispatcher );
				IMeasurementLocation location = ls.find(locationId);
				MeasurementService ms = new MeasurementService( dispatcher );
				ms.setWaterQuality(location);
				ILoginUser user = dispatcher.getLoginUser(userId);
				Collection<BatchData> data = createBatchData(batches);
				LocationMeasurements lm = new LocationMeasurements( user, location, data );
				retval = Response.ok( lm.toJson()).build();
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/findgroups")
	public Response findMeasurementByGroups( @QueryParam("userid") String userIdStr, @QueryParam("token") long token,
			@QueryParam("locationid") long locationId, @QueryParam("range") int range )
	{
		Response retval = null;
		try{
			logger.info( "Finding group measurements for location " + locationId );
			Dispatcher dispatcher = Dispatcher.getInstance();
			BatchService service = new BatchService( dispatcher);
			if( range == 0 )
				range = IBatch.DEFAULT_RANGE;

			boolean hasUser = dispatcher.hasUser(userIdStr, String.valueOf( token ));
			Collection<? extends IBatch> batches = new ArrayList<>();
			long userId = 0;
			if( !hasUser) {
				batches = service.findBatch( locationId, range);	
			}else {
				userId = Long.parseLong(userIdStr);
				if( !dispatcher.isAuthenticated( userId , token))
					return Response.status( Status.UNAUTHORIZED ).build();
				
				batches =  service.findBatchForUser(userId, locationId, range);
			}
			LocationService ls = new LocationService( dispatcher );
			ILocation location = ls.find( locationId );
			if( location == null )
				return Response.noContent().build();
			Collection<LocationMeasurements> results = new ArrayList<LocationMeasurements>();
			ILoginUser user = dispatcher.getLoginUser(userId);
			
			Collection<BatchData> data = createBatchData(batches);
			results.add( new LocationMeasurements( user, location, data));
			Gson gson = new Gson();
			retval = Response.ok( gson.toJson( results.toArray( new LocationMeasurements[ results.size()] ), LocationMeasurements[].class )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getall")
	public Response getAll( @QueryParam("name") String userName, @QueryParam("token") long token ) {
		Response retval = null;
		try{
			//if(!AuthenticationUtils.isAdmin(userName, token)) {
			//	retval = Response.status( Status.UNAUTHORIZED).build();
			//	return retval;
			//}
			Dispatcher dispatcher = Dispatcher.getInstance();
			MeasurementService service = new MeasurementService( dispatcher );
			Collection<Measurement> measurements = service.findAll();
			retval = createMeasurementResponse(measurements);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/userall")
	public Response findMeasurementForUser( @QueryParam("userid") long userId, @QueryParam("token") int token )
	{
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			if(!dispatcher.isAuthenticated(userId, token )) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;				
			}
			
			logger.info( "Finding measurements for user " + userId );
			MeasurementService service = new MeasurementService( dispatcher);
			Collection<Measurement> measurements = service.findMeasurementForUser(userId);
			retval = createMeasurementResponse(measurements);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user")
	public Response findLocationMeasurementForUser( @QueryParam("userid") long userId, @QueryParam("token") long token, @QueryParam("locationid") long locationId )
	{
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			if(!dispatcher.isAuthenticated(userId, token )) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;				
			}
			
			logger.info( "Finding measurements for user " + userId );
			MeasurementService service = new MeasurementService( dispatcher);
			Collection<Measurement> measurements = service.findMeasurementForUser(userId, locationId);
			retval = createMeasurementResponse(measurements);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remove")
	public Response removeMeasurement( @QueryParam("name") String userName, @QueryParam("token") int token, @QueryParam("id") long measurementId ) {
		Response retval = null;
		try{
			if(!AuthenticationUtils.isAdmin(userName, token)) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;
			}
			logger.info( "Adding measurement");
			BatchManager manager = new BatchManager();
			retval = manager.removeMeasurement( measurementId );
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response getMeasurement( @QueryParam("userid") long userId, @QueryParam("token") int token, @QueryParam("id") long measurementId ) {
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			if(!dispatcher.isLoggedIn(userId)) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;				
			}
			logger.info( "Getting measurement");
			BatchManager manager = new BatchManager();
			retval = manager.getMeasurement(userId, token, measurementId);
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/wateranimal")
	public Response findWaterAnimal( @QueryParam("userid") long userId, @QueryParam("name") String name, @QueryParam("description") String description, 
			@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude ) {
		Response retval = null;
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/score")
	public Response getScore( @QueryParam("userid") long userId, @QueryParam("name") String name, @QueryParam("description") String description, 
			@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude ) {
		Response retval = null;
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}
	
	private static Collection<MeasurementData> create( Collection<? extends IMeasurement> measurements){
		Collection<MeasurementData> results = new ArrayList<MeasurementData>();
		for( IMeasurement measurement: measurements )
			results.add( MeasurementData.create(measurement));
		logger.info("Found: " + results.size());
		return results;
	}
	
	private static Response createMeasurementResponse( Collection<? extends IMeasurement> measurements ) {
		Response retval = null;
		if( Utils.assertNull( measurements )) {
			retval = Response.noContent().build();
		}else {
			Collection<MeasurementData> results = create( measurements );
			Gson gson = new Gson();
			String str = gson.toJson( results.toArray( new MeasurementData[ results.size()]), MeasurementData[].class );
			retval = Response.ok( str ).build();
		}
		return retval;
	}
	
	private static Collection<BatchData> createBatchData( Collection<? extends IBatch> batches ) {
		Collection<Long> userIds = new ArrayList<>();
		for( IBatch batch: batches )
			userIds.add(batch.getUserId());
		Dispatcher dispatcher = Dispatcher.getInstance();
		Map<Long, String> ids = dispatcher.getUserNames(userIds);
		Collection<BatchData> data = BatchData.create(batches);
		for( BatchData bd: data ) {
			bd.setUserName( ids.get(bd.getUserId()));
		}
		return data;
	}
}