package org.collin.core.model;

import java.util.Date;

public interface IMeasurement {

	long getId();

	int getAmount();

	Date getCreateDate();

	IWaterAnimal getWaterAnimal();
}