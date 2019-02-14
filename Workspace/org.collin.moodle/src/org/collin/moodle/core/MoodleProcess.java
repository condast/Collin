package org.collin.moodle.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.collin.core.impl.SequenceNode;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;
import org.condast.commons.Utils;
import org.condast.commons.date.DateUtils;

public class MoodleProcess {

	private long userId;
	
	private long moduleId;

	private Date accessed;

	private LinkedHashMap< IAdviceMap, ProgressData> progress;
	
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
	
	public IAdviceMap getRecent() {
		if( Utils.assertNull(this.progress))
			return null;
		List<IAdviceMap> advice = new ArrayList<>( progress.keySet());
		return advice.get(advice.size()-1); 
	}

	public boolean isDue( IAdvice.AdviceTypes type, int delay ) {
		if( Utils.assertNull(this.progress))
			return false;
		if( !getRecent().contains(type))
			return true;
		return DateUtils.isOverdue(accessed, Calendar.SECOND, delay );
	}
	
	public void addAdvice( IAdviceMap advice, SequenceNode<IAdviceMap> node ) {
		this.progress.put( advice, new ProgressData( node));
	}

	public void updateAdvice( long adviceId, IAdvice.Notifications notification ) {
		for( IAdviceMap advice: this.progress.keySet()) {
			if( advice.getId() != adviceId )
				continue;
			ProgressData data = this.progress.get(advice );
			accessed = Calendar.getInstance().getTime();
			data.setNotification(notification);
		}
	}

	public void removeAdvice( IAdviceMap advice ) {
		this.progress.remove(advice);
	}
	
	private class ProgressData{
		
		private SequenceNode<IAdviceMap> node;
		private IAdvice.Notifications notification;
		
		public ProgressData(SequenceNode<IAdviceMap> node) {
			super();
			this.node = node;
			this.notification = IAdvice.Notifications.DONT_CARE;
		}
		
		public SequenceNode<IAdviceMap> getNode() {
			return node;
		}
		public IAdvice.Notifications getNotification() {
			return notification;
		}
		
		public void setNotification(IAdvice.Notifications notification) {
			this.notification = notification;
		}
	}
}
