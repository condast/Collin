package org.collin.core.task;

import java.util.Timer;
import java.util.TimerTask;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractTimedDelegate<N,D extends Object> extends AbstractDelegate<N,D> {

	public enum Fields{
		PAUSE;

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}

	public static final int DEFAULT_COUNT = 120;//two minutes
	public static final int DEFAULT_POLL_TIME = 1000;//1 seconds

	private Timer timer;
	private int counter;
	private int duration;
	private int pollTime;
	private boolean pause;

	private int actualPauseTime;
	private int pauseTime;
	private int pauseCounter;

	protected AbstractTimedDelegate(IDataObject<D> data ) {
		this( data, DEFAULT_POLL_TIME, DEFAULT_COUNT );
	}
	
	protected AbstractTimedDelegate(IDataObject<D> data, int pollTime, int duration ) {
		super(data);
		this.counter = 0;
		this.pause = false;
		this.pauseCounter = 0;
		this.pollTime = pollTime;
		this.duration = duration;
		this.pauseTime = 0;
		this.actualPauseTime = 0;
		timer = new Timer();
	}

	/**
	 * Initialize the task with the given attributes
	 * @param attributes
	 */
	@Override
	public void setParameters(IDataObject<D> attrs) {
		String str = attrs.getValue( SequenceNode.AttributeNames.POLL_TIME.toXmlStyle());
		this.pollTime = StringUtils.isEmpty(str)?DEFAULT_POLL_TIME: Integer.valueOf(str);
		str = attrs.getValue( SequenceNode.AttributeNames.DURATION.toXmlStyle());
		this.duration = StringUtils.isEmpty(str)?DEFAULT_COUNT: Integer.valueOf( str );
		str = attrs.getValue( Fields.PAUSE.toXmlStyle());
		this.pauseTime = StringUtils.isEmpty(str)?0: Integer.valueOf( str );
		this.actualPauseTime = this.pauseTime;
	}

	protected int getCounter() {
		return counter;
	}

	protected double getDuration() {
		return duration;
	}

	protected void setDuration(int duration) {
		this.duration = duration;
	}

	protected int getPollTime() {
		return pollTime;
	}

	protected void setPollTime(int pollTime) {
		this.pollTime = pollTime;
	}

	protected boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
		if(!this.pause)
			this.pauseCounter = 0;
	}
	
	protected int getPauseTime() {
		return pauseTime;
	}

	protected int getActualPauseTime() {
		return actualPauseTime;
	}

	protected boolean setActualPauseTime(int actualPauseTime) {
		if( actualPauseTime < this.pauseTime)
			return false;
		this.actualPauseTime = actualPauseTime;
		return true;
	}

	@Override
	protected Results onStart(ITetraImplementation<N, D> node,
			TetraEvent<D> event) {
		timer.schedule(new PollTask(), pollTime, pollTime);
		return super.onStart(node, event);
	}

	@Override
	protected Results onComplete(ITetraImplementation<N, D> node,
			TetraEvent<D> event) {
		this.pause = true;
		this.counter = 0;
		timer.purge();
		timer.cancel();
		return Results.COMPLETE;
	}
	
	/**
	 * Poll on every count of the counter
	 * @param counter
	 * @param duration
	 * @param settings
	 */
	protected abstract void onPollEvent( int counter, int duration, IDataObject<D> settings );

	/**
	 * Returns true if the pausetime is reached
	 * @param counter
	 * @param duration
	 * @param settings
	 */
	protected abstract boolean onPauseEvent( int counter, int duration, IDataObject<D> settings );

	private class PollTask extends TimerTask {

		@Override
		public void run() {
			try {
				pauseCounter++;
				if(!pause && ( pauseCounter >= actualPauseTime )) {
					pause = onPauseEvent(counter, duration, getData());
				}
				if( pause )
					return;
				counter++;
				onPollEvent(counter, duration, getData());
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		};
	}

}