package org.collin.core.model;

import org.condast.commons.data.latlng.ILocation;
import org.condast.commons.persistence.def.IUpdateable;

public interface IMeasurementLocation extends ILocation, IUpdateable, Comparable<ILocation>{

	public IWaterTypes getWaterType();

	void setWaterType(IWaterTypes waterType);

	public double getWaterQualityIndex();

	void setWaterQualityIndex( double waterQuality);
}
