package org.collin.core.task;

import java.util.Calendar;
import java.util.Date;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.transaction.TetraTransaction;

public abstract class AbstractDelegate<N,D extends Object> implements ICollINDelegate<N,D>{

	public static final int DEFAULT_TIME = 900;//sec
	private IDataObject<D> settings;

	private Date start;
	
	public AbstractDelegate() {
		super();
	}

	public AbstractDelegate(IDataObject<D> sequence ) {
		super();
		this.settings = sequence;
	}
	
	protected IDataObject<D> getData() {
		return settings;
	}

	protected Date getStart() {
		return start;
	}

	protected Date getEndTime() {
		int time = (int) settings.getDuration();
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
	
	protected double getDuration() {
		return getDifference( getStart(), getEndTime());
	}

	/**
	 * The actual progress since starting the task 
	 * @return
	 */
	protected double getActualProgress() {
		double total = 1000* getDuration();//seconds
		Calendar calendar = Calendar.getInstance();
		double currentProgress = getDifference(start, calendar.getTime());
		double actualProgress = 100* currentProgress/total;
		return actualProgress;		
	}

	/**
	 * The expected progress is the tracking' is a measure for the progress. If it is 
	 * >= 0:  the task is performing faster than expected
	 * <0: the task is progressing slower than expected.
	 * @param completion (0-100%)
	 * @return
	 */
	protected double getExpectedProgress( double progress) {
		return progress - getActualProgress();		
	}

	protected double getDifference( int field, int duration ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.add(field, duration);
		return getDifference( getStart(), calendar.getTime());
	}

	protected long getDifference( Date beginDate, Date endDate) {
		long end = endDate.getTime();
		long begin = beginDate.getTime();
		return (end - begin);
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
