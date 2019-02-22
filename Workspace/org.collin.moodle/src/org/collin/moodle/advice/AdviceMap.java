package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

public class AdviceMap implements IAdviceMap {

	public enum Interactions{
		CREATE,
		UPDATE;
	}
	
	private long userId;
	private int adviceId;
	private Date created;
	private IAdvice.Notifications notification;
	
	private long moduleId;
	private long activityId; 
	private double progress;
	private Interactions interaction;
	
	private Collection<IAdvice> advices;

	public AdviceMap( long userId, int adviceId ) {
		this( userId, adviceId, IAdvice.Notifications.UNKNOWN );
	}
	
	public AdviceMap( long userId, int adviceId, IAdvice.Notifications notification ) {
		this( userId, 0, 0, 0);
		this.adviceId = adviceId;
		this.notification = notification;
		this.interaction = Interactions.UPDATE;
	}
	
	public AdviceMap( long userId, long moduleId, long activityId, double progress ) {
		this.userId = userId;
		Random random = new Random();
		this.adviceId = random.nextInt();
		this.created = Calendar.getInstance().getTime();
		this.notification = IAdvice.Notifications.UNKNOWN;
		this.moduleId = moduleId;
		this.activityId = activityId;
		this.progress = progress;
		this.interaction = Interactions.CREATE;
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
	public long getAdviceId() {
		return adviceId;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public Interactions getInteraction() {
		return interaction;
	}

	@Override
	public IAdvice.Notifications getNotification() {
		return notification;
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
