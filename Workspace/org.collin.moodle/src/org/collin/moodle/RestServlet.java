package org.collin.moodle;

import javax.servlet.Servlet;
import javax.ws.rs.ApplicationPath;

import org.collin.moodle.rest.PushResource;
import org.collin.moodle.rest.RESTStudent;
import org.condast.commons.messaging.http.AbstractServletWrapper;
import org.condast.commons.messaging.rest.CorsFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestServlet extends AbstractServletWrapper {

	//Same as alias in plugin.xml
	public static final String S_CONTEXT_PATH = "moodle";

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
			register( RESTStudent.class );
			register( PushResource.class );
		}
	}
}
