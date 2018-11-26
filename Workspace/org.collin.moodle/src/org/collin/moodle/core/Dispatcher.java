package org.collin.moodle.core;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.collin.core.xml.Sequence;
import org.collin.moodle.module.IModule;
import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.Utils;
import org.condast.commons.io.IOUtils;

import com.google.gson.Gson;

public class Dispatcher {

	private static Dispatcher dispatcher = new Dispatcher();
	
	private Map<Long, Integer> progress;	
	
	private Map<Long, URI> lessons;

	private Dispatcher() {
		super();
		progress = new HashMap<>();
		this.lessons = new HashMap<>();
	}

	public static Dispatcher getInstance() {
		return dispatcher;
	}

	public long addLesson( String path ) {
		long lessonId = this.lessons.size();
		URI uri = URI.create(path);
		if( uri == null )
			return -1;
		lessons.put(lessonId, uri);
		return lessonId;

	}
	
	public String getAdvice( long lessonId, long moduleId, double progress ) throws Exception {
		InputStream stream = null;
		String result = null;
		try {
			URI uri = lessons.get(lessonId);
			stream = this.getClass().getResourceAsStream(uri.getPath()); //get URL from your uri object
			Sequence sequencer = new Sequence( stream );
			result = sequencer.getAdvice( moduleId, progress );
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
		return result;
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
