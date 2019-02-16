package org.collin.moodle.rest;

import com.google.gson.Gson;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.core.Dispatcher;
import org.collin.moodle.core.MoodleProcess;
import org.collin.moodle.core.PushOptionsAdviceBuilder;
import org.condast.commons.messaging.push.ISubscription;
import org.condast.commons.messaging.rest.RESTUtils;
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
	public Response startModule( @QueryParam("id") long userId, @QueryParam("token") String token, 
			@QueryParam("module") long moduleId) {

		try{
			boolean response = RESTUtils.checkId(userId, token, moduleId);
			if( !response )
				return ( moduleId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			response = RESTUtils.checkId( userId, token, moduleId);
			if( !response )
				return ( moduleId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			dispatcher.start(userId, moduleId );
			return Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/advice")
	public Response getAdvice( String input ) {
		try{
			Gson gson = new Gson();
			String[] split = gson.fromJson(input, String[].class);
			long userId = Long.parseLong( split[0] );
			String token = split[1];
			long courseId = Long.parseLong( split[2]);
			long moduleId = Long.parseLong( split[3]);
			double progress = MoodleProcess.getProgress(split[4]);
			boolean response = RESTUtils.checkId(userId, token, courseId);
			if( !response ) {
				return ( courseId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			}
			response = RESTUtils.checkId(userId, token, courseId);
			if( !response ) {
				return ( courseId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			}
			logger.info( "Subscriptions found: " + userId );

			IAdviceMap adviceMap = dispatcher.getAdvice( userId, courseId, moduleId, progress);
			if(( adviceMap == null ) || adviceMap.isEmpty() )
				return Response.ok().build();

			PushManager pm = dispatcher.getPushMananger();
			ISubscription subscription = pm.getSubscription( userId );
			PushOptionsAdviceBuilder builder = new PushOptionsAdviceBuilder();
			builder.createPayLoad( adviceMap.getAdvice()[0], true );
			logger.info(builder.toString());
			PushManager.sendPushMessage( S_PUBLIC_KEY, S_PRIVATE_KEY, subscription, builder.createPayLoad());				
			return Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response update( @QueryParam("id") String userId, @QueryParam("token") String token, @QueryParam("courseId") String courseId, @QueryParam("activity-id") long activityId, @QueryParam("progress") double progress) {
		try{
			//dispatcher.getAdvice(Long.parseLong(id), Long.parseLong( adviceId ), notification);
			Response response = Response.serverError().build();
			IAdviceMap adviceMap = dispatcher.getAdvice(Long.parseLong(userId), Long.parseLong(courseId),  activityId, progress);
			if(( adviceMap == null ) || adviceMap.isEmpty() )
				return Response.ok().build();
			return response;
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

}