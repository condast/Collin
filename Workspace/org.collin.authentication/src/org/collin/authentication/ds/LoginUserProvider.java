package org.collin.authentication.ds;

import java.util.Collection;
import java.util.Map;

import org.collin.authentication.services.LoginService;
import org.condast.commons.authentication.core.IAuthenticationListener;
import org.condast.commons.authentication.core.ILoginProvider;
import org.condast.commons.authentication.core.ILoginUser;
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
	public ILoginUser getLoginUser( long loginId) {
		return dispatcher.getLoginUser( loginId );
	}
	
	@Override
	public Map<Long, String> getUserNames( Collection<Long> userIds ){
		LoginService service = new LoginService( dispatcher );
		return service.getUserNames(userIds);
	}
}
