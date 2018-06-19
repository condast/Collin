package org.fgf.animal.count.location.services;

import javax.persistence.TypedQuery;

import org.collin.core.model.IWaterTypes;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;
import org.fgf.animal.count.location.model.WaterTypes;

public class WaterTypesService extends AbstractEntityService<WaterTypes>{

	private static final String S_QUERY_FIND_TYPE = "SELECT wt FROM WaterTypes wt WHERE wt.waterType= :watertype";

	public WaterTypesService( IPersistenceService service ) {
		super( WaterTypes.class, service );
	}

	public IWaterTypes create( ) {
		WaterTypes animal = new WaterTypes();
		super.create(animal);
		return animal;
	}	
	
	public IWaterTypes find( IWaterTypes.TypeOfWater type ) {
		TypedQuery<WaterTypes> query = super.getTypedQuery( S_QUERY_FIND_TYPE );
		query.setParameter("watertype",type.getIndex() );
		IWaterTypes result = query.getSingleResult();
		return result;
	}
}
