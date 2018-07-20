package org.fgf.animal.count.location.ds;

import java.util.Collection;
import java.util.Map;

import org.condast.commons.Utils;
import org.condast.commons.authentication.core.IAuthenticationListener;
import org.condast.commons.authentication.core.ILoginProvider;
import org.condast.commons.authentication.user.AnonymousUser;
import org.condast.commons.authentication.user.ILoginUser;
import org.condast.commons.persistence.service.AbstractPersistencyService;
import org.condast.commons.strings.StringUtils;

public class Dispatcher extends AbstractPersistencyService implements ILoginProvider{

	//Needs to be the same as in the persistence.xml file
	private static final String S_FGF_SERVICE_ID = "org.fgf.animal.count.service"; 
	private static final String S_FGF_SERVICE = "Forget the Fish Animal Count"; 

	private static Dispatcher service = new Dispatcher();
	
	private ILoginProvider loginProvider;
	
	private Dispatcher(  ) {
		super( S_FGF_SERVICE_ID, S_FGF_SERVICE );
	}

	public static Dispatcher getInstance(){
		return service;
	}
	
	public ILoginProvider getLoginProvider() {
		return loginProvider;
	}

	public void setLoginProvider(ILoginProvider loginProvider) {
		this.loginProvider = loginProvider;
	}

	public void clearLoginProvider(ILoginProvider loginProvider) {
		this.loginProvider = null;
	}

	@Override
	public boolean isRegistered(long loginId) {
		if( this.loginProvider == null )
			return false;
		return this.loginProvider.isRegistered(loginId);
	}

	@Override
	public boolean isLoggedIn(long loginId ) {
		if( this.loginProvider == null )
			return false;
		return this.loginProvider.isLoggedIn(loginId);
	}

	/**
	 * returns true if the user is logged in and the token is correct
	 * @param loginId
	 * @param token
	 * @return
	 */
	public boolean isAuthenticated(long loginId, long token ) {
		if( this.loginProvider == null )
			return false;
		ILoginUser user = this.loginProvider.getLoginUser(loginId, token);
		return( user == null )?false: user.getToken() == token;
	}

	/**
	 * returns true if the user is logged in and the token is correct
	 * @param loginId
	 * @param token
	 * @return
	 */
	public boolean hasUser( String login, String tokenstr ) {
		if( this.loginProvider == null )
			return false;
		return ( !StringUtils.isEmpty( login) && !StringUtils.isEmpty( tokenstr ));
	}

	@Override
	public ILoginUser getLoginUser(long loginId, long token) {
		if( loginId <= 0 )
			return new AnonymousUser();
		if( this.loginProvider == null )
			return null;
		return this.loginProvider.getLoginUser(loginId, token);
	}

	@Override
	public Map<Long, String> getUserNames(Collection<Long> userIds) {
		if( Utils.assertNull( userIds))
				return null;
		if( this.loginProvider == null )
			return null;
		return this.loginProvider.getUserNames(userIds);
	}

	@Override
	public void addAuthenticationListener(IAuthenticationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAuthenticationListener(IAuthenticationListener listener) {
		// TODO Auto-generated method stub
		
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