package org.collin.moodle.core;

import java.util.HashMap;
import java.util.Map;

import org.collin.core.advice.IAdvice;
import org.collin.core.advice.IAdvice.Notifications;
import org.collin.core.impl.SequenceNode;

public class MoodleProcess {

	private long userId;
	
	private long moduleId;
	
	private Map< IAdvice, ProgressData> progress;
	
	public MoodleProcess( long userId, long moduleId ) {
		this.userId = userId;
		this.moduleId = moduleId; 
		this.progress = new HashMap<>();
	}

	public long getUserId() {
		return userId;
	}

	public long getModuleId() {
		return moduleId;
	}

	public void addAdvice( IAdvice advice, SequenceNode node ) {
		this.progress.put( advice, new ProgressData( node));
	}

	public void updateAdvice( long adviceId, Notifications notification ) {
		for( IAdvice advice: this.progress.keySet()) {
			if( advice.getId() != adviceId )
				continue;
			ProgressData data = this.progress.get(advice );
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
