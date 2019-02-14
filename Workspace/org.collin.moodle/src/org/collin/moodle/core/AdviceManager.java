package org.collin.moodle.core;

import java.util.HashMap;
import java.util.Map;

import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class AdviceManager {

	public static final int DEFAULT_DELAY = 120;//sec
	
	private Map<Long, MoodleProcess> process;

	public AdviceManager( ) {
		process = new HashMap<>();
	}
	
	public void start( long userId, long moduleId ) {
		process.put(userId, new MoodleProcess( userId, moduleId ));
	}
	
	public IAdvice createAdvice( SequenceNode<IAdviceMap> node, IAdviceMap adviceMap, IAdvice.AdviceTypes type  ) {
		SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>( node );
		SequenceNode<IAdviceMap> task = query.getTetra(adviceMap.getModuleId(), adviceMap.getActivityId(), Nodes.TASK);
		IAdvice advice = null;
		for( SequenceNode<IAdviceMap> child: task.getChildren() ) {
			if( type.equals( IAdvice.AdviceTypes.valueOf( child.getType()))) {
				advice = new Advice( child );
				adviceMap.addAdvice( advice);
			}
		}
		return advice;
	}
	
	public boolean addAdvice( long userId, IAdviceMap advice ) {
		if( advice == null )
			return false;
		MoodleProcess mprocess = process.get(userId);
 		mprocess.addAdvice( advice, null );	
 		return true;
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
	
	private class Advice implements IAdvice{

		private SequenceNode<IAdviceMap> node;
		
		public Advice(SequenceNode<IAdviceMap> node) {
			super();
			this.node = node;
		}

		@Override
		public long getId() {
			return Long.parseLong( node.getId());
		}

		@Override
		public String getMember() {
			String str =  node.getValue( StringStyler.styleToEnum( IAdvice.Attributes.MEMBER.name()));
			return StringUtils.isEmpty(str)? Team.GINO.name(): Team.valueOf( str ).name();
		}

		@Override
		public String getDescription() {
			return node.getDescription();
		}

		@Override
		public int getRepeat() {
			String str =  node.getValue( StringStyler.styleToEnum( IAdvice.Attributes.REPEAT.name()));
			return StringUtils.isEmpty(str)?0: Integer.parseInt(str);
		}

		@Override
		public AdviceTypes getType() {
			return AdviceTypes.valueOf(node.getType());
		}

		@Override
		public Mood getMood() {
			Mood mood = Mood.DOUBT;
			switch( getType()){
			case SUCCESS:
				mood = Mood.HAPPY;
				break;
			case FAIL:
				mood = Mood.NERVOUS;
				break;
			default:
				break;
			}
			return mood;
		}

		@Override
		public String getUri() {
			return node.getUri();
		}
		
	}
}
