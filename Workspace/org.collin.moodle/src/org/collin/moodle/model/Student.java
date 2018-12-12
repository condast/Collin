package org.collin.moodle.model;

import java.util.logging.Logger;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.ITetra;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.AbstractTetraImplementation;
import org.collin.core.transaction.TetraTransaction;
import org.collin.core.xml.SequenceNode;

public class Student extends AbstractTetraImplementation<SequenceNode>{

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private SequenceNode node;

	public Student(SequenceNode node, ITetra<SequenceNode> tetra) {
		super(tetra);
		this.node = node;
	}

	@Override
	protected TetraEvent.Results onNodeChange(ITetraNode<SequenceNode> node, TetraTransaction<SequenceNode> event) {
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		switch( event.getState()) {
		case START:
			break;
		case PROGRESS:
			switch( node.getType()) {
			case TASK:
				//result = event.isFinished();
				break;
			case SOLUTION:
				break;
			default:
				logger.info( "UPDATING TETRA: "+ node.getType().toString() + ":  " + event.getState().toString());
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
	protected TetraEvent.Results onTransactionUpdateRequest(TetraTransaction<SequenceNode> event) {
		logger.info(event.getState().toString());
		return Results.COMPLETE;
	}

	@Override
	protected void onTetraEventReceived(TetraTransaction<SequenceNode> event) {
		logger.info(event.getState().toString());
	}		
}
