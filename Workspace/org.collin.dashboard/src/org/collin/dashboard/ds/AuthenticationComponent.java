package org.collin.dashboard.ds;

import java.util.logging.Logger;

import org.condast.commons.authentication.core.ILoginProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;


@Component(
	name = AuthenticationComponent.COMPONENT_NAME
)
public class AuthenticationComponent{

	public static final String COMPONENT_NAME = "org.collin.dashboard.service.login";

	private AuthenticationDispatcher dispatcher = AuthenticationDispatcher.getInstance();
	
    private static final Logger logger = Logger.getLogger( AuthenticationComponent.class.getName());
    
	@Activate
	public void activate(){
		logger.info("Activating the " + COMPONENT_NAME);		
	}

	@Deactivate
	public void deactivate(){
		logger.info("Deactivating the" + COMPONENT_NAME);				
	}

	@Reference
	public void setFactory( ILoginProvider provider ){
		dispatcher.setLoginProvider(provider);
	}

	public void unsetFactory( ILoginProvider factory ){
		dispatcher.unsetLoginProvider(factory);
	}
}
