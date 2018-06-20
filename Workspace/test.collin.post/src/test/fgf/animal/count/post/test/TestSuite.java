package test.fgf.animal.count.post.test;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Logger;

import org.collin.core.data.BatchEntry;
import org.condast.commons.messaging.http.AbstractHttpRequest;
import org.condast.commons.test.core.AbstractTestSuite;
import org.condast.commons.test.core.ITestEvent;

import test.fgf.animal.count.post.Activator;

public class TestSuite extends AbstractTestSuite<Object>{

	public static final String S_REOURCE_LOCATION = "/resources/measurements.json";
	
	public static final String S_CONTEXT = "http://www.waterdiertjes.nl:8080/wd/";
	public static final String S_HTTPS_CONTEXT = "https://www.waterdiertjes.nl:8443/wd/location/measurement/addall";
	public static final String S_LOCAL_CONTEXT = "http://localhost:10080/wd";
	public static final String S_TEST_CONTEXT = "http://waterdiertjes.condast.com:8080/wd";
	
	public static final int USER_ID = 51;
	
	public enum Tests{
		CREATE_BATCH,
		REGISTER,
		ADD_LOCATION,
		ADD_ALL,
		FIND,
		FIND_GROUPS,
		GET_ALL,
		TEST,
		STAT_TOTAL;
	}
	
	private long userId, token;
	private String context;
	
	private Logger logger = Logger.getLogger( this.getClass().getName());
	
	
	private WebClient client;

	public TestSuite() {
		super( Activator.S_BUNDLE_ID, null);
		this.context = S_TEST_CONTEXT;
	}


	@Override
	protected void testSuite() throws Exception {
		Tests test = Tests.ADD_ALL;
		try {
			switch( test ) { 
			case CREATE_BATCH:
				createBatchEntry();
				break;
			case REGISTER:
				register("keesp", "hoi");
				break;
			case ADD_LOCATION:
				addLocation("Bergumermeer", 53.207064, 6.028483 );
				break;
			case ADD_ALL:
				login("keesp", "hoi");
				addAll( userId, token, 4351, "01-06-2018:13-48-20" );
				break;
			case FIND:
				login("keesp", "hoi");
				find( userId, token, 4351 );
				break;
			case FIND_GROUPS:
				login("keesp", "hoi");
				findGroups( userId, token, 1, 5 );
				break;
			case GET_ALL:
				//login("keesp", "hoi");
				getAll( userId, token );
				break;
			case STAT_TOTAL:
				login("keesp", "hoi");
				statTotal( userId, token );
				break;
			case TEST:
				//login("keesp", "hoi");
				testQuery();
				break;
			default:
				//addAll();
				break;
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		finally {
			logoff();
		}
	}

	private void register( String name, String password) throws Exception {
		String path = context + "/auth/register?name=" + name + "&password=" + password;
		client = new WebClient( path );
		client.sendGet(path);
		Gson gson = new Gson();
		long[] arr = gson.fromJson(client.response, long[].class);
		userId = arr[0];
		token = arr[1];
		logger.info("Registered, user is " + userId + " token is " + token );
	}

	private void login( String name, String password ) throws Exception {
		String path = context + "/auth/login?name=" + name + "&password=" + password;
		client = new WebClient( path );
		client.sendGet(path);
		Gson gson = new Gson();
		long[] arr = gson.fromJson(client.response, long[].class);
		userId = arr[0];
		token = arr[1];
		logger.info("Logged in; user is " + userId + " token is " + token );
	}

	private void logoff( ) throws Exception {
		String path = context + "/auth/logoff?userid=" + userId;
		client = new WebClient( path );
		client.sendGet(path);
		logger.info("logged off: " + client.response );
	}

	private void addLocation( String name, double latitude, double longitude) throws Exception {
		String path = context + "/location/add?name=" + name + "&latitude=" + latitude + "&longitude=" + longitude + "&watertype=1";
		client = new WebClient( context );
		client.sendGet(path);
	}
	
	private void createBatchEntry() {
		BatchEntry entry = new BatchEntry( 80.3, 12.5, true, true, "rommelig");
		entry.addMeasurement(40100, 12);
		entry.addMeasurement(10700, 10);
		entry.addMeasurement(20110, 15);
		entry.addMeasurement(40110, 122);
		entry.addMeasurement(30101, 134);
		Gson gson = new Gson();
		logger.info( gson.toJson(entry, BatchEntry.class));
		
	}
	
	private void addAll( long userid, long token, long locationId, String date) {
		String path = context + "/location/measurement/addall?userid=" + userId + 
				"&token=" + token + "&locationid=" + locationId + "&date=" + date;
		client = new WebClient( path );
		StringBuffer buffer = new StringBuffer();
		Scanner scanner = null;
		try {
			scanner = new Scanner( Activator.class.getResourceAsStream( S_REOURCE_LOCATION ));
			while( scanner.hasNextLine() )
				buffer.append( scanner.nextLine());
			String snd = URLEncoder.encode(buffer.toString(), "UTF-8");
			client.sendPost(path, buffer.toString(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			scanner.close();
		}
	}

	private void find( long userid, long token, long locationId) throws Exception {
		String path = context + "/location/measurement/find?userid=" + userId + "&token=" + token + "&locationid=" + locationId;
		client = new WebClient( path );
		client.sendGet(path);
		logger.info( client.getResponse());
	}

	private void findGroups( long userid, long token, long locationId, int range) throws Exception {
		String path = context + "/location/measurement/findgroups?userid=" + userId + "&token=" + token + 
				"&locationid=" + locationId + "&range=" + range;
		client = new WebClient( path );
		client.sendGet(path);
		logger.info( client.getResponse());
	}

	private void getAll( long userid, long token ) throws Exception {
		String path = context + "/location/getall";
		client = new WebClient( path );
		client.sendGet(path);
		logger.info( client.getResponse());
	}

	private void statTotal( long userid, long token) throws Exception {
		String path = context + "/location/stat/total?userid=" + userId + "&token=" + token;
		client = new WebClient( path );
		client.sendGet(path);
		logger.info( client.getResponse());
	}

	private void testQuery( ) throws Exception {
		String path = context + "/location/test?locationid=1";
		client = new WebClient( path );
		client.sendGet(path);
		logger.info( client.getResponse());
	}

	private class WebClient extends AbstractHttpRequest{

		private String response;
		
		public WebClient( String context) {
			super(context);
			logger.info("\nPROCESSING: " + context );
		}

		@Override
		public String getContextPath() {
			return super.getContextPath();
		}

		public String getResponse() {
			return response;
		}

		@Override
		protected String onHandleResponse(URL url, int responsecode, BufferedReader reader) throws IOException {
			Scanner scanner = new Scanner( reader );
			StringBuffer buffer = new StringBuffer();
			try {
				while( scanner.hasNextLine())
					buffer.append(scanner.nextLine());
			}
			finally {
				scanner.close();
			}
			StringBuffer msg = new StringBuffer();
			msg.append( "URL: " + url ); 
			msg.append( "\n\tRESPONSE CODE: " + getHttpStatus( responsecode).name() + 
					"(" + responsecode + ")" );	
			response = buffer.toString();
			msg.append( "\n\t" + response );
			logger.info( msg.toString() );	
			return response;
		}
	}

	@Override
	protected void onPrepare(ITestEvent<Object> event) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onPerform(ITestEvent<Object> event) {
		// TODO Auto-generated method stub
		
	}
}
