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
		PROGRESS,
		SOURCE,
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
	
	private Logger logger = Logger.getLogger( CollinBuilder.class.getName() );

	public CollinBuilder( Class<?> clss ) throws IOException {
		this( clss, getDefaultModuleLocation());
	}
	
	public CollinBuilder( Class<?> clss, String fileName ) throws IOException {
		this( clss.getResource( fileName ).openStream() );
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	public CollinBuilder( InputStream in) {
		this.in = in;
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
			handler = new XmlHandler<D>();
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
		
		public XmlHandler( ) {
			this.index = 0;
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
			String url = attributes.getValue( AttributeNames.URI.toXmlStyle());
			String index_str = attributes.getValue( AttributeNames.INDEX.toXmlStyle());
			String progress_str = attributes.getValue( AttributeNames.PROGRESS.toXmlStyle());
			String locale_str = attributes.getValue( AttributeNames.LOCALE.toXmlStyle());
			if(!StringUtils.isEmpty(locale_str)) {
				String[] split = locale_str.split("[-]");
				locale = new Locale(split[0], split[1]);
			}
			if( !StringUtils.isEmpty(index_str))
				index = Integer.parseInt(index_str);
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
				Compass<D>compass = new Compass<D>( currentCompass, id, title );
				if( currentCompass != null )
					currentCompass.addChild(compass);
				else
					compasses.add(compass);
				currentCompass = compass;	
				if( !StringUtils.isEmpty(description))
					compass.setDescription(description);
				index++;
				break;
			case TETRA:
				index=0;
				Compass.Tetras ttype = StringUtils.isEmpty(type)? Compass.Tetras.UNDEFINED: 
					Compass.Tetras.valueOf( StringStyler.styleToEnum(type));
				ITetra<D> tetra;
				if( currentNode == null ) {
					tetra = new Tetra<D>( ttype.name(), name );
					tetra.init();
					currentCompass.addTetra(ttype, tetra);
				}else if( currentNode instanceof ITetra ){
					parent = (ITetra<D>) currentNode;
					ITetraNode.Nodes tnode = StringUtils.isEmpty(type)? ITetraNode.Nodes.UNDEFINED: ITetraNode.Nodes.valueOf( StringStyler.styleToEnum(type ));
					tetra = new Tetra<D>( parent, ttype.name(), name, tnode, null );
					tetra.init();
					parent.addNode(tetra);
				}else{
					parent = (ITetra<D>) currentNode.getParent();
					String tname = Compass.Tetras.UNDEFINED.equals(ttype)? parent.getName(): ttype.name();
					tetra = new Tetra<D>( tname, currentNode );
					tetra.init();
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
				currentNode = new TetraNode<D>( parent, id, tnode );
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
		
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String componentName = StringStyler.styleToEnum( qName );		
			CollinNodes node = CollinNodes.valueOf(componentName);
			switch( node ){
			case COMPASS:
				currentCompass = currentCompass.getParent();
				break;
			case TETRA:
			case FUNCTION:
			case GOAL:
			case TASK:
			case SOLUTION:
				if( currentNode != null )
					currentNode = currentNode.getParent();
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
	
	public static SequenceNode find( SequenceNode current, String id ) {
		if(( current == null ) || ( id.equals(current.getId())))
			return current;
		for( SequenceNode child: current.getChildren()) {
			SequenceNode find = find( child, id);
			if( find != null )
				return find;
		}
		return null;
	}


	protected static SequenceNode find( SequenceNode current, CollinNodes node ) {
		if(( current == null ) || node.equals( current.getNode() ))
			return current;
		
		for( SequenceNode child: current.getChildren()) {
			SequenceNode find = find( child, node);
			if( find != null )
				return find;
		}
		return null;		
	}
}