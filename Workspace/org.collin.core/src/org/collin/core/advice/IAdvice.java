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

	enum Notifications{
		UNKNOWN(1),
		DONT_CARE(2),
		THANKS(3),
		SHUT_UP(4),
		HELP(5);
	
		public int getIndex() {
			return index;
		}

		private int index;
		
		private Notifications( int index ) {
			this.index = index;
		}
		
		public static Notifications getNotification( int index ) {
			for( Notifications nf: values()) {
				if( nf.getIndex() == index )
					return nf;
			}
			return Notifications.UNKNOWN;
		}
	}

	long getId();
	
	String getMember();

	String getAdvice();

	int getRepeat();

	IAdvice.AdviceTypes getType();

	IAdvice.Mood getMood();

	void setUserId(long userId);

	void setModuleId(long moduleId);

	void setActivityId(long activityId);

	void setProgress(double progress);

	long getUserId();

	long getModuleId();

	long getActivityId();

	double getProgress();

	String getUri();

}