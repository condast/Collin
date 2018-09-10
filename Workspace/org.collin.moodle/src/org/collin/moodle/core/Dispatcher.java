package org.collin.moodle.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.collin.moodle.module.IModule;
import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.Utils;
import com.google.gson.Gson;

public class Dispatcher {

	private static Dispatcher dispatcher = new Dispatcher();
	
	private Map<Long, Integer> progress;
	
	private Dispatcher() {
		super();
		progress = new HashMap<>();
	}

	public static Dispatcher getInstance() {
		return dispatcher;
	}

	public String getModules() {
		Collection<IModule> results = readModules();
		Gson gson = new Gson();
		return gson.toJson(results.toArray( new IModule[results.size()]));
	}

	public int getProgress( long moduleId ) {
		Integer value = progress.get( moduleId);
		if( value != null ) {
			int next = ( value >= 100 )? 1000: value + 10;
			progress.put(moduleId, next );
		}else {
			progress.put(moduleId, 0);			
		}
		return ( value == null)?0: value;
	}

	private static Collection<IModule> readModules() {
		ModuleBuilder builder = new ModuleBuilder();
		Collection<IModule> modules = new ArrayList<>();
		try {
			builder.build();
			IModule[] results = builder.getUnits();
			if(!Utils.assertNull( results ))
				modules.addAll( Arrays.asList(results));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return modules;
	}

}
