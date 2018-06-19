package org.fgf.animal.count.location.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.core.authentication.AuthenticationUtils;
import org.collin.core.authentication.ILoginUser;
import org.collin.core.data.BatchData;
import org.collin.core.data.LocationData;
import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurementLocation;
import org.collin.core.model.IWaterTypes;
import org.collin.core.view.LocationsView;
import org.condast.commons.Utils;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.strings.StringUtils;
import org.fgf.animal.count.location.ds.Dispatcher;
import org.fgf.animal.count.location.model.Location;
import org.fgf.animal.count.location.services.BatchService;
import org.fgf.animal.count.location.services.LocationService;
import org.fgf.animal.count.location.services.MeasurementService;
import org.fgf.animal.count.location.services.WaterTypesService;

import com.google.gson.Gson;



// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /smash
@Path("/")
public class LocationResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "An request was received from an unknown vessel:";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public LocationResource() {
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/add")
	public Response addLocation( @QueryParam("userid") long userId, @QueryParam("token") long token,  @QueryParam("name") String name, @QueryParam("description") String description, 
			@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude, @QueryParam("watertype") int watertype )
	{
		Response retval = null;
		logger.info( "Adding location " + name + "(" + description + ")");
		Dispatcher dispatcher = Dispatcher.getInstance();

		LatLng latlng = StringUtils.isEmpty( name )? new LatLng( latitude, longitude ): new LatLng( name, latitude, longitude );
		LocationService service = new LocationService( dispatcher );
		WaterTypesService ws = new WaterTypesService(dispatcher);
		try{
			service.open();
			IWaterTypes wtype = ws.find( IWaterTypes.TypeOfWater.getTypeOfWater(watertype ));
			IMeasurementLocation location = service.create(name, description, latlng, wtype );
			retval = Response.ok( String.valueOf( location.getId())).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		finally {
			service.close();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public Response findLocation( @QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude, @QueryParam("range") int range ) {
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			LocationService service = new LocationService( dispatcher );
			Collection<Location> locations = ( range == 0 )?
					service.findLocation( new LatLng( latitude, longitude )):
						service.findLocation( new LatLng( latitude, longitude ), range);
					if( Utils.assertNull( locations))
						retval = Response.noContent().build();
					else {
						Gson gson = new Gson();
						retval = Response.ok( gson.toJson( locations.toArray( new Location[ locations.size()]), Location[].class )).build();
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
	@Path("/getall")
	public Response getAll() {
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			LocationService service = new LocationService( dispatcher );
			Collection<Location> locations = service.findAll();		
			logger.fine("Locations found: " + locations.size() );
			BatchService bs = new BatchService(dispatcher);
			MeasurementService ms = new MeasurementService(dispatcher);
			Collection<BatchData> data = new ArrayList<>();
			ILoginUser user = dispatcher.getLoginUser(0);
			for( IMeasurementLocation location: locations ) {
				location.setWaterQualityIndex( ms.getWaterQuality(location.getId()));
				Collection<? extends IBatch> batches = bs.findBatchForLocation(user.getId(), location.getId() ); 
				logger.fine("Batches found for: " + location.getName() + ": " + batches.size() );
				for( IBatch batch: batches )
					data.add( new BatchData( batch ));
			}
			String result = LocationsView.showJson(data);
			logger.fine("Results: " + result );
			retval = Response.ok(result).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/findall")
	public Response findAll() {
		Response retval = null;
		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			LocationService service = new LocationService( dispatcher );
			Collection<Location> locations = service.findAll();		
			logger.fine("Locations found: " + locations.size() );
			Collection<LocationData> data = new ArrayList<>();
			for( IMeasurementLocation location: locations ) {
				data.add( new LocationData( location ));
			}
			Gson gson = new Gson();
			String result = gson.toJson( data.toArray( new LocationData[ data.size()]), LocationData[].class);
			logger.fine("Results: " + result );
			retval = Response.ok(result).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove")
	public Response remove( @QueryParam("name") String userName, @QueryParam("token") long token, @QueryParam("locationid") long locationid ) {
		Response retval = null;
		try{
			if(!AuthenticationUtils.isAdmin(userName, token)) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;
			}
			Dispatcher dispatcher = Dispatcher.getInstance();
			LocationService service = new LocationService( dispatcher );
			boolean result = service.remove( locationid);
			if( result )
				retval = Response.ok().build();
			else
				retval = Response.noContent().build();
			return retval;
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
}