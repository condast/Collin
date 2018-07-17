package org.collin.authentication.ds;

import org.collin.authentication.services.LoginService;
import org.condast.commons.authentication.core.AbstractLoginProvider;
import org.condast.commons.authentication.user.ILoginUser;

public class Dispatcher extends AbstractLoginProvider{

	//Needs to be the same as in the persistence.xml file
	private static final String S_COLLIN_SERVICE_ID = "org.collin.service"; 
	private static final String S_COLLIN_SERVICE = "Collin Service"; 

	private static Dispatcher service = new Dispatcher();
			
	private Dispatcher(  ) {
		super( S_COLLIN_SERVICE_ID, S_COLLIN_SERVICE );
	}

	public static Dispatcher getInstance(){
		return service;
	}
	
	@Override
	public boolean isRegistered( long loginId ) {
		LoginService service = new LoginService( this );
		ILoginUser user = service.find( loginId );
		return ( user != null );
	}

}