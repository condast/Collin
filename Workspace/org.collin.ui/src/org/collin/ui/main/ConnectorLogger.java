package org.collin.ui.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.collin.core.connector.ConnectorEvent;
import org.collin.core.connector.IConnectorListener;
import org.collin.core.essence.Compass;
import org.condast.commons.strings.StringUtils;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class ConnectorLogger<D extends Object> extends Composite {
	private static final long serialVersionUID = 1L;

	private enum Columns{
		INDEX(0),
		ORIGIN(1),
		TYPE(2),
		DESTINATION(3),
		DEST_ID(4);
		
		private int index;

		private Columns( int index ){
			this.index = index;
		}
		
		@SuppressWarnings("unused")
		public int getIndex() {
			return index;
		}


		@Override
		public String toString() {
			return StringUtils.prettyString(super.toString());
		}

		public static int getWeight( Columns column ){
			switch( column ){
			case ORIGIN:
				return 20;
			case DESTINATION:
				return 40;
			default:
				return 10;
			}
		}
	}

	private Compass<D> compass;
	private Collection<DataObject> logs;
	
	private TableViewer tableViewer;
	private TableColumnLayout tableColumnLayout;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private IConnectorListener<Compass<D>, D> listener = new IConnectorListener<Compass<D>,D>() {

		@Override
		public void notifyConnectorFired(ConnectorEvent<Compass<D>, D> event) {
			DataObject data = new DataObject( event, logs.size());
			logs.add( data);
			logger.info("Adding log: " + event.getOrigin().getName() + " - " + 
			event.getDestination().getName() + ": " + logs.size());
			tableViewer.refresh();
			requestLayout();
		}
	};
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ConnectorLogger(Composite parent, int style ) {
		super(parent, style);
		this.createComposite(parent, style);
		logs = new ArrayList<>();
	}

	protected void createComposite(Composite parent, int style ) {
		tableColumnLayout = new TableColumnLayout();
		super.setLayout( tableColumnLayout );

		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		for( Columns column: Columns.values()){
			createColumns( column, tableViewer);
		}

		Table argumentTable = tableViewer.getTable();
		argumentTable.setHeaderVisible(true);
		argumentTable.setLinesVisible(true);

		tableViewer.setUseHashlookup( true );
		tableViewer.setContentProvider( ArrayContentProvider.getInstance() );
		tableViewer.setLabelProvider(new LogProvider() );
	}

	private void createColumns( Columns column, TableViewer tableViewer) {
		TableViewerColumn tcolumn = new TableViewerColumn( tableViewer, SWT.NONE);
		tableColumnLayout.setColumnData(tcolumn.getColumn(), new ColumnWeightData( Columns.getWeight(column)));
		tcolumn.getColumn().setData(column);
		tcolumn.getColumn().setText( column.toString());
	}

	public void clear() {
		this.logs.clear();
	}
	
	public Compass<D> getInput() {
		return compass;
	}

	public void setInput( Compass<D> compass ){
		this.clear();
		if( this.compass != null )
			compass.removeConnectorListener(listener);
		this.compass = compass;
		this.compass.addConnectorListener(listener);
		tableViewer.setInput(logs);
		requestLayout();
	}

	private class LogProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(Object element, int columnIndex ) {
			String text = null;
			Columns column = Columns.values()[ columnIndex ];
			try{
				DataObject datao = (ConnectorLogger<D>.DataObject) element;
				ConnectorEvent<Compass<D>, D>data = datao.event;
				switch( column ){
				case INDEX:
					text = String.valueOf( datao.index);
					break;
				case ORIGIN:
					text = data.getOrigin().getName();
					break;
				case DESTINATION:
					text = data.getDestination().getName();
					break;
				case DEST_ID:
					text = data.getDestination().getType().toString();
					break;
				case TYPE:
					text = data.getOrigin().getType().toString();					
					break;
				default:
					break;
				}

			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
			return text;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			Image img = super.getImage(element);
		return img;
		}
	}	
	
	private class DataObject{
		ConnectorEvent<Compass<D>, D> event;
		int index;
		public DataObject(ConnectorEvent<Compass<D>, D> event, int index) {
			super();
			this.event = event;
			this.index = index;
		}
	}
}