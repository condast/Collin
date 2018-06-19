package org.collin.core.model;

import java.util.Locale;
import java.util.Map;

import org.collin.core.model.ILocaleDescription.DefaultDescriptions;

public interface IMorphoCode extends Comparable<IMorphoCode>{

	long getId();

	String getName();

	void setName(String name);

	ILocaleDescription getDescription(Locale locale);

	void addDescription(Locale locale, ILocaleDescription description);

	void addDescription( ILocaleDescription.DefaultDescriptions key, ILocaleDescription description);

	void removeDescription(Locale locale);

	int getMorphologicalCode();

	void setMorphologicalCode( int morphologicalCode);

	void setSearchMapIVN(boolean searchMapIVN);

	boolean isSearchMapIVN();

	boolean isSearchMapGLOBE();

	void setSearchMapGLOBE(boolean searchMapGLOBE);

	String getMainCategory();

	String getSubCategory();

	int compareTo(IMorphoCode arg0);

	Map<String, ILocaleDescription> getDescriptions();

	ILocaleDescription getDescription(String key);

	ILocaleDescription getDescription(DefaultDescriptions key);


} 
