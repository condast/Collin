package org.collin.authentication.utils;

public class Utils
{

	/**
	 * returns true if the string is null or empty
	 * @param str
	 * @return
	 */
	public static final boolean isNull( String str ){
		return (( str == null) || ( str.trim().length() == 0 ));
	}
}
