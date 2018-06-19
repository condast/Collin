package org.fgf.animal.count.location.services;

import java.util.Locale;

import org.collin.core.model.ILocaleDescription;
import org.collin.core.model.IMorphoCode;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;
import org.fgf.animal.count.location.model.LocaleDescription;

public class DescriptionService extends AbstractEntityService<LocaleDescription>{

	public DescriptionService( IPersistenceService service ) {
		super( LocaleDescription.class, service );
	}

	public ILocaleDescription create( IMorphoCode morpho, Locale locale, String name, String description ) {
		ILocaleDescription ld = new LocaleDescription( morpho, name, null, description );
		super.create((LocaleDescription) ld);
		return ld;
	}

	public ILocaleDescription create( IMorphoCode morpho, Locale locale, String name, String alternative, String description ) {
		ILocaleDescription ld = new LocaleDescription( morpho, name, alternative, description );
		super.create((LocaleDescription) ld);
		return ld;
	}
}