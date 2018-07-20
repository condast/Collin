package org.collin.authentication.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.collin.authentication.model.Login;
import org.condast.commons.Utils;
import org.condast.commons.authentication.user.ILoginUser;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;

public class LoginService extends AbstractEntityService<Login>{

	private static final String SELECT = "SELECT ua FROM Login ua WHERE ua.userName=:userName and ua.password=:password";

	private static final String S_IDS = "ids"; 
	private static final String SQL_LOGIN_ID_QUERY = "SELECT l FROM Login l WHERE l.id in :" + S_IDS;

	public LoginService( IPersistenceService service) {
		super( Login.class, service);
	}

	public ILoginUser create( String name, String password, String email ) {
		Login login = new Login();
		login.setUserName(name);
		login.setPassword(password);
		login.setEmail(email);
		super.create(login);
		return login;
	}
	
	public ILoginUser login( String user, String password ) {
		TypedQuery<ILoginUser> query = super.getManager().createQuery( SELECT, ILoginUser.class);
		query.setParameter("userName", user);
		query.setParameter("password", password);
		Collection<ILoginUser> users = query.getResultList();
		if( Utils.assertNull( users))
			return null;
		return users.iterator().next();
	}

	public ILoginUser[] getAll() {
		Collection<Login> users = super.findAll("");
		if( Utils.assertNull( users))
			return null;
		return users.toArray( new ILoginUser[users.size()]);
	}
	
	/**
	 * Get the user names corresponding with the given user ids.
	 * @param userIds
	 * @return
	 */
	public Map<Long, String> getUserNames( Collection<Long> userIds ){
		if( Utils.assertNull(userIds ))
			return null;
		Map<Long, String> results = new HashMap<Long, String>();
		try{
			TypedQuery<Login> query = super.getTypedQuery( SQL_LOGIN_ID_QUERY );
			query.setParameter( S_IDS, userIds );
			Collection<Login> qresults = query.getResultList();
			for( Login login: qresults ) {
				results.put(login.getId(), login.getUserName());
			}
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return results;
	}
}
