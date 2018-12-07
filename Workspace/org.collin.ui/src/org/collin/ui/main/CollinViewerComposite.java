package org.collin.ui.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.collin.core.connector.TetraConnector;
import org.collin.core.connector.IConnector;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.Compass;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.xml.CollinBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;

public class CollinViewerComposite extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_MOODLE_URL = "https://www.plusklas.nu";
	
	private CollinBuilder<?> builder;
	private TreeViewer viewer;
	private TetraEventWidget<Object> eventWidget;
	private ConnectorLogger<Object> logger;
	
	private ISelectionChangedListener listener;
	
	private Compass<Object>[] compasses;
		
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CollinViewerComposite(Composite parent, int style) {
		super(parent, style);
		setLayout( new GridLayout(3, false));
		
		viewer = new TreeViewer(this, SWT.BORDER);
		Tree tree = viewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        viewer.setContentProvider(new TreeContentProvider());
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = event.getStructuredSelection();
				if( selection.getFirstElement() instanceof Compass ) {
					Compass<Object> compass = (Compass<Object>) selection.getFirstElement();
					logger.setInput(compass);
				}else if( selection.getFirstElement() instanceof ITetra ) {
					logger.clear();
					ITetra<?> tetra = (ITetra<?>) selection.getFirstElement();
					TetraEvent<Object> tevent = new TetraEvent<Object>( tetra, new Object( ));
					for( Compass<Object> compass: compasses )
						compass.fire(tevent);
					eventWidget.setInput(tevent);
					
				}
				
			}
		});
 
        TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
        viewerColumn.getColumn().setWidth(300);
        viewerColumn.getColumn().setText("Names");
        viewerColumn.setLabelProvider(new ColumnLabelProvider());
        
        eventWidget = new TetraEventWidget<>(this, style);
		eventWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        logger = new ConnectorLogger<>(this, style);
		logger.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}
	
	public ISelectionChangedListener getSelectionListener( ) {
		return this.listener;
	}
	
	public void setSelectionListener( ISelectionChangedListener listener ) {
		if( this.listener != null )
			this.viewer.removeSelectionChangedListener(listener);
		this.listener = listener;
		this.viewer.addSelectionChangedListener(listener);
	}

	public void removeSelectionListener( ISelectionChangedListener listener ) {
		this.viewer.removeSelectionChangedListener(listener);
		this.listener = null;
	}

	@SuppressWarnings("unchecked")
	public void setInput( Class<?> clss) {
		try {
			builder = new CollinBuilder<Object>( clss);
			compasses = (Compass<Object>[]) builder.build();
			viewer.setInput(compasses);
			logger.setInput(compasses[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public class TreeContentProvider implements ITreeContentProvider {
		private static final long serialVersionUID = 1L;

		@Override
	    public boolean hasChildren(Object element) {
	    	if( element instanceof Compass ) {
	    		return true;
	    	}else if( element instanceof ITetra ) {
	    		return true;
	    	}else if( element instanceof ITetraNode )
	    		return false;
	    	return ( element instanceof TetraConnector );
	    }

	    @SuppressWarnings("unchecked")
		@Override
	    public Object getParent(Object element) {
	    	if( element instanceof Compass ) {
	    		Compass<?> compass = (Compass<?>) element;
	    		return compass.getParent();
	    	}else if( element instanceof ITetraNode ) {
	    		ITetraNode<?> node = (ITetraNode<?>) element;
	    		return node.getParent();
	    	}if( element instanceof TetraConnector ) {
	    		TetraConnector<Compass<?>, ?> connector = (TetraConnector<Compass<?>,?>) element;
	    		return connector.getOwner();
	    	}if( element instanceof IConnector ) {
	    		IConnector<Compass<?>, ?> connector = (IConnector<Compass<?>,?>) element;
	    		return connector.getOwner();
	    	}
	    	return null;
	    }

	    @Override
	    public Object[] getElements(Object inputElement) {
	        return ArrayContentProvider.getInstance().getElements(inputElement);
	    }

	    @Override
	    public Object[] getChildren(Object parentElement) {
	    	if( parentElement instanceof Compass ) {
	    		Compass<?> compass = (Compass<?>) parentElement;
	    		Collection<Object> children = new ArrayList<>();
	    		children.addAll( Arrays.asList( compass.getChildren() ));
	    		children.addAll( Arrays.asList( compass.getTetras() ));
	    		children.add( compass.getConnectors() );
	    		return children.toArray( new Object[ children.size()]);
	    	}else if( parentElement instanceof ITetra ) {
	    		ITetra<?> node = (ITetra<?>) parentElement;
	    		return node.getNodes();
	    	}else if( parentElement instanceof TetraConnector ) {
	    		TetraConnector<?,?> connector = (TetraConnector<?,?>) parentElement;
	    		return connector.getConnectors();
	    	}
	    	return null;
	    }

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}
	}
}
