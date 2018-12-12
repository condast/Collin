/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.collin.moodle.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.collin.core.xml.ISequenceEventListener;
import org.collin.core.xml.SequenceEvent;
import org.collin.core.xml.SequenceNode;
import org.collin.core.xml.SequenceNode.Nodes;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ModuleBuilder{

	public static String S_DEFAULT_FOLDER = "/design";
	public static String S_DEFAULT_FILE = "lesson.xml";
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

	public enum SequenceEvents{
		RESET,
		CURRENT,
		NEXT,
		PREVIOUS,
		NEXT_TRACK,
		PREVIOUS_TRACK,
		OFFSET,
		COMPLETE;
	}

	private enum AttributeNames{
		ID,
		TITLE,
		NAME,
		CLASS,
		COLLIN,
		DESCRIPTION,
		ENABLED,
		TYPE,
		INDEX,
		LOCALE,
		PROGRESS,
		SOURCE,
		TOTAL_TIME,
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
	
	private XmlHandler handler;

	private Collection<ISequenceEventListener> listeners;
	
	private Logger logger = Logger.getLogger( ModuleBuilder.class.getName() );

	public ModuleBuilder( Class<?> clss ) throws IOException {
		this( clss, getDefaultModuleLocation());
	}
	
	public ModuleBuilder( Class<?> clss, String fileName ) throws IOException {
		this( clss.getResource( fileName ).openStream() );
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	public ModuleBuilder( InputStream in) {
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

	public SequenceNode build() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//URL schema_in = XMLFactoryBuilder.class.getResource( S_SCHEMA_LOCATION); 
		//if( schema_in == null )
		//	throw new RuntimeException( S_ERR_NO_SCHEMA_FOUND );
		
		//factory.setNamespaceAware( true );
		//SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		//Source schemaFile = new StreamSource( this.getClass().getResourceAsStream( S_SCHEMA_LOCATION ));
		
		//First parse the XML file
		SequenceNode result = null;
		try {
			logger.info("Parsing code " );
			//Schema schema = schemaFactory.newSchema(schemaFile);
			//factory.setSchema(schema);//saxParser.
			
			SAXParser saxParser = factory.newSAXParser();
			if( !saxParser.isNamespaceAware() )
				logger.warning( S_WRN_NOT_NAMESPACE_AWARE );
			
			//saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
			//saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(JP2P_XSD_SCHEMA)); 
			handler = new XmlHandler();
			saxParser.parse( in, handler);
			result = handler.root;
		}
		catch( Exception ex) {
			ex.printStackTrace();
		}
		return result;
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

	public static class XmlHandler extends DefaultHandler{
		
		public static final int MAX_COUNT = 200;	
		
		private Stack<Nodes> stored;
		
		private SequenceNode root, current;
		private SequenceNode parent;
		private int index;
		private Locale locale;
		
		public XmlHandler( ) {
			stored = new Stack<Nodes>();
			this.index = 0;
			this.locale = Locale.getDefault();
		}
	
		@Override
		public void startElement(String uri, String localName, String qName, 
				Attributes attributes) throws SAXException {
			if(( current != null ) &&  Nodes.TEXT.equals( current.getNode()))
				return;
					
			String componentName = StringStyler.styleToEnum( qName );
			
			//The name is not a group. try the default JP2P components
			if( !Nodes.isNode( componentName ))
				return;
			
			String id = attributes.getValue( AttributeNames.ID.toXmlStyle());
			String type = attributes.getValue( AttributeNames.TYPE.toXmlStyle());
			String title = attributes.getValue( AttributeNames.TITLE.toXmlStyle());
			String name = attributes.getValue( AttributeNames.NAME.toXmlStyle());
			String description = attributes.getValue( AttributeNames.DESCRIPTION.toXmlStyle());
			String url = attributes.getValue( AttributeNames.URI.toXmlStyle());
			String index_str = attributes.getValue( AttributeNames.INDEX.toXmlStyle());
			String from = attributes.getValue( AttributeNames.FROM.toXmlStyle());
			String to = attributes.getValue( AttributeNames.TO.toXmlStyle());
			String progress_str = attributes.getValue( AttributeNames.PROGRESS.toXmlStyle());
			float progress = StringUtils.isEmpty(progress_str)?0: Float.valueOf(progress_str);  
			String locale_str = attributes.getValue( AttributeNames.LOCALE.toXmlStyle());
			String totaltime_str = attributes.getValue( AttributeNames.TOTAL_TIME.toXmlStyle());
			long totalTime = StringUtils.isEmpty(totaltime_str)?-1: Long.parseLong(totaltime_str);
			if(!StringUtils.isEmpty(locale_str)) {
				String[] split = locale_str.split("[-]");
				locale = new Locale(split[0], split[1]);
			}
			String class_str = attributes.getValue( AttributeNames.CLASS.toXmlStyle());
			if( !StringUtils.isEmpty(index_str))
				index = Integer.parseInt(index_str);
			String collin = attributes.getValue( AttributeNames.COLLIN.toXmlStyle());
			if( StringUtils.isEmpty(collin) && ( this.current != null )) {
				collin = this.current.getCollin();
			}
			Nodes node = Nodes.valueOf(componentName);
			switch( node ){
			case COURSE:
				index = 0;
				current = new SequenceNode( node, locale, id, name, index, title);
				root= current;
				if( !StringUtils.isEmpty(title ))
					current.setTitle(title);
				break;
			case MODEL:
				index = 0;
				parent = current;
				current = new SequenceNode(node, locale, id, name, collin, index);
				break;
			case MODULES:
				index = 0;
				parent = current;
				current = new SequenceNode(node, locale, id, name, collin, index);
				break;
			case MODULE:
				current = new SequenceNode(node, locale, id, name, collin, index, totalTime);
				current.setUri(uri);
				index++;
				break;
			case ACTIVITIES:
				index = 0;
				parent = current;
				current = new SequenceNode(node, locale, id, name, collin, index);
				break;
			case ACTIVITY:
				current = new SequenceNode(node, locale, id, name, collin, index, totalTime);
				current.setUri(uri);
				index++;
				break;
			case VIEW:
				index=0;
				current = new SequenceNode(node, locale, id, collin, name, index);
				if( !StringUtils.isEmpty(type))
					current.setType(type);
				break;
			case PARTS:
				index=0;
				current = new SequenceNode(node, locale, id, collin, name, index);
				completeFromTo(current, from, to);
				break;	
			case PART:
				current = new SequenceNode(node, locale, id, collin, name, index);
				index++;
				break;	
			case CONTROLLER:
				index=0;
				current = new SequenceNode(node, locale, id, collin, name, index);
				break;	
			case SEQUENCE:
				index=0;
				current = new SequenceNode(node, locale, id, collin, name, index);
				break;	
			case STEP:
				current = new SequenceNode(node, locale, id, collin, name, index);
				if( !StringUtils.isEmpty(name))
					current.setTitle(name);
				break;	
			case ADVICE:
				current = new SequenceNode(node, locale, id, collin, name, index);
				current.setProgress( progress);
				current.setType(type);
				break;	
			case TEXT:
				current = new SequenceNode(node, locale, id, name, collin, index);
				break;
			case FUNCTION:
			case GOAL:
			case TASK:
			case SOLUTION:
				current = new SequenceNode(node, locale, id, name, collin, 0, totalTime);
				current.setUri(uri);
				break;
			default:
				break;
			}
			if(( parent != null ) && ( !parent.equals(current))) {
				parent.addChild(current);
			}else if( parent == null )
				parent = current;

			if(!StringUtils.isEmpty(url))
				current.setUri(url);
			if(!StringUtils.isEmpty( description))
				current.setUri(description);
			stored.push(node);
			parent = current;
		}
		
		protected void completeFromTo( SequenceNode node, String from, String to ) {
			SequenceNode view = find( this.root, Nodes.VIEW);
			SequenceNode parts= find( view, Nodes.PARTS);
			SequenceNode frm_seq = null;
			if( !StringUtils.isEmpty(from)) {
				frm_seq = find( parts, from );
			};
			if( frm_seq != null ) {
				SequenceNode to_seq = StringUtils.isEmpty(to)? parts.lastChild(): parts.find( to );
				int index = parts.getChildren().indexOf(frm_seq);
				int last = parts.getChildren().indexOf(to_seq);
				if( last < index)
					last = parts.getChildren().size()-1;
				for( int i=index; i<last; i++ ) {
					current.addChild(parts.getChildren().get(i));
				}
			}
			
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String componentName = StringStyler.styleToEnum( qName );		
			//The name is not a node.
			if( !Nodes.isNode( componentName ))
				return;
			if( stored.isEmpty())
				return;
			Nodes node = Nodes.valueOf(componentName);
			switch( node ){
			case MODULES:
				break;
			default:
				break;
			}
			if(( parent != null ) && ( parent.getParent() != null ))
				parent = parent.getParent();
			stored.pop();
		}

		@Override
		public void characters(char ch[], int start, int length) throws SAXException {
			String value = new String(ch, start, length);
			if( StringUtils.isEmpty( value ) || ( stored.isEmpty()))
				return;
			Nodes current = stored.lastElement();
			switch( current ){
			case PART:
				break;
			default:
				break;
			}
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


	protected static SequenceNode find( SequenceNode current, Nodes node ) {
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