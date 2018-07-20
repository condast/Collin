package org.collin.dashboard.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.authentication.core.AuthenticationEvent;
import org.condast.commons.authentication.core.IAuthenticationListener;
import org.condast.commons.authentication.core.ILoginProvider;
import org.condast.commons.authentication.user.ILoginUser;
import org.eclipse.swt.widgets.Composite;

/**
 * The volunteer who has access to the data
 * @author Kees
 *
 */
public class Dispatcher implements ILoginProvider{

	private static Dispatcher dispatcher = new Dispatcher();
	
	private ILoginProvider factory;
	
	private Composite main; 

	private Collection<IAuthenticationListener> listeners;

	private Collection<ILoginUser> users;

	private IAuthenticationListener listener=  new IAuthenticationListener(){

		@Override
		public void notifyLoginChanged(AuthenticationEvent event) {
			switch( event.getEvent() ) {
			case REGISTER:
			case LOGIN:
				users.add(event.getUser());
				break;
			default:
				users.remove( event.getUser());
				break;
			}
			for( IAuthenticationListener listener: listeners )
				listener.notifyLoginChanged( event );
		}	
	};

	public static Dispatcher getInstance(){
		return dispatcher;
	}
	
	private Dispatcher() {
		super();
		listeners = new ArrayList<>();
		users = new ArrayList<>();
	}

	public void addAuthenticationListener( IAuthenticationListener listener ) {
		this.listeners.add(listener);
	}

	public void removeAuthenticationListener( IAuthenticationListener listener ) {
		this.listeners.remove(listener);		
	}
	
	public void setFactory( ILoginProvider factory ){
		this.factory = factory;
		this.factory.addAuthenticationListener(listener);
	}

	public void unsetFactory( ILoginProvider factory ){
		this.factory.removeAuthenticationListener(listener);
		this.factory = null;
	}
	
	public Composite getMainComposite() {
		return main;
	}
	
	public void setMainComposite( Composite main) {
		this.main = main;
	}
	
	@Override
	public boolean isRegistered(long loginId) {
		for( ILoginUser user: this.users ) {
			if( user.getId() == loginId )
				return true;
		}
		return false;
	}

	@Override
	public boolean isLoggedIn(long loginId) {
		for( ILoginUser user: this.users ) {
			if( user.getId() == loginId )
				return true;
		}
		return false;
	}

	@Override
	public ILoginUser getLoginUser(long loginId, long token) {
		for( ILoginUser user: this.users ) {
			if( user.getId() == loginId )
				return user;
		}
		return null;
	}

	@Override
	public Map<Long, String> getUserNames(Collection<Long> userIds) {
		Map<Long, String> results = new HashMap<>();
		for( ILoginUser user: this.users ) {
			results.put( user.getId(), user.getUserName());
		}
		return results;
	}

	@Override
	public void logoutRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout(long loginId, long token) {
		// TODO Auto-generated method stub
		
	}
}
