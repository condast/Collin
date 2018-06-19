package org.fgf.animal.count.location.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.core.model.IWaterTypes;
import org.fgf.animal.count.location.ds.Dispatcher;
import org.fgf.animal.count.location.services.BatchService;
import org.fgf.animal.count.location.services.MeasurementService;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /smash
@Path("/stat")
public class StatisticsResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public StatisticsResource() {
		super();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/total")
	public Response getTotal( @QueryParam("userid") long userId, @QueryParam("token") long token )
	{
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			if(!dispatcher.isLoggedIn(userId)) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;				
			}
			logger.info( "Getting total count for user " + userId );
			BatchService service = new BatchService( dispatcher);
			long result = service.getTotalCountForUser(userId);
			retval = Response.ok( String.valueOf( result )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/totalloc")
	public Response getTotalLocation( @QueryParam("locationid") long locationId )
	{
		Response retval = null;
		try{
			logger.info( "Getting total count for location " + locationId );
			Dispatcher dispatcher = Dispatcher.getInstance();
			MeasurementService service = new MeasurementService( dispatcher); 
			long result = service.getTotalCountForLocation(locationId);
			retval = Response.ok( String.valueOf( result )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/average")
	public Response getAverageWaterQuality( @QueryParam("watertype") int watertype )
	{
		Response retval = null;
		try{
			IWaterTypes.TypeOfWater tow = IWaterTypes.TypeOfWater.getTypeOfWater(watertype);
			logger.info( "Getting average quality of water type " + tow.name() );
			Dispatcher dispatcher = Dispatcher.getInstance();
			MeasurementService service = new MeasurementService( dispatcher);
			double result = service.getAverageWaterQuality( tow );
			retval = Response.ok( String.valueOf( result )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/avgflow")
	public Response getAverageWaterFlowQuality( @QueryParam("waterflow") int waterflow )
	{
		Response retval = null;
		try{
			IWaterTypes.WaterFlow flow = IWaterTypes.WaterFlow.getWaterFlow(waterflow);
			logger.info( "Getting average quality of water flow " + flow.name() );
			Dispatcher dispatcher = Dispatcher.getInstance();
			MeasurementService service = new MeasurementService( dispatcher);
			double result = service.getAverageWaterFlowQuality( flow );
			retval = Response.ok( String.valueOf( result )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/quality")
	public Response getWaterQuality( @QueryParam("locationid") long locationId )
	{
		Response retval = null;
		try{
			logger.info( "Getting total water quality " + locationId );
			Dispatcher dispatcher = Dispatcher.getInstance();
			MeasurementService service = new MeasurementService( dispatcher);
			double result = service.getWaterQuality(locationId);
			retval = Response.ok( String.valueOf( result )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}
}