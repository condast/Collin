package org.collin.core.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.collin.core.def.ICollINDelegate;
import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.transaction.TetraTransaction;

public abstract class AbstractTask<T extends Object, D extends IDataObject<T>> implements ICollINDelegate<T,D>{

	private IDataObject<T> sequence;

	private Date start;
	
	public AbstractTask() {
		super();
	}

	public AbstractTask(IDataObject<T> sequence, ITetraNode<D> node ) {
		super();
		this.sequence = sequence;
	}

	protected IDataObject<T> getData() {
		return sequence;
	}

	protected Date getStart() {
		return start;
	}

	protected Date getEndTime() {
		int time = (int) sequence.getTotalTime();
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

	protected static String getContent( File file ) {
		StringBuffer buffer = new StringBuffer();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file );
			while( scanner.hasNextLine())
				buffer.append(scanner.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			scanner.close();
		}
		return buffer.toString();
	}
}
