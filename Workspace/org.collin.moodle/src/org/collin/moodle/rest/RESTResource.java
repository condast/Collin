package org.collin.moodle.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.core.impl.SequenceNode;
import org.collin.moodle.core.Dispatcher;
import org.condast.commons.messaging.push.ISubscription;
import org.condast.commons.messaging.rest.RESTUtils;
import org.condast.commons.strings.StringUtils;

import nl.martijndwars.webpush.core.PushManager;

//Sets the path to alias + path
@Path("/module")
public class RESTResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	public static final String S_PUBLIC_KEY = "BDvq04Lz9f7WBugyNHW2kdgFI7cjd65fzfFRpNdRpa9zWvi4yAD8nAvgb8c8PpRXdtgUqqZDG7KbamEgxotOcaA";
	public static final String S_PRIVATE_KEY = "CxbJjjbVMABqzv72ZL4GH_0gNStbZV0TSBaNOIzLwbE";
	
	public static final String S_CODED = "BMfyyFPnyR8MRrzPJ6jloLC26FyXMcrL8v46d7QEUccbQVArghc9YHC6USyp4TggrFleNzAUq8df0RiSS13xwtM";
	
	private Dispatcher dispatcher = Dispatcher.getInstance();

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public RESTResource() {
	}

	// This method is called if TEXT_PLAIN is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lesson")
	public Response getLesson( @QueryParam("id") long id, @QueryParam("token") String token, @QueryParam("module-id") long moduleId, @QueryParam("activity-id") long activityId ) {

		try{
			boolean response = RESTUtils.checkId(moduleId, token, moduleId);
			if( !response )
				return Response.status( Status.BAD_REQUEST).build();
			SequenceNode node = dispatcher.findLesson( moduleId, activityId ); 
				return ( node != null )? Response.ok( node.getUri()).build(): Response.noContent().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	// This method is called if TEXT_PLAIN is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/modules")
	public Response getModules( @QueryParam("id") long id, @QueryParam("token") String token ) {

		try{
			String result = "";//dispatcher.getModules();
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
			@QueryParam("module") long moduleId) {

		try{
			boolean response = RESTUtils.checkId(id, token, moduleId);
			if( !response )
				return ( moduleId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			response = RESTUtils.checkId(id, token, moduleId);
			if( !response )
				return ( moduleId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			String result = dispatcher.start(moduleId);
			return StringUtils.isEmpty(result)? Response.noContent().build(): Response.ok( result ).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/advice")
	public Response getAdvice( @QueryParam("id") long id, @QueryParam("token") String token, @QueryParam("module-id") long moduleId, @QueryParam("activity-id") long activityId, @QueryParam("progress") double progress ) {
		try{
			boolean response = RESTUtils.checkId(id, token, moduleId);
			if( !response ) {
				return ( moduleId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			}
			response = RESTUtils.checkId(id, token, moduleId);
			if( !response ) {
				return ( moduleId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			}
			PushManager pm = dispatcher.getPushMananger();
			ISubscription[] subscriptions = pm.getSubscriptions();
			logger.info( "Subscriptions found: " + subscriptions.length );
			String result;
			for( ISubscription subscription: subscriptions ) {
				logger.info( PushManager.sendPushMessage( S_PUBLIC_KEY, S_PRIVATE_KEY, subscription, "Hello".getBytes()));				
			}
			return Response.ok().build();
			//SequenceNode result = dispatcher.getAdvice( moduleId, activityId, progress);
			//String[] advice = result.getData();
			//Random random = new Random(advice.length);
			//return (result == null)? Response.noContent().build(): Response.ok( advice[random.nextInt()]).build();
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