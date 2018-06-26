package org.collin.core.authentication;

import org.condast.commons.authentication.core.ILoginUser;

public class AuthenticationUtils {

	/**
	 * returns true if the user has administrative privileges
	 * @param userName
	 * @param token
	 * @return
	 */
	public static boolean isAdmin( String userName, long token ) {
		return ( ILoginUser.S_ADMIN.equals(userName)) && ILoginUser.AdminToken == token;
	}

}
