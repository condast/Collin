package org.fgf.animal.count.location.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.collin.core.model.IWaterTypes;

@Entity
public class WaterTypes implements IWaterTypes {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column( nullable=true)
	private String name;
	
	@Column( nullable=true)
	private String property;
	
	private int waterType;

	private int waterFlow;

	private int shape;

	public WaterTypes() {
		super();
	}

	public WaterTypes(long id, String name, String property, int waterType, int shape) {
		super();
		this.id = id;
		this.name = name;
		this.property = property;
		this.waterType = waterType;
		this.shape = shape;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IWaterTypes#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IWaterTypes#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IWaterTypes#getProperty()
	 */
	@Override
	public String getProperty() {
		return property;
	}

	@Override
	public TypeOfWater getWaterType() {
		return TypeOfWater.getTypeOfWater( waterType );
	}

	
	@Override
	public WaterFlow getWaterFlow() {
		return WaterFlow.getWaterFlow( this.waterFlow );
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IWaterTypes#getShape()
	 */
	@Override
	public Shapes getShape() {
		return Shapes.getShape( shape );
	}
	
	
	
	
}
