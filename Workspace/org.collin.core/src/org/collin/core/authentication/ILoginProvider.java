package org.collin.core.authentication;

import java.util.Collection;
import java.util.Map;

public interface ILoginProvider {

	/**
	 * Returns true if a login user is registered
	 * @return
	 */
	public boolean isRegistered( long loginId );

	/**
	 * Returns true if a login user is logged in
	 * @return
	 */
	public boolean isLoggedIn( long loginId );

	/**
	 * Get the login user
	 * @return
	 */
	public ILoginUser getLoginUser( long loginId );

	/**
	 * Get the user names for the given user ids
	 * @param userIds
	 * @return
	 */
	public Map<Long, String> getUserNames( Collection<Long> userIds); 
}
