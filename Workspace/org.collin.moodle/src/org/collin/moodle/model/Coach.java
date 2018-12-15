package org.collin.moodle.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.impl.SequenceDelegateFactory;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.Activator;
import org.condast.commons.Utils;
import org.condast.commons.strings.StringUtils;

public class Coach extends AbstractTetraImplementation<String, SequenceNode>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private boolean completed; 

	public Coach(SequenceNode sequence, ITetra<SequenceNode> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory( sequence ));
		this.completed = false;
	}

	@Override
	protected TetraEvent.Results onCallFunction(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		return result;
	}

	@Override
	protected TetraEvent.Results onCallGoal(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		return event.getResult();
	}

	@Override
	protected TetraEvent.Results onCallTask(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event ) {
		this.completed = false;
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		TetraTransaction<SequenceNode> transaction = event.getTransaction();
		switch( transaction.getState()) {
		case START:
			break;
		case PROGRESS:
			FileFilter filter = new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					String name= pathname.getName();
					return name.startsWith( event.getResult().name().toLowerCase());
				}		
			};

			switch( event.getResult()) {
			case SUCCESS:
			case FAIL:
				SequenceQuery query = new SequenceQuery( super.getData());
				SequenceNode sn = query.find(node.getType());
				String url = sn.getUri();
				File file = null;
				try {
					file = Activator.getFileResource(url);
					if( file.isDirectory()) {
						Random random = new Random();
						File[] files =  file.listFiles( filter );
						if( Utils.assertNull(files))
							return result;
						int choice = (int)random.nextInt(files.length);
						file = files[ choice ];
					}
					event.getTransaction().getData().addDatum( StringUtils.getContent(file));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
				this.completed = true;
				result = Results.COMPLETE;//the coach has succesfully given an advice
				break;
			case COMPLETE:
				break;
			default:
				break;
			}
		default:
			break;
		}
 		return result;
	}

	@Override
	protected TetraEvent.Results onCallSolution(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event) {
		logger.info(node.getId() + ": " + event.getTransaction().getState().toString());
		TetraEvent.Results result = completed? TetraEvent.Results.COMPLETE:TetraEvent.Results.FAIL ;
		return result;
	}	

	@Override
	protected TetraEvent.Results onTransactionUpdateRequest(TetraEvent<SequenceNode> event) {
		logger.info(event.getTransaction().getState().toString());
		return TetraEvent.Results.COMPLETE;
	}

	@Override
	protected void onTetraEventReceived(TetraEvent<SequenceNode> event) {
		logger.info(event.getTransaction().getState().toString());
	}	
}
