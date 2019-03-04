package org.collin.core.task;

import java.util.Calendar;
import java.util.Date;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.operator.IOperator;
import org.collin.core.transaction.TetraTransaction;
import org.xml.sax.Attributes;

public abstract class AbstractDelegate<N,D extends Object> implements ICollINDelegate<N,D>, IOperator<D>{

	public static final int DEFAULT_TIME = 900;//sec
	private IDataObject<D> sequence;

	private Date start;
	
	public AbstractDelegate() {
		super();
	}

	public AbstractDelegate(IDataObject<D> sequence ) {
		super();
		this.sequence = sequence;
	}
	
	protected IDataObject<D> getData() {
		return sequence;
	}

	
	@Override
	public void setParameters(Attributes attrs) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean select(ITetraNode<D> source, TetraEvent<D> event) {
		return true;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	protected Date getStart() {
		return start;
	}

	protected Date getEndTime() {
		int time = (int) sequence.getDuration();
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
	
	protected double getDifference() {
		return getDifference( getStart(), getEndTime());
	}

	protected double getDifference( int field, int duration ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(field, duration);
		return getDifference( getStart(), calendar.getTime());
	}

	protected double getDifference( Date beginDate, Date endDate) {
		long end = endDate.getTime();
		long begin = beginDate.getTime();
		long current = getCurrentTime().getTime();
		if( current < end )
			return 100d*( begin - current)/( end - begin);
		else
			return 100d*( current - end)/( end - begin);
	}
	protected Results onStart( ITetraImplementation<N,D> node, TetraEvent<D> event ) {
		TetraTransaction<D> transaction = event.getTransaction();
		start = transaction.getCreate();
		return Results.COMPLETE;
	}
	
	protected abstract Results onProgress( ITetraImplementation<N,D> owner, TetraEvent<D> event );

	protected abstract Results onComplete( ITetraImplementation<N,D> owner, TetraEvent<D> event );

	@Override
	public Results perform(ITetraImplementation<N,D> owner, TetraEvent<D> event) {
		Results result = Results.COMPLETE;
		TetraTransaction<D> transaction = event.getTransaction();
		switch( transaction.getState()) {
		case START:
			result = onStart(owner, event);
			break;
		case PROGRESS:
			result = onProgress(owner, event);			
			break;
		case COMPLETE:
			result = onComplete(owner, event);			
			break;
		default:
			break;
		}
		return result;
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
