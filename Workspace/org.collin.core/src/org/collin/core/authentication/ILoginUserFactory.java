package org.collin.core.authentication;

public interface ILoginUserFactory {
	
	public ILoginUser registerUser( String userName, String password, String email );

	public void logoff( ILoginUser officer );
	
	public void unregisterUser( ILoginUser officer );

}
