package org.collin.moodle.rest;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

import org.collin.moodle.advice.AdviceData;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.core.Push;
import org.collin.moodle.service.ActorService;
import org.condast.commons.messaging.rest.RESTUtils;
import org.condast.commons.strings.StringUtils;

//Sets the path to alias + path
@Path("/module")
public class RESTStudent{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
		
	private ActorService service = ActorService.getInstance();

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public RESTStudent() {
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
			service.start(userId, moduleId );
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
			logger.info( "Advice received: '" + input + "'");
			//input = AdviceData.S_EXAMPLE;
			//logger.info( "Compare received: '" + input + "'");
			AdviceData data = gson.fromJson(input, AdviceData.class);
			long userId = data.getUserid();
			String token = data.getToken();
			long courseId = data.getCourseid();
			long moduleId = data.getCmid();
			double progress = data.getProgress();
			boolean response = RESTUtils.checkId(userId, token, courseId);
			if( !response ) {
				return ( courseId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			}
			response = RESTUtils.checkId(userId, token, courseId);
			if( !response ) {
				return ( courseId < 0 )? Response.noContent().build(): Response.status( Status.UNAUTHORIZED ).build();
			}
			logger.info( "Subscriptions found: " + userId );

			IAdviceMap adviceMap = service.createAdvice( userId, courseId, moduleId, progress);
			if(( adviceMap == null ) || adviceMap.isEmpty() )
				return Response.ok().build();

			boolean result = Push.sendPushMessage(userId, adviceMap.getAdvice()[0]);
			return result?Response.ok().build(): Response.status(Status.BAD_REQUEST).build();
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
	public Response update( @QueryParam("id") long userId, @QueryParam("token") String token, @QueryParam("adviceid") int adviceId, @QueryParam("notification") String notification, @QueryParam("progress") double progress ) {
		try{
			//dispatcher.getAdvice(Long.parseLong(id), Long.parseLong( adviceId ), notification);
			logger.info("Notification received: " + notification);
			Response response = Response.serverError().build();
			IAdvice.Notifications notif = StringUtils.isEmpty(notification)? IAdvice.Notifications.DONT_CARE: IAdvice.Notifications.valueOf(notification);
 			IAdviceMap adviceMap = service.updateAdvice(userId, adviceId, notif, progress);
			return (( adviceMap == null ) || adviceMap.isEmpty() )? response:
				Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}

	@SuppressWarnings("unchecked")
	public static double getProgress( String progress ) {
		Gson gson = new Gson();
		Map<Integer,Boolean> map = gson.fromJson(progress, HashMap.class);
		Iterator<Map.Entry<Integer,Boolean>> iterator = map.entrySet().iterator();
		int counter = 0;
		while( iterator.hasNext()) {
			Map.Entry<Integer,Boolean> entry = iterator.next();
			if( entry.getValue())
				counter++;
		}
		return 100d*counter/map.size();
	}
}