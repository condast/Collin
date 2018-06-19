package org.collin.authentication.ds;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.collin.authentication.services.LoginService;
import org.collin.core.authentication.ILoginProvider;
import org.collin.core.authentication.ILoginUser;
import org.condast.commons.authentication.core.IAuthenticationManager;
import org.condast.commons.persistence.service.AbstractPersistencyService;
import org.condast.commons.persistence.service.IPersistenceService;

public class Dispatcher extends AbstractPersistencyService implements ILoginProvider, IPersistenceService{

	//Needs to be the same as in the persistence.xml file
	private static final String S_COLLIN_SERVICE_ID = "org.collin.service"; 
	private static final String S_COLLIN_SERVICE = "Collin Service"; 

	private static Dispatcher service = new Dispatcher();
	
	private  Map<ILoginUser, IAuthenticationManager> managers;
	
	private Dispatcher(  ) {
		super( S_COLLIN_SERVICE_ID, S_COLLIN_SERVICE );
		managers = new HashMap<ILoginUser, IAuthenticationManager>();
	}

	public static Dispatcher getInstance(){
		return service;
	}
	
	public boolean isRegistered( ILoginUser user ) {
		return this.managers.containsKey( user );
	}

	public boolean addUser( ILoginUser user, IAuthenticationManager manager ) throws IOException {
		boolean found = this.managers.containsKey(user);
		if( found ) {
			manager.close();
			return false;
		}
		this.managers.put( user, manager );
		return true;
	}
	
	public IAuthenticationManager getManager( ILoginUser user ) {
		return this.managers.get(user);
	}

	public boolean removeUser( ILoginUser user ) {
		boolean result = this.managers.containsKey(user);
		this.managers.remove( user );
		return result;
	}

	public ILoginUser getUser( long id ) {
		for( ILoginUser user: this.managers.keySet() ) {
		if( user.getId() == id )
			return user;
		}
		return null;
	}

	@Override
	public boolean isRegistered( long loginId ) {
		LoginService service = new LoginService( this );
		ILoginUser user = service.find( loginId );
		return ( user != null );
	}

	@Override
	public boolean isLoggedIn(long loginId) {
		for( ILoginUser user: this.managers.keySet() ) {
			if( user.getId() == loginId )
				return true;
		}
		return ( loginId <= 0);
	}

	@Override
	public ILoginUser getLoginUser(long loginId) {
		for( ILoginUser user: this.managers.keySet() ) {
			if( user.getId() == loginId )
				return user;
		}
		return null;
	}

	@Override
	public Map<Long, String> getUserNames( Collection<Long> userIds) {
		LoginService service = new LoginService( this );
		return service.getUserNames(userIds);
	}
	
	
}