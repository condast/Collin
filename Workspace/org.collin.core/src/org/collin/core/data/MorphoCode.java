package org.collin.core.data;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.collin.core.model.ILocaleDescription;
import org.collin.core.model.IMorphoCode;
import org.collin.core.model.ILocaleDescription.DefaultDescriptions;
import org.condast.commons.persistence.def.IUpdateable;

public class MorphoCode implements IMorphoCode, IUpdateable {

	private int morphologicalCode;

	private String name;//latin name
	
	private String mainCategory;
	
	private String subCategory;
	
	private boolean searchMapGLOBE;

	private boolean searchMapIVN;

	private Map<String, ILocaleDescription> descriptions;

	private Date createDate;
	
	private Date updateDate;

	public MorphoCode() {
		super();
	}
	
	public MorphoCode( int code, String name, String main, String sub, boolean searchMapGlobe, boolean searchMapIVN ) {
		super();
		this.morphologicalCode = code;
		this.name = name;
		this.mainCategory = main;
		this.subCategory = sub;
		this.searchMapGLOBE = searchMapGlobe;
		this.searchMapIVN = searchMapIVN;
		this.createDate = Calendar.getInstance().getTime();
		this.updateDate = Calendar.getInstance().getTime();
		this.descriptions = new HashMap<String, ILocaleDescription>();
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.location.model.ITemp#getId()
	 */
	@Override
	public long getId() {
		return this.morphologicalCode;
	}
	
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
		return subCategory;
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
	 * @see org.fgf.animal.count.location.model.ITemp#addDescription(java.util.Locale, org.fgf.animal.count.core.model.ILocaleDescription)
	 */
	@Override
	public void addDescription( Locale locale, ILocaleDescription description) {
		this.descriptions.put( locale.getDisplayName(), description );
	}

	
	@Override
	public void addDescription(DefaultDescriptions key, ILocaleDescription description) {
		this.descriptions.put( key.name(), description);
	}

	@Override
	public ILocaleDescription getDescription(DefaultDescriptions key) {
		return 	this.descriptions.get( key.name());
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
		return subCategory;
	}

	@Override
	public Map<String, ILocaleDescription> getDescriptions() {
		return descriptions;
	}

	
	@Override
	public ILocaleDescription getDescription(String key) {
		return descriptions.get(key);
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

	public static IMorphoCode create( IMorphoCode code ) {
		if( code == null )
			return null;
		IMorphoCode mc = new MorphoCode( code.getMorphologicalCode(), code.getName(), 
				code.getMainCategory(), code.getSubCategory(), 
				code.isSearchMapGLOBE(), code.isSearchMapIVN());
		Iterator<Map.Entry<String, ILocaleDescription>> iterator = code.getDescriptions().entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, ILocaleDescription> entry = iterator.next();
			mc.addDescription( ILocaleDescription.DefaultDescriptions.valueOf( entry.getKey().toUpperCase() ), new LocaleDescriptor( entry.getValue() ));
		}
		return mc;
	}
	
	@Override
	public int compareTo(IMorphoCode arg0) {
		return morphologicalCode - arg0.getMorphologicalCode();
	}
	
	private static class LocaleDescriptor implements ILocaleDescription{

		private long id;
		private String name;
		private String alternative;
		private String description;
		
		
		public LocaleDescriptor( ILocaleDescription lc ) {
			super();
			this.id = lc.getId();
			this.name = lc.getName();
			this.alternative = lc.getAlternative();
			this.description = lc.getDescription();
		}
		@Override
		public long getId() {
			return id;
		}
		@Override
		public String getName() {
			return name;
		}
		@Override
		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String getAlternative() {
			return this.alternative;
		}
		@Override
		public void setAlternative(String name) {
			this.alternative = name;
		}
		
		@Override
		public String getDescription() {
			return this.description;
		}
		@Override
		public void setDescription(String description) {
			this.description = description;
		}
	}
}