package org.collin.core.model;

public interface ILocaleDescription {

	public enum DefaultDescriptions{
		NL,
		EN,
		MATTHIJS
	}
	
	long getId();

	String getName();

	void setName(String name);

	String getAlternative();

	void setAlternative(String name);

	String getDescription();

	void setDescription(String description);

}