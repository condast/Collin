package org.collin.moodle.module;

public interface IModule {

	/* (non-Javadoc)
	 * @see org.collin.moodle.xml.Module#getId()
	 */
	long getId();

	IModule addChild(int id, String name);

	IModule removeChild(int id);

	String getModuleId();

}