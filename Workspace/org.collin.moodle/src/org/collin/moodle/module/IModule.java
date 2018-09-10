package org.collin.moodle.module;

import org.collin.moodle.xml.ModuleBuilder.SubModule;

public interface IModule {

	/* (non-Javadoc)
	 * @see org.collin.moodle.xml.Module#getId()
	 */
	long getId();

	SubModule addChild(int id, String name);

	SubModule removeChild(int id);

	String getModuleId();

}