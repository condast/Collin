package org.collin.moodle.core;

import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.core.impl.SequenceQuery;

public class Activity {

	public static final String S_ERR_NOT_AN_ACTIVIY = "The rpovided node is not an activity";
	private int moduleId;
	private int activityId;
	
	private double progress;
	
	public Activity( SequenceNode<IAdviceMap> activity ) {
		if( !Nodes.ACTIVITY.equals(activity.getNode()))
			throw new IllegalArgumentException();
		SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>( activity );
		activityId = Integer.parseInt(activity.getId());
		SequenceNode<IAdviceMap> module = query.searchParent(Nodes.MODULE, activity);
		moduleId = Integer.parseInt(module.getId());
	}

	public int getModuleId() {
		return moduleId;
	}

	public int getActivityId() {
		return activityId;
	}

	public double getProgress() {
		return progress;
	}

}
