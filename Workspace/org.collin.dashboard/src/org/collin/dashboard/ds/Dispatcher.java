package org.collin.dashboard.ds;

import org.collin.core.authentication.ILoginUser;
import org.collin.core.authentication.ILoginUserFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * The volunteer who has access to the data
 * @author Kees
 *
 */
public class Dispatcher{

	private static Dispatcher dispatcher = new Dispatcher();
	
	private ILoginUserFactory factory;
	
	private Composite main; 

	public static Dispatcher getInstance(){
		return dispatcher;
	}
	
	private Dispatcher() {
		super();
	}

	public void setFactory( ILoginUserFactory factory ){
		this.factory = factory;
	}

	public void unsetFactory( ILoginUserFactory factory ){
		this.factory = null;
	}
	
	public Composite getMainComposite() {
		return main;
	}
	
	public void setMainComposite( Composite main) {
		this.main = main;
	}
	
	public ILoginUser getOfficer( String userName, String password, String email ) {
		ILoginUser officer = factory.registerUser( userName, password, email );
		//manager.setSupportOfficer(officer);
		return officer;
	}
}
