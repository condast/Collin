package org.collin.dashboard;

import java.net.URL;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String BUNDLE_ID = "org.collin.dashboard";

    public static String JAAS_RESOURCE = "data/jaas.cfg";

    private static BundleContext defaultContext;
    
	public void start(BundleContext context) throws Exception {
      defaultContext = context;
    }

    public void stop(BundleContext context) throws Exception {
    }

	public static ILoginContext createLoginContext(){
	      String jaasConfigFile = JAAS_RESOURCE;
	       URL configUrl = defaultContext.getBundle().getEntry( jaasConfigFile );
	      return LoginContextFactory.createContext( "COLLIN", configUrl );
	}
}
