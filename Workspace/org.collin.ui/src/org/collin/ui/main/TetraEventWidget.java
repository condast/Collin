package org.collin.ui.main;

import org.collin.core.def.ITetraNode;
import org.collin.core.transaction.TetraTransaction;
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

public class TetraEventWidget<D extends Object> extends Composite {
	private static final long serialVersionUID = 1L;


	private enum Columns{
		ID(0),
		TYPE(1);
		
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
			case ID:
				return 20;
			default:
				return 10;
			}
		}
	}

	private TetraTransaction<D> tetraEvent;
	
	private TableViewer tableViewer;
	private TableColumnLayout tableColumnLayout;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TetraEventWidget(Composite parent, int style ) {
		super(parent, style);
		this.createComposite(parent, style);
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
		tableViewer.setLabelProvider(new TetraNodeProvider() );
	}

	private void createColumns( Columns column, TableViewer tableViewer) {
		TableViewerColumn tcolumn = new TableViewerColumn( tableViewer, SWT.NONE);
		tableColumnLayout.setColumnData(tcolumn.getColumn(), new ColumnWeightData( Columns.getWeight(column)));
		tcolumn.getColumn().setData(column);
		tcolumn.getColumn().setText( column.toString());
	}

	public TetraTransaction<D> getInput() {
		return tetraEvent;
	}

	public void setInput( TetraTransaction<D> event ){
		this.tetraEvent = event;
		tableViewer.setInput(event.getHistory());
	}

	private class TetraNodeProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(Object element, int columnIndex ) {
			String text = null;
			Columns column = Columns.values()[ columnIndex ];
			try{
				ITetraNode<D> data = (ITetraNode<D>) element;
				switch( column ){
				case ID:
					text = data.getId();
					break;
				case TYPE:
					text = data.getType().toString();
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
}