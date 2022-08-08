package org.fgf.animal.count.location.ds;

import org.condast.commons.authentication.core.ILoginProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component( name="org.fgf.animal.count.location.login.client", immediate=true)
public class LoginClient {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	@Reference( cardinality = ReferenceCardinality.MANDATORY,
			policy=ReferencePolicy.DYNAMIC)
	public void bindEnvironment( ILoginProvider provider){
		this.dispatcher.setLoginProvider(provider);
	}

	public void unbindEnvironment( ILoginProvider provider ){
		this.dispatcher.clearLoginProvider( provider);
	}
}
