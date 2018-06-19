package org.fgf.animal.count.location.services;

import javax.persistence.TypedQuery;

import org.collin.core.model.IMorphoCode;
import org.condast.commons.persistence.service.AbstractEntityService;
import org.condast.commons.persistence.service.IPersistenceService;
import org.fgf.animal.count.location.model.MorphoLogicalCode;

public class MorphologicalService extends AbstractEntityService<MorphoLogicalCode>{

	private static final String S_QUERY_GET = "SELECT mc FROM MorphoLogicalCode mc WHERE mc.morphologicalCode= :morpho";

	public MorphologicalService( IPersistenceService service ) {
		super( MorphoLogicalCode.class, service );
	}

	public IMorphoCode create( int code, String name, boolean searchMapGlobe, boolean searchMapIVN ) {
		MorphoLogicalCode morpho = new MorphoLogicalCode( code, name, searchMapGlobe, searchMapIVN );
		super.create(morpho);
		return morpho;
	}
	
	public IMorphoCode get( long morpho ) {
		TypedQuery<MorphoLogicalCode> query = super.getTypedQuery( S_QUERY_GET );
		query.setParameter("morpho", morpho );
		MorphoLogicalCode result = query.getSingleResult();
		return result;
	}
}
