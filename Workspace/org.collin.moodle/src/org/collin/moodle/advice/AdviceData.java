package org.collin.moodle.advice;

import java.util.Map;


public class AdviceData {

	public static final String S_EXAMPLE = "{\"userid\":\"1368\",\"token\":\"1\",\"courseid\":\"329\",\"cmid\":\"23568\",\"progress\":"+ 
	"{\"23565\":false,\"23567\":true,\"23568\":true,\"23575\":true,\"22468\":true,\"23427\":false,\"22453\":false," +
	"\"23405\":false,\"23396\":false,\"23398\":false,\"23412\":false,\"22470\":false,\"23572\":false,\"22474\":false," +
	"\"23428\":false,\"22455\":false,\"23416\":true,\"22477\":true,\"23417\":false,\"23422\":false,\"22459\":false,"+
	"\"23420\":false,\"22481\":true,\"22480\":false,\"23461\":false,\"23663\":false,\"23573\":false,\"22487\":true,"+
	"\"23467\":false,\"22489\":false,\"22491\":false,\"23466\":false,\"23574\":false,\"23498\":false}}"; 

	private String userid;
	private String token;
	private String courseid;
	private String cmid;
	
	private Map<Long, Boolean> progress;

	public AdviceData(long userid, String token, long courseid, long cmid, Map<Long, Boolean> progress) {
		super();
		this.userid = String.valueOf( userid );
		this.token = token;
		this.courseid = String.valueOf( courseid );
		this.cmid = String.valueOf(cmid);
		this.progress = progress;
	}
	
	public long getUserid() {
		return Long.parseLong( userid );
	}

	public String getToken() {
		return token;
	}

	public long getCourseid() {
		return Long.parseLong(  courseid );
	}

	public long getCmid() {
		return Long.parseLong( cmid );
	}


	public int getProgress(){
		double counter = 0;
		for( Boolean succeed: this.progress.values()) {
			if( succeed)
				counter++;
		}
		return (int)( 100 * counter/this.progress.size() ); 
	}
}
