package org.collin.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.collin.dashboard.entry.MoodleTestPoint;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;


public class BasicApplication implements ApplicationConfiguration {

	private static final String S_ENTRY_POINT = "/home";
	private static final String S_MOODLE_ENTRY_POINT = "/moodle";

	public void configure(Application application) {
		application.addStyleSheet( "collin.theme", "themes/theme.css" );

		Map<String, String> properties = new HashMap<String, String>();
		properties.put(WebClient.PAGE_TITLE, "Platform HB");
		application.setOperationMode( OperationMode.SWT_COMPATIBILITY );       
		properties.put( WebClient.THEME_ID, "collin.theme" );
		application.addEntryPoint( S_ENTRY_POINT, BasicEntryPoint.class, properties);
		application.addEntryPoint( S_MOODLE_ENTRY_POINT, MoodleTestPoint.class, properties);
	}
}
