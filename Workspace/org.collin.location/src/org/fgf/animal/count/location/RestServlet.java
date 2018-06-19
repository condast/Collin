package org.fgf.animal.count.location;

import javax.servlet.Servlet;
import javax.ws.rs.ApplicationPath;

import org.condast.commons.messaging.http.AbstractServletWrapper;
import org.fgf.animal.count.location.rest.CorsFilter;
import org.fgf.animal.count.location.rest.LocationResource;
import org.fgf.animal.count.location.rest.MeasurementResource;
import org.fgf.animal.count.location.rest.MorphoResource;
import org.fgf.animal.count.location.rest.StatisticsResource;
import org.fgf.animal.count.location.rest.WaterAnimalResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestServlet extends AbstractServletWrapper {

	//Same as alias in plugin.xml
	public static final String S_CONTEXT_PATH = "location";

	public RestServlet() {
		super( S_CONTEXT_PATH );
	}
	
	@Override
	protected Servlet onCreateServlet(String contextPath) {
		RestApplication resourceConfig = new RestApplication();
		return new ServletContainer(resourceConfig);
	}

	@ApplicationPath(S_CONTEXT_PATH)
	private class RestApplication extends ResourceConfig {

		//Loading classes is the safest way...
		//in equinox the scanning of packages may not work
		private RestApplication() {
			register( CorsFilter.class );
			register( MorphoResource.class );
			register( LocationResource.class );
			register( WaterAnimalResource.class );
			register( MeasurementResource.class );
			register( StatisticsResource.class );
		}
	}
}
