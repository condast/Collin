package org.collin.core.authentication;

import org.condast.commons.persistence.def.IUpdateable;
import org.condast.commons.strings.StringStyler;

public interface ILoginUser extends IUpdateable, Comparable<ILoginUser>{

	public static final String S_ADMIN = "admin";
	public static final long AdminToken = -1024;
	
	public enum Attributes{
		USERNAME,
		FIRST_NAME,
		SURNAME,
		PASSWORD,
		EMAIL,
		COMMUNITY,
		TYPE;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}		
	}

	public enum Roles{
		MATCH_MAKER,
		ADMIN,
		SUPER_USER;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static String[] getItems(){
			String[] items = new String[ values().length ];
			for( int i=0; i< values().length; i++ ){
				items[i] = values()[i].toString();
			}
			return items;
		}	
	}
	
	String getUserName();

	String getEmail();

	long getId();

	long getToken();
	
	/**
	 * returns true if the user has administrative privileges
	 * @param userName
	 * @param token
	 * @return
	 */
	public boolean isAdmin( String userName, long token );
}
