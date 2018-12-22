package org.collin.core.advice;

import org.condast.commons.strings.StringStyler;

public interface IAdvice {

	enum AdviceTypes{
		FAIL,
		PROGRESS,
		SUCCESS,
	}

	enum Mood{
		ANGRY,
		SCARED,
		HAPPY,
		DOUBT,
		NERVOUS,
		SAD;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	String getMember();

	String getAdvice();

	int getRepeat();

	IAdvice.AdviceTypes getType();

	IAdvice.Mood getMood();

	void setUserId(long userId);

	void setModuleId(long moduleId);

	void setActivityId(long activityId);

	void setProgress(double progress);

}