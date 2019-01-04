package org.collin.ui.main;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.Utils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;

public class TetraViewer extends Composite {
	private static final long serialVersionUID = 1L;

	public static final String S_MOODLE_URL = "https://www.plusklas.nu";
	
	private TreeViewer viewer;
	private ITetra<Object> tetra;
	
	private TetraEventWidget<Object> eventWidget;
		
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TetraViewer(Composite parent, int style) {
		super(parent, style);
		setLayout( new GridLayout(2, true));
		
		viewer = new TreeViewer(this, SWT.BORDER);
		Tree tree = viewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        viewer.setContentProvider(new TreeContentProvider());
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
        viewer.addSelectionChangedListener( new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				//viewer.getTree().pack();
			}
		});

        TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
        viewerColumn.getColumn().setWidth(500);
        viewerColumn.getColumn().setText("Names");
        viewerColumn.setLabelProvider(new ColumnLabelProvider());

        eventWidget = new TetraEventWidget<>(this, style);
		eventWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@SuppressWarnings("unchecked")
	public void setInput( long userId, ITetra<?> tetra) {
		this.tetra = (ITetra<Object>) tetra;
		ITetra<?>[] arr = new ITetra<?>[1];
		arr[0] = tetra;
		viewer.setInput(arr);
		if( tetra == null )
			return;
		TetraTransaction<Object> event = new TetraTransaction<Object>( this, userId, new Object( ));
		this.tetra.fire(event);
		eventWidget.setInput(event);
		requestLayout();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public class TreeContentProvider implements ITreeContentProvider {
		private static final long serialVersionUID = 1L;

		@Override
	    public boolean hasChildren(Object element) {
	    	if( element instanceof ITetra )
	    		return true;
	    	else if ( element instanceof ITetraNode ) {
	    		ITetraNode<?> tn = (ITetraNode<?>) element;
	    		return !Utils.assertNull( tn.getListeners());
	    	}
		    return true;
	    }

	    @Override
	    public Object getParent(Object element) {
	    	if( element instanceof ITetraNode<?> ) {
	    		ITetraNode<?> tetra = (ITetraNode<?>) element;
	    		return tetra.getParent();
	    	}
	    	return null;
	    }

	    @Override
	    public Object[] getElements(Object inputElement) {
	        return ArrayContentProvider.getInstance().getElements(inputElement);
	    }

	    @Override
	    public Object[] getChildren(Object parentElement) {
	    	if( parentElement instanceof ITetra ) {
	    		ITetra<?> node = (ITetra<?>) parentElement;
	    		return node.getNodes();
	    	}else if( parentElement instanceof ITetraNode ) {
	    		ITetraNode<?> node = (ITetraNode<?>) parentElement;
	    		return node.getListeners();
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
