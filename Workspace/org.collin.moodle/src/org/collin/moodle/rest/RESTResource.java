package org.collin.moodle.rest;

import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.collin.moodle.core.Dispatcher;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /smash
@Path("/rest")
public class RESTResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	//private Dispatcher dispatcher = Dispatcher.getInstance();

	// This method is called if TEXT_PLAIN is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/modules")
	public Response getModules( @QueryParam("id") long id, @QueryParam("token") String token ) {

		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			String result = dispatcher.getModules();
			return Response.ok( result ).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	// This method is called if TEXT_PLAIN is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/start")
	public Response startModule( @QueryParam("id") long id, @QueryParam("token") String token, 
			@QueryParam("module") long moduleId, @QueryParam("submodule") int subModule) {

		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			return Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	// This method is called if TEXT_PLAIN is requested
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/progress")
	public Response getProgress( @QueryParam("id") long id, @QueryParam("token") String token, 
			@QueryParam("module") long moduleId) {

		try{
			Dispatcher dispatcher = Dispatcher.getInstance();
			int progress = dispatcher.getProgress(moduleId);
			return Response.ok( String.valueOf( progress )).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	// This method is called if TEXT_PLAIN is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response update( @QueryParam("id") long id, @QueryParam("token") String token, 
			@QueryParam("module") String module, @QueryParam("chapter") int chapter, @QueryParam("progress") int progress) {

		try{
			return Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
}