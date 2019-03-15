package org.collin.moodle.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.moodle.core.Push;
import org.collin.moodle.service.ActorService;
import org.condast.commons.messaging.push.ISubscription;

//Sets the path to alias + path
@Path("/push")
public class PushResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_VESSEL = "A request was received from an unknown vessel:";
	
	public static final String S_PUBLIC_KEY = "BDvq04Lz9f7WBugyNHW2kdgFI7cjd65fzfFRpNdRpa9zWvi4yAD8nAvgb8c8PpRXdtgUqqZDG7KbamEgxotOcaA";
	public static final String S_CODED = "BMfyyFPnyR8MRrzPJ6jloLC26FyXMcrL8v46d7QEUccbQVArghc9YHC6USyp4TggrFleNzAUq8df0RiSS13xwtM";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ActorService service = ActorService.getInstance();

	public PushResource() {
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/subscribe")
	public Response subscribe( @QueryParam("id") long id, @QueryParam("token") String token, String subscription ) {
		try{
			logger.info( "Subscription request for " + id + ": " + subscription );
			ISubscription sub = Push.subscribe(id, token, subscription);
			service.start(id, 0);
			return ( sub == null )? Response.status( Status.BAD_REQUEST).build(): 
				Response.ok().build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
	}
}