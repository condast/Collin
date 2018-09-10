/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.collin.moodle.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.collin.moodle.module.IModule;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.xml.AbstractXMLBuilder;
import org.condast.commons.xml.AbstractXmlHandler;
import org.xml.sax.Attributes;

public class ModuleBuilder extends AbstractXMLBuilder<IModule, ModuleBuilder.Elements> {
	
	public static String S_DEFAULT_MODULE_FOLDER = "/modules";
	public static String S_DEFAULT_MODULE_FILE = "modules.xml";

	public enum Elements{
		FEEDBACK,
		MODULES,
		MODULE,
		SUB_MODULE
	}
	
	private Class<?> clss;

	public ModuleBuilder(  ) {
		this( ModuleBuilder.class );
	}

	public ModuleBuilder(  Class<?> clss ) {
		super( new XMLHandler( clss ), clss.getResource( S_DEFAULT_MODULE_FOLDER + File.separator + S_DEFAULT_MODULE_FILE) );
		this.clss = clss;
	}

	public static String getLocation( String defaultLocation ){
		if( !StringUtils.isEmpty( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}

	protected Class<?> getClss() {
		return clss;
	}
	
	@Override
	public void build() {
		super.build();
	}

	@Override
	public IModule[] getUnits() {
		return getHandler().getUnits();
	}
	
	private static class XMLHandler extends AbstractXmlHandler<IModule, ModuleBuilder.Elements>{
		
		private Collection<Module> modules;
		private Class<?> clss;
		private long id;
		
		private IModule current;
		
		public XMLHandler( Class<?> clss) {
			super( EnumSet.allOf( ModuleBuilder.Elements.class));
			this.modules = new ArrayList<>();
			this.clss = clss;
			this.id = 1;
		}

		@Override
		public IModule getUnit(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IModule[] getUnits() {
			return this.modules.toArray( new IModule[ this.modules.size()]);
		}

		@Override
		protected IModule parseNode( ModuleBuilder.Elements node, Attributes attributes) {
			IModule  retval = null;
			String moduleId = getAttribute( attributes, AttributeNames.ID );
			String name = getAttribute( attributes, AttributeNames.NAME );
			//String height_str = getAttribute( attributes, AttributeNames.HEIGHT );
			//int height = StringUtils.isEmpty( height_str )? 50: Integer.parseInt( height_str );
			String size_str = getAttribute( attributes, AttributeNames.SIZE );

			//String width_str = getAttribute( attributes, AttributeNames.WIDTH );
			//int width = StringUtils.isEmpty( width_str )? 50: Integer.parseInt( width_str );

			switch( node ){
			case MODULES:
				break;
			case MODULE:
				Module module = new Module( id, moduleId, name );
				id++;
				current = module;
				modules.add(module);
				break;
			case SUB_MODULE:
				String str = moduleId.replaceAll("[.]", "");
				int identifier =  Integer.parseInt(str);
				IModule mod = (IModule) current;
				mod.addChild(identifier, name);
				break;
			default:
				break;
			}
			return retval;
		}

		@Override
		protected void completeNode(Enum<Elements> node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void addValue(Enum<Elements> node, String value) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class Module implements IModule{

		private long id;
		private String moduleId;
		private String name;
		private Map<Integer, SubModule> submodules;
		
		protected Module( long id, String moduleId, String name ) {
			this.id = id;
			this.moduleId = moduleId;
			this.name = name;
			this.submodules = new HashMap<>();
		}
	
		@Override
		public long getId() {
			return id;
		}

		@Override
		public String getModuleId() {
			return moduleId;
		}

		/* (non-Javadoc)
		 * @see org.collin.moodle.xml.IModule#addChild(int, java.lang.String)
		 */
		@Override
		public SubModule addChild( int id, String name) {
			SubModule sm = new SubModule( name );
			this.submodules.put( id, sm );
			return sm;
		}

		/* (non-Javadoc)
		 * @see org.collin.moodle.xml.IModule#removeChild(int)
		 */
		@Override
		public SubModule removeChild( int id) {
			return this.submodules.remove( id );
		}
	}

	public static class SubModule{

		private String name;
		
		private Map<Integer, SubModule> submodules;
		
		protected SubModule( String name ) {
			this.submodules = new HashMap<>();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		protected SubModule addChild( int id, String name) {
			SubModule sm = new SubModule( name );
			this.submodules.put( id, sm );
			return sm;
		}

		protected SubModule removeChild( int id) {
			return this.submodules.remove( id );
		}
	}

}