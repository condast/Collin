package org.collin.core.advice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

public class AdviceFactory {

	public static String S_DEFAULT_LOCATION = "/resources/advice/advice.mdl";
	
	private Collection<Advice> advices;
	
	public AdviceFactory() {
		advices = new ArrayList<>();
	}
	
	public void load( Class<?> clss, String path ) {
		Scanner scanner = new Scanner( clss.getResourceAsStream(path));
		try{
			while( scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if( line.startsWith("#"))
					continue;
				String[] split = line.split("[,;]");
				advices.add( new Advice( split ));
			}
		}
		finally {
			scanner.close();
		}
	}
	
	public IAdvice[] getAdvice( IAdvice.AdviceTypes type ) {
		if( type == null )
			return new Advice[0];
		Collection<IAdvice> results = new ArrayList<>();
		for( Advice advice: advices ) {			
			if( advice.getType().equals( type ))
				results.add(advice);
		}
		return results.toArray( new IAdvice[results.size()]);
	}
	
	private class Advice implements IAdvice{
		
		private long adviceId;
		private long userId;
		private long moduleId;
		private long activityId; 
		private double progress;
		
		private String member;
		private String advice;
		private int repeat;
		private IAdvice.AdviceTypes type;
		private IAdvice.Mood mood;

		public Advice( String[] arr) {
			this( createAdviceId(), arr[0], IAdvice.AdviceTypes.valueOf( arr[1].trim().toUpperCase()), IAdvice.Mood.valueOf(arr[2].trim().toUpperCase()), arr[3], Integer.parseInt( arr[4].trim() ));
		}
		
		public Advice( long adviceId, String member, IAdvice.AdviceTypes type, Mood mood, String advice, int repeat) {
			super();
			this.adviceId = adviceId;
			this.member = member;
			this.type = type;
			this.mood = mood;
			this.advice = advice;
			this.repeat = repeat;
		}

		@Override
		public long getId() {
			return adviceId;
		}

		@Override
		public long getUserId() {
			return userId;
		}

		@Override
		public void setUserId(long userId) {
			this.userId = userId;
		}

		@Override
		public long getModuleId() {
			return moduleId;
		}

		@Override
		public void setModuleId(long moduleId) {
			this.moduleId = moduleId;
		}

		@Override
		public long getActivityId() {
			return activityId;
		}

		@Override
		public void setActivityId(long activityId) {
			this.activityId = activityId;
		}
		
		@Override
		public double getProgress() {
			return progress;
		}

		@Override
		public void setProgress(double progress) {
			this.progress = progress;
		}

		/* (non-Javadoc)
		 * @see org.collin.moodle.core.IAdvice#getMember()
		 */
		@Override
		public String getMember() {
			return member;
		}

		/* (non-Javadoc)
		 * @see org.collin.moodle.core.IAdvice#getAdvice()
		 */
		@Override
		public String getAdvice() {
			return advice;
		}

		/* (non-Javadoc)
		 * @see org.collin.moodle.core.IAdvice#getRepeat()
		 */
		@Override
		public int getRepeat() {
			return repeat;
		}

		/* (non-Javadoc)
		 * @see org.collin.moodle.core.IAdvice#getType()
		 */
		@Override
		public IAdvice.AdviceTypes getType() {
			return type;
		}

		@Override
		public IAdvice.Mood getMood() {
			return mood;
		}
	}
	
	public static long createAdviceId() {
		Random rand = new Random();
		return rand.nextLong();
	}
}
