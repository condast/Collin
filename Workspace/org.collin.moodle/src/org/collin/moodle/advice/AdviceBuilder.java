package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.collin.core.impl.SequenceNode;

public class AdviceBuilder {

	private static final int DEFAULT_PAUSE_TIME = 240;//seconds
	
	private Date create;
	private int delay;

	public AdviceBuilder() {
		this( DEFAULT_PAUSE_TIME );
	}
	
	public AdviceBuilder( int delay ) {
		this.delay = delay;
		create = Calendar.getInstance().getTime();
	}

	public IAdviceMap buildAdvice( SequenceNode<IAdviceMap> result, long id, long activityId, long moduleId, double progress) {
		if( result == null )
			return null;
		List<IAdviceMap> data = new ArrayList<>( result.getData() );
		if( data.isEmpty())
			return null;
		
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.setTime(create);
		calendar.add(Calendar.SECOND, delay);
		if( now.before(calendar.getTime()) )
			return null;
		Random random = new Random();
		IAdviceMap advice = data.get( random.nextInt(data.size()));
		//advice.setUserId( id );
		//advice.setModuleId(moduleId);
		//advice.setActivityId(activityId);
		//advice.setProgress(progress);
		create = Calendar.getInstance().getTime();
		return advice;
	}
}