package org.collin.core.task;

import java.util.Calendar;
import java.util.Date;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.transaction.TetraTransaction;

public abstract class AbstractTask<D extends Object> implements ICollINDelegate<D>{

	public static final int DEFAULT_TIME = 900;//sec
	private IDataObject<D> sequence;

	private Date start;
	
	public AbstractTask() {
		super();
	}

	public AbstractTask(IDataObject<D> sequence, ITetraNode<D> node ) {
		super();
		this.sequence = sequence;
	}

	protected IDataObject<D> getData() {
		return sequence;
	}

	protected Date getStart() {
		return start;
	}

	protected Date getEndTime() {
		int time = (int) sequence.getTotalTime();
		if( time < 0 )
			time = DEFAULT_TIME;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(Calendar.SECOND, time);
		return calendar.getTime();		
	}

	protected Date getCurrentTime() {
		return Calendar.getInstance().getTime();
	}
	
	public double getDifference() {
		long end = getEndTime().getTime();
		long begin = getStart().getTime();
		long current = getCurrentTime().getTime();
		if( current < end )
			return 100d*( begin - current)/( end - begin);
		else
			return 100d*( current - end)/( end - begin);
	}
	protected Results onStart( ITetraNode<D> node, TetraEvent<D> event ) {
		return Results.COMPLETE;
	}
	
	protected abstract Results onProgress( ITetraNode<D> node, TetraEvent<D> event );

	protected abstract Results onComplete( ITetraNode<D> node, TetraEvent<D> event );

	@Override
	public Results perform(ITetraNode<D> node, TetraEvent<D> event) {
		Results result = Results.COMPLETE;
		TetraTransaction<D> transaction = event.getTransaction();
		switch( transaction.getState()) {
		case START:
			start = ( start == null )? transaction.getCreate(): start;
			result = onStart(node, event);
			break;
		case PROGRESS:
			result = onProgress(node, event);			
			break;
		case COMPLETE:
			result = onComplete(node, event);			
			break;
		default:
			break;
		}
		return Results.FAIL;//result;
	}
	
	protected Results calculate( TetraTransaction<D> transaction) {
		Results result = Results.COMPLETE;
		double progress = transaction.getProgress();
		Calendar calendar = Calendar.getInstance();
		Date current = calendar.getTime();
		Date end = getEndTime();
		if( current.before(end)) {
			result = ( progress < 100 )? Results.COMPLETE: Results.SUCCESS;
		}else {
			result = Results.FAIL;
		}	
		return result;
	}
}
