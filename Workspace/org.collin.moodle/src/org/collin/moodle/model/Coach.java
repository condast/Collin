package org.collin.moodle.model;

import java.io.File;
import java.io.FileFilter;
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
import org.condast.commons.strings.StringUtils;

public class Coach extends AbstractTetraImplementation<String, SequenceNode>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public Coach(SequenceNode sequence, ITetra<SequenceNode> tetra) {
		super(tetra, sequence, new SequenceDelegateFactory( sequence ));
	}

	@Override
	protected TetraEvent.Results onNodeChange(ITetraNode<SequenceNode> node, TetraEvent<SequenceNode> event ) {
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		TetraTransaction<SequenceNode> transaction = event.getTransaction();
		switch( transaction.getState()) {
		case START:
			break;
		case PROGRESS:
			switch( node.getType()) {
			case GOAL:
				result = event.getResult();
				break;
			case TASK:
				FileFilter filter = new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						return pathname.getName().startsWith( event.getResult().name().toLowerCase());
					}
					
				};

				switch( event.getResult()) {
				case SUCCESS:
				case FAIL:
					SequenceQuery query = new SequenceQuery( super.getData());
					SequenceNode sn = query.find(node.getType());
					String url = sn.getUri();
					File file = Activator.getContext().getDataFile(url);
					if( file.isDirectory()) {
						Random random = new Random();
						int choice = (int)random.nextInt( file.listFiles( filter ).length);
						file = file.listFiles()[ choice ];
					}
					super.getData().addDatum( StringUtils.getContent(file));
					break;
				default:
					break;
				}
				break;				
			case SOLUTION:
				break;
			default:
				logger.info( "UPDATING TETRA: "+ node.getType().toString() + ":  " + transaction.getState().toString());
				break;
			}
			break;
		case COMPLETE:
			break;
		default:
			break;
		}
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
