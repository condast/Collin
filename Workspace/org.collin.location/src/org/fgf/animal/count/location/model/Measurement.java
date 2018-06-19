package org.fgf.animal.count.location.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IWaterAnimal;

@Entity
public class Measurement implements IMeasurement{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	private Batch batch;

	private int amount;
		
	@JoinColumn(name="WATERANIMAL_ID", nullable=false)
	@OneToOne
	private WaterAnimal waterAnimal;

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
		
	public Measurement() {
		super();
	}

	public Measurement( IBatch batch, IWaterAnimal wa, int amount) {
		super();
		this.batch = (Batch) batch;
		this.amount = amount;
		this.waterAnimal = (WaterAnimal) wa;
		this.createDate = Calendar.getInstance().getTime();
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IMeasurement#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	@Override
	public WaterAnimal getWaterAnimal() {
		return waterAnimal;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IMeasurement#getAmount()
	 */
	@Override
	public int getAmount() {
		return amount;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IMeasurement#getCreateDate()
	 */
	@Override
	public Date getCreateDate() {
		return createDate;
	}
}
