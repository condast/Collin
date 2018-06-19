package org.collin.authentication.ds;

import javax.persistence.EntityManagerFactory;

import org.collin.authentication.Activator;
import org.condast.commons.persistence.service.AbstractEntityManagerService;
/**
 * Used by Gemini Blueprint
 * @author Kees
 *
 */
public class EntityManagerService extends AbstractEntityManagerService{

	Dispatcher service = Dispatcher.getInstance();		

	public EntityManagerService() {
		super( Activator.BUNDLE_ID );
	}
		
	@Override
	protected void onBindFactory(EntityManagerFactory factory) {
		service.setEMF(factory);
	}


	@Override
	protected void onUnbindFactory(EntityManagerFactory factory) {
		service.disconnect();
	}
}