package org.collin.authentication.rest;

import java.util.Collection;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.login.LoginException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.collin.authentication.core.CollinCallbackHandler;
import org.collin.authentication.core.LoginMediator;
import org.collin.authentication.core.LoginModule;
import org.collin.authentication.ds.Dispatcher;
import org.collin.authentication.model.Login;
import org.collin.authentication.services.LoginService;
import org.collin.core.authentication.AuthenticationUtils;
import org.collin.core.authentication.ILoginUser;
import org.condast.commons.authentication.core.AbstractAuthenticationManager;
import org.condast.commons.strings.StringUtils;
import org.eclipse.equinox.security.auth.ILoginContextListener;

import com.google.gson.Gson;


// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /smash
@Path("/")
public class AuthenticationResource{

	public static final String S_ERR_UNKNOWN_REQUEST = "An invalid request was rertrieved: ";
	public static final String S_ERR_INVALID_USER    = "The provided credentials are invalid:";

	private enum ErrorMessages{
		NO_USERNAME_OR_EMAIL,
		INVALID_USERNAME,
		INVALID_PASSWORD;
	}
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public AuthenticationResource() {
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/register")
	public Response register( @QueryParam("name") String name, @QueryParam("password") String password, @QueryParam("email") String email) {

		Response retval = Response.noContent().build();
		AuthenticationManager manager = new AuthenticationManager();
		manager.open();
		try{
			if( StringUtils.isEmpty(name) && StringUtils.isEmpty( email )) {
				retval = Response.notModified( ErrorMessages.NO_USERNAME_OR_EMAIL.name()).build();
				return retval;
			}else if( StringUtils.isEmpty( name )) {
				name = email.split("[@]")[0];
			}else if( StringUtils.isEmpty( password )) {
				return Response.status( Status.BAD_REQUEST).build();
			}
			
			logger.info( "Registering " + name + "(" + email + ")");
			Dispatcher dispatcher=  Dispatcher.getInstance();
			
			ILoginUser user = manager.login( name, password);
			if( user == null )
				user = manager.registerUser(name, password, email);
			dispatcher.addUser(user, manager);
			retval = manager.response;
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}finally {
			manager.close();
		}

		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login( @QueryParam("name") String name, @QueryParam("password") String password ) {

		Response retval = null;
		AuthenticationManager manager = new AuthenticationManager();
		manager.open();
		try{
			logger.info( "Login " + name );
			if( StringUtils.isEmpty(name)) {
				retval = Response.notModified( ErrorMessages.NO_USERNAME_OR_EMAIL.name()).build();
				return retval;
			}else if( StringUtils.isEmpty( password )) {
				return Response.status( Status.BAD_REQUEST).build();
			}
	
			ILoginUser user = manager.login( name, password);
			if( user == null )
				return Response.status( Status.UNAUTHORIZED).build();

			logger.info( "User logged in " + name );
			retval = manager.response;
			Dispatcher dispatcher=  Dispatcher.getInstance();
			if( dispatcher.isLoggedIn(user.getId())) {
				return retval;
			}else
				dispatcher.addUser(user, manager);
		}catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}finally {
			manager.close();
		}
		return retval;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/logoff")
	public Response logoff( @QueryParam("userid") long id ) {

		Response retval = null;
		Dispatcher dispatcher=  Dispatcher.getInstance();
		try{
			if( !dispatcher.isLoggedIn( id))
				retval = Response.noContent().build();
			else {
				ILoginUser user = dispatcher.getUser( id );
				if( user == null )
					return Response.status( Status.GONE ).build();
				logger.info( "Log off " + user.getUserName() );
				AuthenticationManager manager = (AuthenticationManager) dispatcher.getManager(user);
				manager.logoff(id);
				manager.close();
				retval = manager.response;
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
	@Path("/unregister")
	public Response unregister( @QueryParam("id") long id ) {

		Response retval = null;
		try{
			Dispatcher dispatcher=  Dispatcher.getInstance();
			if( !dispatcher.isRegistered(id))
				retval = Response.noContent().build();
			else {
				ILoginUser user = dispatcher.getUser( id );
				AuthenticationManager manager = (AuthenticationManager) dispatcher.getManager(user);
				logger.info( "Unregister " + user.getUserName() );
				manager.unregisterUser(user);
				retval = manager.response;
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
	public Response getAll( @QueryParam("name") String userName, @QueryParam("token") long token ) {
		Response retval = null;
		try{
			if(!AuthenticationUtils.isAdmin(userName, token)) {
				retval = Response.status( Status.UNAUTHORIZED).build();
				return retval;
			}
			Dispatcher dispatcher = Dispatcher.getInstance();
			LoginService service = new LoginService( dispatcher );
			Collection<Login> Logins = service.findAll();
			Gson gson = new Gson();
			String str = gson.toJson(Logins.toArray( new Login[Logins.size()] ), Login[].class);
			retval = Response.ok( str).build();
		}
		catch( Exception ex ){
			ex.printStackTrace();
			return Response.serverError().build();
		}
		return retval;
	}

	public class AuthenticationManager extends AbstractAuthenticationManager implements Callback{

		static final String S_CALLBACK_ID = "FGF";

		private LoginMediator mediator = LoginMediator.getIntance();
		private Callback callback = this;

		private ILoginContextListener listener = new ILoginContextListener(){

			@Override
			public void onLoginStart(Subject arg0) {
				mediator.add( callback );
			}

			@Override
			public void onLoginFinish(Subject arg0, LoginException arg1) {
				if( arg1 != null ){

					try{
						if( AuthenticationResults.isValid( arg1.getMessage() )){
							return;
						}
						LoginModule module = new LoginModule();
						module.login( new CollinCallbackHandler());
						completeLogin( AuthenticationResults.OK );
					}
					catch( IllegalArgumentException ex ){
						logger.warning( ex.getMessage() + "\n defaulting to local login");
					} catch (LoginException e) {
						e.printStackTrace();
						completeLogin( AuthenticationResults.INVALID_NAME );
					}
					return;
				}
				mediator.remove( callback );
			}


			protected void completeLogin( final AuthenticationResults result ){
				
			}
			
			@Override
			public void onLogoutStart(Subject arg0) {
				// NOTHING	
			}
			

			@Override
			public void onLogoutFinish(Subject arg0, LoginException arg1) {
			}
		};

		private Response response;
		
		private AuthenticationManager() {
			super( S_CALLBACK_ID );
			registerListener(listener);
		}

		public synchronized ILoginUser registerUser(String userName, String password, String email ) {
			Dispatcher dispatcher = Dispatcher.getInstance();
			response = null;
			LoginService service = new LoginService( dispatcher );
			ILoginUser user = null;
			service.open();
			try {
				user = service.create(userName, password, email);
				response = Response.ok( toResponseString(user)).build();
			}
			catch( Exception ex ) {
				ex.printStackTrace();
				response = Response.serverError().build();
			}
			finally {
				service.close();
			}
			return user;
		}
		
		public synchronized void unregisterUser(ILoginUser officer) {
			Dispatcher dispatcher = Dispatcher.getInstance();
			LoginService service = new LoginService( dispatcher );
			ILoginUser user = null;
			service.open();
			try {
				dispatcher.removeUser(user);
				service.remove( officer.getId() );
				response = Response.ok().build();
			}
			catch( Exception ex ) {
				ex.printStackTrace();
				response = Response.serverError().build();
			}
			finally {
				service.close();
				Thread.currentThread().interrupt();
			}
		}

		public synchronized ILoginUser login( String userName, String password ) {
			Dispatcher dispatcher = Dispatcher.getInstance();
			LoginService service = new LoginService( dispatcher );
			ILoginUser user = null;
			service.open();
			try {
				user = service.login(userName, password);
				if( user == null )
					response = Response.status( Status.UNAUTHORIZED).build();
				else
					response = Response.ok( toResponseString(user)).build();
			}
			catch( Exception ex ) {
				ex.printStackTrace();
				response = Response.serverError().build();
			}
			finally {
				service.close();
			}
			return user;
		}
		
		public synchronized ILoginUser logoff( long id ) {
			Dispatcher dispatcher = Dispatcher.getInstance();
			ILoginUser user = dispatcher.getUser(id);
			if( user == null )
				response = Response.noContent().build();
			else {
				dispatcher.removeUser( user );		
				response = Response.ok().build();
			}
			return user;
		}
	}

	/**
	 * Create a response object of the user
	 * @param user
	 * @return
	 */
	public static String toResponseString( ILoginUser user ) {
		long[] str = new long[2];
		str[0] = user.getId();
		str[1] = user.getToken();
		Gson gson = new Gson();
		return gson.toJson(str, long[].class);
	}

}