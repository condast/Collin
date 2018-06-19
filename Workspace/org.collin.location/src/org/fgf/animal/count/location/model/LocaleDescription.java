package org.fgf.animal.count.location.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.collin.core.model.ILocaleDescription;
import org.collin.core.model.IMorphoCode;

@Entity
public class LocaleDescription implements ILocaleDescription{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@JoinColumn( nullable=false)
	@ManyToOne
	private MorphoLogicalCode morpho;

	@Column( nullable=false)
	private String name;

	@Column( nullable=false)
	private String alternative;

	@Column( nullable=true)
	private String description;
	
	public LocaleDescription() {
		super();
	}

	public LocaleDescription( IMorphoCode morpho, String name, String info) {
		this( morpho, name, null, info );
	}
	
	public LocaleDescription( IMorphoCode morpho, String name, String alternative, String info) {
		this();
		this.morpho = (MorphoLogicalCode) morpho;
		this.name = name;
		this.alternative = alternative;
		this.description = info;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getId()
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getUserName()
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getName()
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#setName(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getAlternative()
	 */
	@Override
	public String getAlternative() {
		return alternative;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#setAlternative(java.lang.String)
	 */
	@Override
	public void setAlternative(String name) {
		this.alternative = name;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getDescription()
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#setDescription(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ILocaleDescription#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
