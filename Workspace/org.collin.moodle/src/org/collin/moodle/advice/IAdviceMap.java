package org.collin.moodle.advice;

import org.collin.moodle.advice.IAdvice.AdviceTypes;

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
}