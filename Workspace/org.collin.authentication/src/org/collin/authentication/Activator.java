package org.collin.authentication;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public static String BUNDLE_ID = "org.collin.authentication";
    
    private static BundleContext defaultContext;
 
	public void start(BundleContext context) throws Exception {
      defaultContext = context;
    }

    public void stop(BundleContext context) throws Exception {
    }
        
	public static BundleContext getDefault(){
    	return defaultContext;
    }
}