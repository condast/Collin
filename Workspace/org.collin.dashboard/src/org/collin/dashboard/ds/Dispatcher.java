package org.collin.dashboard.ds;

import org.condast.commons.authentication.core.AbstractLoginClient;
import org.eclipse.swt.widgets.Composite;

/**
 * The volunteer who has access to the data
 * @author Kees
 *
 */
public class Dispatcher extends AbstractLoginClient{

	private static Dispatcher dispatcher = new Dispatcher();
	
	private Composite main; 

	public static Dispatcher getInstance(){
		return dispatcher;
	}
	
	private Dispatcher() {
		super();
	}

	public Composite getMainComposite() {
		return main;
	}
	
	public void setMainComposite( Composite main) {
		this.main = main;
	}

	@Override
	public void logout(long loginId, long token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logoutRequest() {
		// TODO Auto-generated method stub
		
	}
}
