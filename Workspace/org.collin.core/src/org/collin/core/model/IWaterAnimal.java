package org.collin.core.model;

import org.collin.core.model.IWaterTypes.TypeOfWater;

public interface IWaterAnimal {

	long getId();

	double getLength();

	void setLength(double length);

	int compareTo(IWaterAnimal arg0);

	IMorphoCode getMorphologicalCode();

	void setMorphologicalCode(IMorphoCode morphologicalCode);

	boolean isCompositeLegs();

	void setCompositeLegs(boolean compositeLegs);

	boolean isWormLike();

	void setWormLike(boolean wormLike);

	void setMacroFauna(boolean macroFauna);

	boolean isMacroFauna();

	void setTailExtensions(int tailExtensions);

	int getTailExtensions();

	void setLegs(int legs);

	boolean isSurface();

	void setSnail(boolean snail);

	void setSurface(boolean surface);

	int getLegs();

	boolean isSnail();

	int getQualityIndicator();

	TypeOfWater getTypicalTypeOfWater();

	IWaterTypes.WaterFlow getTypicalWaterFlow();

	//TypeOfWater getTypeOfWater();
} 
