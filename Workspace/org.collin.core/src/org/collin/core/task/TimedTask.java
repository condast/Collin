package org.collin.core.task;

import java.util.Calendar;
import java.util.Date;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.transaction.TetraTransaction;

public class TimedTask<D extends Object> implements ICollINDelegate<D>{

	private SequenceNode sequence;

	private Date start;
	
	public TimedTask() {
		super();
	}

	public TimedTask(SequenceNode sequence, ITetraNode<D> node ) {
		super();
		this.sequence = sequence;
	}

	@Override
	public Results perform(ITetraNode<D> node, TetraTransaction<SequenceNode> transaction) {
		Results result = Results.COMPLETE;
		switch( transaction.getState()) {
		case START:
			start = ( start == null )? transaction.getCreate(): start;
			break;
		case PROGRESS:
			result = calculate(transaction);			
			break;
		case COMPLETE:
			result = calculate(transaction);			
			break;
		default:
			break;
		}
		return result;
	}
	
	protected Results calculate( TetraTransaction<SequenceNode> transaction) {
		Results result = Results.COMPLETE;
		double progress = transaction.getProgress();
		int time = (int) sequence.getTotalTime();
		Calendar calendar = Calendar.getInstance();
		Date current = calendar.getTime();
		calendar.setTime(start);
		calendar.add(Calendar.SECOND, time);
		Date end = calendar.getTime();
		if( current.before(end)) {
			result = ( progress < 100 )? Results.COMPLETE: Results.SUCCESS;
		}else {
			result = Results.FAIL;
		}	
		return result;
	}
}
