package org.collin.dashboard.ds;

import org.collin.core.authentication.ILoginUser;
import org.collin.core.authentication.ILoginUserFactory;

/**
 * The volunteer who has access to the data
 * @author Kees
 *
 */
public class AuthenticationDispatcher{

	private static AuthenticationDispatcher dispatcher = new AuthenticationDispatcher();
	
	private ILoginUserFactory factory;

	public static AuthenticationDispatcher getInstance(){
		return dispatcher;
	}
	
	private AuthenticationDispatcher() {
		super();
	}

	public void setFactory( ILoginUserFactory factory ){
		this.factory = factory;
	}

	public void unsetFactory( ILoginUserFactory factory ){
		this.factory = null;
	}
		
	public ILoginUser getOfficer( String userName, String password, String email ) {
		ILoginUser officer = factory.registerUser( userName, password, email );
		//manager.setSupportOfficer(officer);
		return officer;
	}
}
