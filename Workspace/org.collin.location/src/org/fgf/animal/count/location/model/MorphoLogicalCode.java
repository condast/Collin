package org.fgf.animal.count.location.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.collin.core.model.ILocaleDescription;
import org.collin.core.model.IMorphoCode;
import org.collin.core.model.ILocaleDescription.DefaultDescriptions;
import org.condast.commons.persistence.def.IUpdateable;

@Entity
@EntityListeners(MorphoListener.class)
public class MorphoLogicalCode implements IMorphoCode, IUpdateable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int morphologicalCode;

	@Column( nullable=false)
	private String name;//latin name
	
	private String mainCategory;
	
	private String subcategory;
	
	private boolean searchMapGLOBE;

	private boolean searchMapIVN;

	@JoinColumn( nullable=false )
	@OneToMany( cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Map<String, LocaleDescription> descriptions;

	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Basic(optional = false)
	@Column( nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	public MorphoLogicalCode() {
		super();
	}

	public MorphoLogicalCode( int code, String name) {
		this( code, name, true, true );
	}
	
	public MorphoLogicalCode( int code, String name, boolean searchMapGlobe, boolean searchMapIVN ) {
		super();
		this.morphologicalCode = code;
		this.name = name;
		this.searchMapGLOBE = searchMapGlobe;
		this.searchMapIVN = searchMapIVN;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getUserName()
	 */
	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public String getSubcategory() {
		return subcategory;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getDescription(java.util.Locale)
	 */
	@Override
	public ILocaleDescription getDescription( Locale locale ) {
		return this.descriptions.get( locale.getDisplayName());
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getDescription(java.util.Locale)
	 */
	@Override
	public ILocaleDescription getDescription( String key ) {
		return this.descriptions.get( key );
	}

	
	@Override
	public ILocaleDescription getDescription(DefaultDescriptions key) {
		return this.descriptions.get( key.name());
	}

	
	@Override
	public void addDescription(DefaultDescriptions key, ILocaleDescription description) {
		this.descriptions.put(key.name(), (LocaleDescription) description);
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#addDescription(java.util.Locale, org.fgf.animal.count.core.model.ILocaleDescription)
	 */
	@Override
	public void addDescription( Locale locale, ILocaleDescription description) {
		this.descriptions.put( locale.getDisplayName(), (LocaleDescription) description );
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#removeDescription(java.util.Locale)
	 */
	@Override
	public void removeDescription( Locale locale) {
		this.descriptions.remove( locale.getDisplayName() );
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getMorphologicalCode()
	 */
	@Override
	public int getMorphologicalCode() {
		return morphologicalCode;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#setMorphologicalCode(long)
	 */
	@Override
	public void setMorphologicalCode(int morphologicalCode) {
		this.morphologicalCode = morphologicalCode;
	}

	@Override
	public boolean isSearchMapGLOBE() {
		return searchMapGLOBE;
	}

	@Override
	public void setSearchMapGLOBE(boolean searchMapGLOBE) {
		this.searchMapGLOBE = searchMapGLOBE;
	}

	@Override
	public boolean isSearchMapIVN() {
		return searchMapIVN;
	}

	@Override
	public void setSearchMapIVN(boolean searchMapIVN) {
		this.searchMapIVN = searchMapIVN;
	}

	@Override
	public String getSubCategory() {
		return this.subcategory;
	}

	@Override
	public Map<String, ILocaleDescription> getDescriptions() {
		Iterator<Map.Entry<String, LocaleDescription>> iterator = this.descriptions.entrySet().iterator();
		Map<String, ILocaleDescription> results = new HashMap<String, ILocaleDescription>(); 
		while( iterator.hasNext() ) {
			Map.Entry<String, LocaleDescription> entry = iterator.next();
			results.put( entry.getKey(), entry.getValue() );
		}
		return results;
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
	public int compareTo(IMorphoCode arg0) {
		return morphologicalCode - arg0.getMorphologicalCode();
	}
}