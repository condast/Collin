package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.Collection;

public class AdviceMap implements IAdviceMap {

	private long userId;
	private long moduleId;
	private long activityId; 
	private double progress;
	
	private Collection<IAdvice> advices;

	public AdviceMap( long userId, long moduleId ) {
		this( userId, moduleId, 0, 0);
	}
	
	public AdviceMap( long userId, long moduleId, long activityId, double progress ) {
		this.userId = userId;
		this.moduleId = moduleId;
		this.activityId = activityId;
		this.progress = progress;
		this.advices = new ArrayList<>();
	}
	
	@Override
	public long getId() {
		return this.userId + this.moduleId;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	@Override
	public long getModuleId() {
		return moduleId;
	}

	@Override
	public long getActivityId() {
		return activityId;
	}

	@Override
	public double getProgress() {
		return progress;
	}

	@Override
	public void addAdvice( IAdvice advice ) {
		this.advices.add(advice);
	}
	
	@Override
	public boolean isEmpty() {
		return this.advices.isEmpty();
	}
	
	@Override
	public boolean contains( IAdvice.AdviceTypes type ) {
		for( IAdvice advice: this.advices ) {
			if( type.equals(advice.getType()))
				return true;
		}
		return false;
	}
	
	@Override
	public IAdvice[] getAdvice() {
		return this.advices.toArray( new IAdvice[ this.advices.size()]);
	} 
}
