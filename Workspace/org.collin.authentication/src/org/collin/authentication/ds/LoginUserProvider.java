package org.collin.authentication.ds;

import java.util.Collection;
import java.util.Map;

import org.collin.authentication.services.LoginService;
import org.collin.core.authentication.ILoginProvider;
import org.collin.core.authentication.ILoginUser;
import org.osgi.service.component.annotations.Component;

@Component( name="org.collin.authentication.factory")
public class LoginUserProvider implements ILoginProvider {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
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
