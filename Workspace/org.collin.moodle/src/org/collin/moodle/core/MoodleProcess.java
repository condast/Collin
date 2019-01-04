package org.collin.moodle.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.collin.core.advice.IAdvice;
import org.collin.core.advice.IAdvice.Notifications;
import org.collin.core.impl.SequenceNode;
import org.condast.commons.Utils;
import org.condast.commons.date.DateUtils;

public class MoodleProcess {

	private long userId;
	
	private long moduleId;

	private Date accessed;

	private LinkedHashMap< IAdvice, ProgressData> progress;
	
	public MoodleProcess( long userId, long moduleId ) {
		this.userId = userId;
		this.moduleId = moduleId; 
		this.progress = new LinkedHashMap<>();//keep order of insertion
		this.accessed = Calendar.getInstance().getTime();
	}

	public long getUserId() {
		return userId;
	}

	public long getModuleId() {
		return moduleId;
	}
	
	public IAdvice getRecent() {
		if( Utils.assertNull(this.progress))
			return null;
		List<IAdvice> advice = new ArrayList<>( progress.keySet());
		return advice.get(advice.size()-1); 
	}

	public boolean isDue( IAdvice.AdviceTypes type, int delay ) {
		if( !getRecent().getType().equals(type))
			return true;
		return DateUtils.isOverdue(accessed, Calendar.SECOND, delay );
	}
	
	public void addAdvice( IAdvice advice, SequenceNode node ) {
		this.progress.put( advice, new ProgressData( node));
	}

	public void updateAdvice( long adviceId, Notifications notification ) {
		for( IAdvice advice: this.progress.keySet()) {
			if( advice.getId() != adviceId )
				continue;
			ProgressData data = this.progress.get(advice );
			accessed = Calendar.getInstance().getTime();
			data.setNotification(notification);
		}
	}

	public void removeAdvice( IAdvice advice ) {
		this.progress.remove(advice);
	}
	
	private class ProgressData{
		
		private SequenceNode node;
		private Notifications notification;
		
		public ProgressData(SequenceNode node) {
			super();
			this.node = node;
			this.notification = Notifications.DONT_CARE;
		}
		
		public SequenceNode getNode() {
			return node;
		}
		public Notifications getNotification() {
			return notification;
		}
		
		public void setNotification(Notifications notification) {
			this.notification = notification;
		}
	}
}
