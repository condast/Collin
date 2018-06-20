package org.collin.authentication.ds;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.collin.authentication.services.LoginService;
import org.collin.core.authentication.ILoginProvider;
import org.collin.core.authentication.ILoginUser;
import org.condast.commons.persistence.service.AbstractPersistencyService;
import org.condast.commons.persistence.service.IPersistenceService;

public class Dispatcher extends AbstractPersistencyService implements ILoginProvider, IPersistenceService{

	//Needs to be the same as in the persistence.xml file
	private static final String S_COLLIN_SERVICE_ID = "org.collin.service"; 
	private static final String S_COLLIN_SERVICE = "Collin Service"; 

	private static Dispatcher service = new Dispatcher();
	
	private  Set<ILoginUser> users;
	
	private Dispatcher(  ) {
		super( S_COLLIN_SERVICE_ID, S_COLLIN_SERVICE );
		users = new TreeSet<ILoginUser>();
	}

	public static Dispatcher getInstance(){
		return service;
	}
	
	public boolean isRegistered( ILoginUser user ) {
		return this.users.contains( user );
	}

	public boolean addUser( ILoginUser user ){
		boolean found = this.users.contains(user);
		if( found ) {
			return false;
		}
		this.users.add( user );
		return true;
	}
	
	public boolean removeUser( ILoginUser user ) {
		return this.users.remove( user );
	}

	public ILoginUser getUser( long id ) {
		for( ILoginUser user: this.users ) {
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
		for( ILoginUser user: this.users ) {
			if( user.getId() == loginId )
				return true;
		}
		return ( loginId <= 0);
	}

	@Override
	public ILoginUser getLoginUser(long loginId) {
		for( ILoginUser user: this.users) {
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