package org.fgf.animal.count.location.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.collin.core.model.IMorphoCode;
import org.collin.core.model.IWaterAnimal;
import org.collin.core.model.IWaterTypes;
import org.collin.core.model.IWaterTypes.TypeOfWater;
import org.condast.commons.persistence.def.IUpdateable;

@Entity
public class WaterAnimal implements IWaterAnimal, IUpdateable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private MorphoLogicalCode morphologicalCode;

	private double length;

	private boolean compositeLegs;

	private boolean wormLike;

	private boolean snail;

	private boolean surface;

	private int legs;

	private int tailExtensions;

	private boolean macroFauna;
	
	private int typicalWaterFlow;

	private int qualityIndicator;

	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public WaterAnimal() {
		super();
		this.macroFauna = false;
	}

	public WaterAnimal( IMorphoCode mc ) {
		this();
		this.morphologicalCode = (MorphoLogicalCode) mc;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
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
		this.morphologicalCode = (MorphoLogicalCode) morphologicalCode;
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
		return TypeOfWater.getTypeOfWater( this.typicalWaterFlow);
	}

	@Override
	public IWaterTypes.WaterFlow  getTypicalWaterFlow() {
		return IWaterTypes.WaterFlow.getWaterFlow( this.typicalWaterFlow);
	}

	@Override
	public int getQualityIndicator() {
		return qualityIndicator;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date create) {
		this.createDate = create;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date update) {
		this.updateDate = update;
	}

	@Override
	public int compareTo(IWaterAnimal arg0) {
		return this.morphologicalCode.compareTo( arg0.getMorphologicalCode());
	}
}
