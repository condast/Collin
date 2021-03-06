/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.collin.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.Compass;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.Tetra;
import org.collin.core.essence.TetraNode;
import org.collin.core.impl.ISequenceEventListener;
import org.collin.core.impl.SequenceEvent;
import org.collin.core.operator.IOperator;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class CollinBuilder<D extends Object>{

	public static String S_DEFAULT_FOLDER = "/design";
	public static String S_DEFAULT_FILE = "collin.xml";
	public static String S_SCHEMA_LOCATION =  S_DEFAULT_FOLDER + "/rdm-schema.xsd";

	protected static final String JAXP_SCHEMA_SOURCE =
		    "http://java.sun.com/xml/jaxp/properties/schemaSource";
	protected static final String JP2P_XSD_SCHEMA = "http://www.condast.com/saight/jp2p-schema.xsd";

	//private static final String S_ERR_NO_SCHEMA_FOUND = "The XML Schema was not found";
	private static final String S_WRN_NOT_NAMESPACE_AWARE = "The parser is not validating or is not namespace aware";
	private static final String S_WRN_NULL_TYPE = "This Tetra has a null type: ";	
	
	static final String S_DOCUMENT_ROOT = "DOCUMENT_ROOT";

	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";	

	static final String S_ERR_SOURCE = "The url and source may not both be provided. Choose only one";	

	private boolean completed, failed;
	private InputStream in;

	public enum CollinNodes{
		COLLIN,
		COMPASS,
		COMPASSES,
		TETRAS,
		TETRA,
		NODES,
		FUNCTION,
		GOAL,
		TASK,
		SOLUTION,
		OPERATION,
		AMBITION,
		LEARNING,
		RECOVERY;
	}

	private enum AttributeNames{
		ID,
		TITLE,
		NAME,
		DESCRIPTION,
		ENABLED,
		TYPE,
		INDEX,
		LOCALE,
		OPERATOR,
		PROGRESS,
		SOURCE,
		TETRA,
		URI,
		FROM,
		TO;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}
	
	private XmlHandler<D> handler;

	private Collection<ISequenceEventListener> listeners;
	
	private Class<?> clss;
	
	private static Logger logger = Logger.getLogger( CollinBuilder.class.getName() );

	public CollinBuilder( Class<?> clss ) throws IOException {
		this( clss, getDefaultModuleLocation());
	}
	
	public CollinBuilder( Class<?> clss, String fileName ) throws IOException {
		this( clss, clss.getResource( fileName ).openStream() );
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	public CollinBuilder( Class<?> clss, InputStream in) {
		this.in = in;
		this.clss = clss;
		this.completed = false;
		this.failed = false;
		this.listeners = new ArrayList<>();
	}
	
	public void addListener( ISequenceEventListener listener ) {
		this.listeners.add(listener);
	}

	public void removeListener( ISequenceEventListener listener ) {
		this.listeners.remove(listener);
	}
	
	protected void notifyListeners( SequenceEvent event ) {
		for( ISequenceEventListener listener: this.listeners )
			listener.notifySequenceEvent(event);
	}

	@SuppressWarnings("unchecked")
	public Compass<D>[] build() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//URL schema_in = XMLFactoryBuilder.class.getResource( S_SCHEMA_LOCATION); 
		//if( schema_in == null )
		//	throw new RuntimeException( S_ERR_NO_SCHEMA_FOUND );
		
		//factory.setNamespaceAware( true );
		//SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		//Source schemaFile = new StreamSource( this.getClass().getResourceAsStream( S_SCHEMA_LOCATION ));
		
		//First parse the XML file
		Collection<Compass<D>> result = null;
		try {
			logger.info("Parsing code " );
			//Schema schema = schemaFactory.newSchema(schemaFile);
			//factory.setSchema(schema);//saxParser.
			
			SAXParser saxParser = factory.newSAXParser();
			if( !saxParser.isNamespaceAware() )
				logger.warning( S_WRN_NOT_NAMESPACE_AWARE );
			
			//saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
			//saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(JP2P_XSD_SCHEMA)); 
			handler = new XmlHandler<D>( clss );
			saxParser.parse( in, handler);
			result = handler.compasses;
		}
		catch( Exception ex) {
			ex.printStackTrace();
		}
		return (Compass<D>[]) result.toArray( new Compass<?>[ result.size()]);
	}
	
	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.xml.IWizardBuilder#complete()
	 */
	public boolean complete() {
		this.completed = true;
		return completed;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.xml.IWizardBuilder#isCompleted()
	 */
	public boolean isCompleted() {
		return completed;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.wizard.xml.IWizardBuilder#hasFailed()
	 */
	public boolean hasFailed() {
		return failed;
	}

	public static String getLocation( String defaultLocation ){
		if( !StringUtils.isEmpty( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}

	public static final String getDefaultModuleLocation() {
		return S_DEFAULT_FOLDER + "/" + S_DEFAULT_FILE;
	}

	public static class XmlHandler<D extends Object> extends DefaultHandler{
		
		public static final int MAX_COUNT = 200;

		private Collection<Compass<D>> compasses;
		
		private Compass<D> currentCompass;
		private ITetraNode<D> currentNode;
		private int index;
		private Locale locale;
		private Class<?> clss;
		
		public XmlHandler(	Class<?> clss ) {
			this.index = 0;
			this.clss = clss;
			this.locale = Locale.getDefault();
			this.compasses = new ArrayList<>();
		}

		public Locale getLocale() {
			return locale;
		}

		@Override
		public void startElement(String uri, String localName, String qName, 
				Attributes attributes) throws SAXException {

			String componentName = StringStyler.styleToEnum( qName );

			String id = attributes.getValue( AttributeNames.ID.toXmlStyle());
			String type = attributes.getValue( AttributeNames.TYPE.toXmlStyle());
			String title = attributes.getValue( AttributeNames.TITLE.toXmlStyle());
			String name = attributes.getValue( AttributeNames.NAME.toXmlStyle());
			String description = attributes.getValue( AttributeNames.DESCRIPTION.toXmlStyle());
			//String url = attributes.getValue( AttributeNames.URI.toXmlStyle());
			String index_str = attributes.getValue( AttributeNames.INDEX.toXmlStyle());
			//String progress_str = attributes.getValue( AttributeNames.PROGRESS.toXmlStyle());
			String locale_str = attributes.getValue( AttributeNames.LOCALE.toXmlStyle());
			String tetra_str = attributes.getValue( AttributeNames.TETRA.toXmlStyle());
			String operator_str = attributes.getValue( AttributeNames.OPERATOR.toXmlStyle());
			boolean isTetra = StringUtils.isEmpty( tetra_str )?false: Boolean.parseBoolean(tetra_str);			
			if(!StringUtils.isEmpty(locale_str)) {
				String[] split = locale_str.split("[-]");
				locale = new Locale(split[0], split[1]);
			}
			if( !StringUtils.isEmpty(index_str))
				index = Integer.parseInt(index_str);
			String tid = null;

			ITetra<D> parent;
			CollinNodes node = CollinNodes.valueOf(componentName);
			switch( node ){
			case COLLIN:
				index = 0;
				break;
			case COMPASSES:
				index = 0;
				break;
			case COMPASS:
				Compass<D>compass = new Compass<D>( clss, currentCompass, id, title );
				if( currentCompass != null )
					currentCompass.addChild(compass);
				else {
					compasses.add(compass);
				}
				currentCompass = compass;	
				if( !StringUtils.isEmpty(description))
					compass.setDescription(description);
				index++;
				break;
			case TETRA:
				index=0;
				if( StringUtils.isEmpty(type)) {
					logger.warning( S_WRN_NULL_TYPE + id);
					return;
				}
				Compass.Tetras ttype = StringUtils.isEmpty(type)? Compass.Tetras.UNDEFINED: 
					Compass.Tetras.valueOf( StringStyler.styleToEnum(type));
				tid = StringUtils.isEmpty(id)?Compass.createId(currentCompass, ttype): id;
				ITetra<D> tetra;
				if( currentNode == null ) {
					tetra = new Tetra<D>( tid, ttype.toString() );
					currentCompass.addTetra(ttype, tetra);
				}else if( currentNode instanceof ITetra ){
					parent = (ITetra<D>) currentNode;
					ITetraNode.Nodes tnode = StringUtils.isEmpty(type)? ITetraNode.Nodes.UNDEFINED: ITetraNode.Nodes.valueOf( StringStyler.styleToEnum(type ));
					tetra = new Tetra<D>( parent, tid, ttype.toString(), tnode );
					parent.addNode(tetra);
				}else{
					parent = (ITetra<D>) currentNode.getParent();
					String tname = Compass.Tetras.UNDEFINED.equals(ttype)? parent.getName(): ttype.toString();
					tetra = new Tetra<D>( tname, currentNode );
					parent.addNode( tetra );
				}
				if( !StringUtils.isEmpty(description))
					tetra.setDescription(description);
				currentNode = tetra;
				break;
			case NODES:
				index=0;
				break;	
			case FUNCTION:
			case GOAL:
			case TASK:
			case SOLUTION:
				parent = (ITetra<D>) currentNode;
				ITetraNode.Nodes tnode = ITetraNode.Nodes.valueOf( componentName ); 
				tid = StringUtils.isEmpty(id)?TetraNode.createId(parent, tnode): id;
				String tname = StringUtils.isEmpty(name)?TetraNode.createName(parent, tnode): name;
				if( isTetra ) {
					currentNode = new Tetra<D>( parent, tid, tname, tnode );
				}else{
					currentNode = new TetraNode<D>( parent, tid, tname, tnode );
					if( !StringUtils.isEmpty(operator_str)) {
						constructOperator(clss, operator_str, attributes, currentNode );
					}
				}
				parent.addNode( currentNode );
				if( !StringUtils.isEmpty(description))
					currentNode.setDescription(description);
				break;	
			case AMBITION:
			case OPERATION:
			case LEARNING:
			case RECOVERY:
				break;	
			default:
				break;
			}
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String componentName = StringStyler.styleToEnum( qName );		
			CollinNodes node = CollinNodes.valueOf(componentName);
			switch( node ){
			case COMPASS:
				currentCompass.init();
				currentCompass = currentCompass.getParent();
				break;
			case TETRA:
				if( currentNode instanceof ITetra<?>) {
					ITetra<?> tetra = (ITetra<?>) currentNode;
					tetra.init();
 					currentNode = (ITetraNode<D>) currentNode.getParent();
				}
				break;
			case FUNCTION:
			case GOAL:
			case TASK:
			case SOLUTION:
				if( currentNode == null )
					break;
				if( currentNode instanceof ITetra<?>) {
					ITetra<?> tetra = (ITetra<?>) currentNode;
					tetra.init();
				}
				currentNode = (ITetraNode<D>) currentNode.getParent();
				break;
			default:
				break;
			}
		}

		@SuppressWarnings("unused")
		@Override
		public void characters(char ch[], int start, int length) throws SAXException {
			String value = new String(ch, start, length);
		}

		@SuppressWarnings("unchecked")
		public static <D> IOperator<D> constructOperator( Class<?> clss, String className, Attributes attrs, ITetraNode<D> origin){
			if( StringUtils.isEmpty( className ))
				return null;
			Class<IOperator<D>> builderClass;
			IOperator<D> operator = null;
			try {
				builderClass = (Class<IOperator<D>>) clss.getClassLoader().loadClass( className );
				Constructor<IOperator<D>> constructor = builderClass.getConstructor();
				operator = constructor.newInstance();
				origin.setOperator(operator);
				operator.setParameters(attrs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return operator;
		}

		private void print(SAXParseException x)
		{
			Logger.getLogger( this.getClass().getName()).info(x.getMessage());
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			print(e);
			super.error(e);
		}

		@Override
		public void fatalError(SAXParseException arg0) throws SAXException {
			print(arg0);
			super.fatalError(arg0);
		}

		@Override
		public void warning(SAXParseException arg0) throws SAXException {
			print(arg0);
			super.warning(arg0);
		}
	}
}