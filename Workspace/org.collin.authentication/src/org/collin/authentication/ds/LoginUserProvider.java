package org.collin.authentication.ds;

import java.util.Collection;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import org.collin.authentication.services.LoginService;
import org.condast.commons.authentication.core.IAuthenticationListener;
import org.condast.commons.authentication.core.ILoginProvider;
import org.condast.commons.authentication.user.ILoginUser;
import org.osgi.service.component.annotations.Component;

@Component( name="org.collin.authentication.provider")
public class LoginUserProvider implements ILoginProvider {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	@Override
	public void addAuthenticationListener(IAuthenticationListener listener) {
		dispatcher.addAuthenticationListener(listener);
	}

	@Override
	public void removeAuthenticationListener(IAuthenticationListener listener) {
		dispatcher.removeAuthenticationListener(listener);
	}

	@Override
	public boolean isRegistered( long loginId) {
		return dispatcher.isRegistered( loginId );
	}
	
	@Override
	public boolean isLoggedIn(long loginId) {
		return dispatcher.isLoggedIn(loginId);
	}

	@Override
	public ILoginUser getLoginUser( long loginId, long token) {
		return dispatcher.getLoginUser( loginId, token );
	}
	
	@Override
	public Map<Long, String> getUserNames( Collection<Long> userIds ){
		LoginService service = new LoginService( dispatcher );
		return service.getUserNames(userIds);
	}

	@Override
	public void logoutRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout(long loginId, long token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CallbackHandler createCallbackHandler() {
		// TODO Auto-generated method stub
		return null;
	}
}
