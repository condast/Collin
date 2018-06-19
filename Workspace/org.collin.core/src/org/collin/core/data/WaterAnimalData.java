package org.collin.core.data;

import org.collin.core.model.IMorphoCode;
import org.collin.core.model.IWaterAnimal;
import org.collin.core.model.IWaterTypes;
import org.collin.core.model.IWaterTypes.TypeOfWater;

public class WaterAnimalData implements IWaterAnimal {

	private long id;

	private IMorphoCode morphologicalCode;

	private double length;

	private boolean compositeLegs;

	private boolean wormLike;

	private boolean snail;

	private boolean surface;

	private int legs;

	private int tailExtensions;

	private boolean macroFauna;

	private int typicalWaterType;

	private int typicalWaterFlow;

	private int qualityIndicator;

	public WaterAnimalData() {
		super();
		this.macroFauna = false;
	}

	public WaterAnimalData( IWaterAnimal animal ) {
		this();
		this.morphologicalCode = MorphoCode.create( animal.getMorphologicalCode());
		this.id = animal.getId();
		this.length = animal.getLength();
		this.compositeLegs = animal.isCompositeLegs();
		this.wormLike = animal.isWormLike();
		this.snail = animal.isSnail();
		this.surface = animal.isSurface();
		this.legs = animal.getLegs();
		this.tailExtensions = animal.getTailExtensions();
		this.macroFauna = animal.isMacroFauna();
		this.typicalWaterFlow = animal.getTypicalWaterFlow().getIndex();
		this.typicalWaterType = animal.getTypicalTypeOfWater().getIndex();
		this.qualityIndicator = animal.getQualityIndicator();
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getMorphologicalCode()
	 */
	@Override
	public IMorphoCode getMorphologicalCode() {
		return morphologicalCode;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#setMorphologicalCode(long)
	 */
	@Override
	public void setMorphologicalCode( IMorphoCode morphologicalCode) {
		this.morphologicalCode = morphologicalCode;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getLength()
	 */
	@Override
	public double getLength() {
		return length;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#setLength(double)
	 */
	@Override
	public void setLength(double length) {
		this.length = length;
	}

	@Override
	public boolean isCompositeLegs() {
		return compositeLegs;
	}

	@Override
	public void setCompositeLegs(boolean compositeLegs) {
		this.compositeLegs = compositeLegs;
	}

	@Override
	public boolean isWormLike() {
		return wormLike;
	}

	@Override
	public void setWormLike(boolean wormLike) {
		this.wormLike = wormLike;
	}

	@Override
	public boolean isSnail() {
		return snail;
	}

	@Override
	public void setSnail(boolean snail) {
		this.snail = snail;
	}

	@Override
	public boolean isSurface() {
		return surface;
	}

	@Override
	public void setSurface(boolean surface) {
		this.surface = surface;
	}

	@Override
	public int getLegs() {
		return legs;
	}

	@Override
	public void setLegs(int legs) {
		this.legs = legs;
	}

	@Override
	public int getTailExtensions() {
		return tailExtensions;
	}

	@Override
	public void setTailExtensions(int tailExtensions) {
		this.tailExtensions = tailExtensions;
	}

	@Override
	public boolean isMacroFauna() {
		return macroFauna;
	}

	@Override
	public void setMacroFauna(boolean macroFauna) {
		this.macroFauna = macroFauna;
	}

	@Override
	public TypeOfWater getTypicalTypeOfWater() {
		return TypeOfWater.getTypeOfWater( this.typicalWaterType);
	}

	@Override
	public IWaterTypes.WaterFlow  getTypicalWaterFlow() {
		return IWaterTypes.WaterFlow .getWaterFlow( typicalWaterFlow );
	}

	@Override
	public int getQualityIndicator() {
		return qualityIndicator;
	}

	@Override
	public int compareTo(IWaterAnimal arg0) {
		return this.morphologicalCode.compareTo( arg0.getMorphologicalCode());
	}
	
	public static IWaterAnimal create( IWaterAnimal wateranimal ) {
		if( wateranimal == null )
			return null;
		IWaterAnimal mmnt = new WaterAnimalData(wateranimal);
		return mmnt;
	}

}
