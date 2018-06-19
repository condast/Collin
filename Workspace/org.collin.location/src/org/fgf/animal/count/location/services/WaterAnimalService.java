package org.fgf.animal.count.location.services;

import javax.persistence.TypedQuery;

import org.collin.core.model.IMorphoCode;
import org.collin.core.model.IWaterAnimal;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;
import org.fgf.animal.count.location.model.WaterAnimal;

public class WaterAnimalService extends AbstractEntityService<WaterAnimal>{

	String SELECT_MORPHO = " SELECT wa FROM WaterAnimal wa JOIN wa.morphologicalCode mc WHERE mc.morphologicalCode=:morpho";

	public WaterAnimalService( IPersistenceService service ) {
		super( WaterAnimal.class, service );
	}

	public IWaterAnimal create( long morpho) {
		MorphologicalService ms = new MorphologicalService( super.getService() );
		IMorphoCode mc = ms.find(morpho);
		WaterAnimal animal = new WaterAnimal( mc );
		super.create(animal);
		return animal;
	}

	public IWaterAnimal get( long morpho ) {
		TypedQuery<WaterAnimal> query = super.getTypedQuery( SELECT_MORPHO );
		query.setParameter("morpho", morpho );
		return query.getSingleResult();
	}

	
}
