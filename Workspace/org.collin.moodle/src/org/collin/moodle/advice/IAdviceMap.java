package org.collin.moodle.advice;

import java.util.Date;

import org.collin.moodle.advice.AdviceMap.Interactions;
import org.collin.moodle.advice.IAdvice.AdviceTypes;
import org.collin.moodle.advice.IAdvice.Notifications;

public interface IAdviceMap {

	long getId();
	
	long getUserId();

	long getModuleId();

	long getActivityId();

	double getProgress();
	
	IAdvice[] getAdvice();

	void addAdvice(IAdvice advice);

	boolean isEmpty();

	boolean contains(AdviceTypes type);

	Interactions getInteraction();

	long getAdviceId();

	Notifications getNotification();

	Date getCreated();
}