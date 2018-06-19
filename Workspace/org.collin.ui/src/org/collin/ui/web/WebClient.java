package org.collin.ui.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.config.Config;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.messaging.http.ResponseEvent;

public class WebClient extends AbstractHttpRequest{

	public static final String S_ARNAC_URL = "http://localhost:10080/arnac/rest/";
	//public static final String S_ARNAC_URL = "http://www.condast.com:8080/arnac/rest/";

	public enum Requests{
		FIELD,
		WAYPOINTS,
		UPDATE,
		POSITION,
		OPTIONS,
		DEBUG,
		LOG,
		NMEA;

		@Override
		public String toString() {
			String str = null;
			switch( this ){
			case POSITION:
				str = "update";
				break;
			default:
				str = this.name().toLowerCase();
				break;
			}
			return str;
		}
		
		public static Requests getRequest( URL url ){
			for( Requests request: values()){
				String path = url.toExternalForm();
				if( path.contains(request.toString()))
					return request;
			}
			return null;
		}
	}
	
	private enum Parameters{
		ID,
		TOKEN,
		MSG,
		LA,
		LO,
		B,
		S,
		NMEA;
		
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}		
	}

	private Config config = new Config();
	
	private String name;
	private int token;
	
	private java.util.logging.Logger jlogger = java.util.logging.Logger.getLogger( this.getClass().getName());

	public WebClient(String name, int token) {
		super();
		this.name = name;
		this.token = token;
	}

	public boolean sendHttp( Requests request ) {
		Map<String, String> params = new HashMap<String, String>();
		return sendHttp(request, params);
	}

	boolean sendHttp( Requests request, String message ) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(Parameters.MSG.toString(), message);
		return sendHttp(request, params);
	}


	protected boolean sendHttp( Requests request, Map<String, String> params ) {
		try {
			String url = S_ARNAC_URL + request.toString();
			params.put(Parameters.ID.toString(), name);
			params.put(Parameters.TOKEN.toString(), String.valueOf( token));
			super.sendGet(url, params);
			return true;
		} catch (Exception e) {
			jlogger.severe( e.getMessage() );
		}
		return false;
	}

	@Override
	protected String onHandleResponse(URL url, int responsecode, BufferedReader reader) throws IOException {
		try{
			WebClient.Requests request = WebClient.Requests.getRequest(url);
			ResponseEvent response= new ResponseEvent( this, request.name(), reader );
			notifyListeners( response );
			return Responses.OK.name();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return Responses.BAD.name();
	}

}
