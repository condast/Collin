package org.fgf.animal.count.location.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.core.authentication.AuthenticationUtils;
import org.collin.core.data.WaterAnimalData;
import org.collin.core.model.IWaterAnimal;
import org.fgf.animal.count.location.ds.Dispatcher;
import org.fgf.animal.count.location.model.WaterAnimal;
import org.fgf.animal.count.location.services.WaterAnimalService;

import com.google.gson.Gson;



// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /smash
@Path("/wa")
public class WaterAnimalResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public WaterAnimalResource() {
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public Response findWaterAnimalById( @QueryParam("animalid") long animalId){
		Response retval = null;
		try{
			logger.info( "finding waterAnimal for " + animalId );
			Dispatcher dispatcher = Dispatcher.getInstance();
			WaterAnimalService service = new WaterAnimalService( dispatcher );
			IWaterAnimal waterAnimal = service.find( animalId );
			if( waterAnimal == null )
				retval = Response.noContent().build();
			else {
				Gson gson = new Gson();
				String str = gson.toJson( WaterAnimalData.create(waterAnimal), IWaterAnimal.class);
				retval = Response.ok( str).build();
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
	@Path("/get")
	public Response findWaterAnimalCode( @QueryParam("morpho") long morpho){
		Response retval = null;
		try{
			logger.info( "finding Morphology for " + morpho );
			Dispatcher dispatcher = Dispatcher.getInstance();
			WaterAnimalService service = new WaterAnimalService( dispatcher );
			IWaterAnimal waterAnimal = service.get( morpho);
			if( waterAnimal == null )
				retval = Response.noContent().build();
			else {
				Gson gson = new Gson();
				String str = gson.toJson( WaterAnimalData.create(waterAnimal), IWaterAnimal.class);
				retval = Response.ok( str).build();
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
			WaterAnimalService service = new WaterAnimalService( dispatcher );
			Collection<WaterAnimal> waterAnimals = service.findAll();
			Collection<IWaterAnimal> results = new ArrayList<IWaterAnimal>();
			for( IWaterAnimal wa: waterAnimals )
				results.add( WaterAnimalData.create(wa));
			
			logger.info("Found: " + results.size());
			Gson gson = new Gson();
			String str = gson.toJson(results.toArray( new IWaterAnimal[results.size()] ), IWaterAnimal[].class);
			logger.info(str);
			retval = Response.ok( str).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remove")
	public Response remove( @QueryParam("name") String userName, @QueryParam("token") long token, @QueryParam("morphoid") long morphoid ) {
		Response retval = null;
		try{
			if(!AuthenticationUtils.isAdmin(userName, token)) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;
			}
			Dispatcher dispatcher = Dispatcher.getInstance();
			WaterAnimalService service = new 	WaterAnimalService( dispatcher );
			boolean result = service.remove( morphoid);
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