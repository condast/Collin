package org.fgf.animal.count.location.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.collin.core.authentication.ILoginUser;
import org.collin.core.model.IBatch;
import org.collin.core.model.IMeasurement;
import org.collin.core.model.IMeasurementLocation;
import org.condast.commons.Utils;
import org.condast.commons.data.latlng.ILocation;

@Entity
public class Batch implements IBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private long userid;
		
	@JoinColumn(name="LOCATION_ID", nullable=false)
	@OneToOne
	private Location location;
	
	@OneToMany( mappedBy="batch", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Measurement> measurements;
	
	private long distance;

	private int plants;
	
	private double secchi_depth;
	
	private double mudthickness;
	
	@Basic(optional = true)
	private String description;

	@Basic(optional = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date measurementDate;

	private transient ILoginUser user;
	
	public Batch() {
		super();
		measurements = new ArrayList<>();
	}

	protected Batch( ILoginUser user, ILocation location, Date date, String description, double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater) {
		this( user, location, date, DEFAULT_RANGE,description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater  ); 
	}
	
	public Batch( ILoginUser user, ILocation location, Date date, long range, String description, double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater) {
		this( user, location, date, new ArrayList<Measurement>(), range, description, secchiDepth, mudThickness, plantsOnWater, plantsUnderwater );
	}

	public Batch( ILoginUser user, ILocation location, Date date, Collection<Measurement> measurements, long range, String description, double secchiDepth, double mudThickness, boolean plantsOnWater, boolean plantsUnderwater) {
		super();
		this.location = (Location) location;
		this.userid = ( user == null )?0: user.getId();
		this.user = user;
		this.measurements = measurements;
		this.distance = range;
		this.measurements = new ArrayList<>( measurements );
		this.description = description;
		this.secchi_depth = secchiDepth;
		this.mudthickness = mudThickness;
		this.plants = PlantGrowth.setPlantGrowthIndex(plantsOnWater, plantsUnderwater);
		this.measurementDate = date;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getUserId() {
		return userid;
	}

	@Override
	public ILoginUser getLoginUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getUserName()
	 */
	@Override
	public String getUserName() {
		return (user == null )? null: user.getUserName();
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getLocation()
	 */
	@Override
	public IMeasurementLocation getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getMeasurements()
	 */
	@Override
	public IMeasurement[] getMeasurements() {
		return this.measurements.toArray( new IMeasurement[ this.measurements.size()]);
	}
	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#addMeasurement(org.fgf.animal.count.location.model.Measurement)
	 */
	@Override
	public void addMeasurement( IMeasurement measurement ) {
		if( measurement == null )
			return;
		this.measurements.add( (Measurement) measurement );
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#removeMeasurement(org.fgf.animal.count.core.model.IMeasurement)
	 */
	@Override
	public void removeMeasurement( IMeasurement measurement ) {
		this.measurements.remove( measurement );
	}

	long getRange() {
		return distance;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getSecchiDepth()
	 */
	@Override
	public double getSecchiDepth() {
		return  secchi_depth;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#setSecchiDepth(double)
	 */
	@Override
	public void setSecchiDepth( double depth) {
		this.secchi_depth = depth;
	}

	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getMudThickness()
	 */
	@Override
	public double getMudThickness() {
		return this.mudthickness;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#setMudThickness(double)
	 */
	@Override
	public void setMudThickness(double thickness) {
		this.mudthickness = thickness;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getPlantGrowth()
	 */
	@Override
	public int getPlantGrowth() {
		return this.plants;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#addPlantGrowth(org.fgf.animal.count.location.model.Batch.PlantGrowth)
	 */
	@Override
	public void addPlantGrowth( PlantGrowth plantGrowth) {
		this.plants |= plantGrowth.getIndex();
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#removePlantGrowth(org.fgf.animal.count.location.model.Batch.PlantGrowth)
	 */
	@Override
	public void removePlantGrowth( PlantGrowth plantGrowth) {
		int mask = Integer.MAX_VALUE;
		mask ^= plantGrowth.getIndex();
		this.plants &= mask;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getMeasurementDate()
	 */
	@Override
	public Date getMeasurementDate() {
		return this.measurementDate;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#setMeasurementDate(java.util.Date)
	 */
	@Override
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}
		
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getCreateDate()
	 */
	@Override
	public Date getCreateDate() {
		if( Utils.assertNull(this.measurements))
			return null;
		Date createDate = Calendar.getInstance().getTime();
		for( IMeasurement measurement: measurements ) {
			if( createDate.before( measurement.getCreateDate() )){
				createDate = measurement.getCreateDate();
				this.id = measurement.getId();
			}
		}	
		return createDate;
	}
	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getChangeDate()
	 */
	@Override
	public Date getChangeDate() {
		if( Utils.assertNull(this.measurements))
			return null;
		Date changeDate = Calendar.getInstance().getTime();
		for( IMeasurement measurement: measurements ) {
			if( changeDate.after( measurement.getCreateDate() )) 
				changeDate = measurement.getCreateDate();
			
		}
		return changeDate;
	}
	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#size()
	 */
	@Override
	public int size() {
		return Utils.assertNull( this.measurements)?0: this.measurements.size();
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.IBatch#getTotalAnimals()
	 */
	@Override
	public int getTotalAnimals() {
		int result = 0;
		if( Utils.assertNull(this.measurements))
			return result;
		for( IMeasurement measurement: measurements ) {
			result += measurement.getAmount();	
		}
		return result;
	}	
}
