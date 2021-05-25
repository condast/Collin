package org.collin.authentication.ds;

import javax.persistence.EntityManagerFactory;

import org.collin.authentication.Activator;
import org.condast.commons.persistence.service.EntityManagerFactoryService;

/**
 * Used by Gemini Blueprint
 * @author Kees
 *
 */
public class EntityManagerService extends EntityManagerFactoryService{

	Dispatcher service = Dispatcher.getInstance();		

	public EntityManagerService() {
		super( Activator.BUNDLE_ID );
	}
		
	@Override
	public synchronized void bindEMF(EntityManagerFactory emf) {
		if( !compare( emf, BUNDLE_NAME_KEY, Activator.BUNDLE_ID))
			return;
		service.setEMF(emf);
		super.bindEMF(emf);
	}


	@Override
	public synchronized void unbindEMF(EntityManagerFactory emf) {
		if( !compare( emf, BUNDLE_NAME_KEY, Activator.BUNDLE_ID))
			return;
		service.disconnect();
		super.unbindEMF(emf);
	}
}