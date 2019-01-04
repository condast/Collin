package org.collin.moodle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.collin.core.advice.AdviceFactory;
import org.collin.core.advice.IAdvice;
import org.collin.core.impl.SequenceNode;

public class AdviceManager {

	public static final int DEFAULT_DELAY = 120;//sec
	
	private Map<Long, MoodleProcess> process;

	public AdviceManager() {
		process = new HashMap<>();
	}
	
	public void start( long userId, long moduleId ) {
		process.put(userId, new MoodleProcess( userId, moduleId ));
	}
	
	public IAdvice createAdvice( long userId, SequenceNode node, IAdvice.AdviceTypes type  ) {
		MoodleProcess mp = this.process.get(userId );
		if( !mp.isDue(type, DEFAULT_DELAY))
			return null;

		IAdvice recent = mp.getRecent();
		AdviceFactory factory = new AdviceFactory( node );
		factory.load( this.getClass(), AdviceFactory.S_DEFAULT_LOCATION);
		Map<Long, IAdvice> results = factory.getAdvice( type );
		if( results.size() > 1 )
			results.remove(recent.getId());
		List<IAdvice> advice = new ArrayList<>( results.values());
		Random random = new Random();
		int choice = (int)random.nextInt(advice.size());
		return advice.get(choice );		
	}
	
	public void addAdvice( long userId, SequenceNode node ) {
			MoodleProcess mprocess = process.get(userId);
 			mprocess.addAdvice( node.getData().iterator().next(), node);				
	}
	
	public MoodleProcess getAdvice( long userId ) {
		return this.process.get(userId);
	}

	public void updateAdvice( long userId, long adviceId, IAdvice.Notifications notification ) throws Exception {
		try {
 			MoodleProcess mprocess = this.process.get(userId);
 			mprocess.updateAdvice(adviceId, notification);
 		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
}
