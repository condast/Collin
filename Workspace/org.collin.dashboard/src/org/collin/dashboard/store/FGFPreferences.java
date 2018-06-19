package org.collin.dashboard.store;

import org.collin.dashboard.Activator;
import org.condast.commons.preferences.AbstractPreferenceStore;
import org.condast.commons.preferences.IPreferenceStore;
import org.osgi.service.prefs.Preferences;

public class FGFPreferences extends AbstractPreferenceStore{

	public FGFPreferences() {
		super( Activator.BUNDLE_ID);
	}

	private enum Attributes{
		APPLICATION,
		OFFICER,
		LOGGED_IN;
	}
	
	@Override
	public boolean open() {
		return super.open();
	}
	
	public void clear() {
		if( !super.isOpen())
			return;
		setLoggedIn( Boolean.FALSE );
	}

	public boolean isLoggedin(){
		Object obj = super.getSettings( Attributes.LOGGED_IN.name() );
		return ( obj == null )? false:  (boolean)obj;	
	}
	
	public boolean setLoggedIn( boolean choice ){
		return choice;
	}

	@Override
	public String getSettings(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBoolean(String name, int position, boolean choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected IPreferenceStore<String, String> onAddChild(Preferences preferences) {
		// TODO Auto-generated method stub
		return null;
	}
}
