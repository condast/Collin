package org.collin.moodle.operators;

import java.util.ArrayList;
import java.util.List;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.task.AbstractTask;
import org.collin.moodle.advice.Advice;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.core.MoodleProcess;
import org.condast.commons.Utils;

public class AdviceManager extends AbstractTask<IAdviceMap> {

	public static final int DEFAULT_DELAY = 120;//sec
	
	private MoodleProcess process;
	
	private List<IAdviceMap> advice;

	public AdviceManager( ) {
		this.advice = new ArrayList<>();
	}
	
	public void start( long userId, long moduleId ) {
		process = new MoodleProcess( userId, moduleId );
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
	
	public boolean addAdvice( IAdviceMap advice ) {
		if( advice == null )
			return false;
		this.advice.add( advice );
 		process.addAdvice( advice, null );	
 		return true;
	}
	
	public MoodleProcess getAdvice() {
		return this.process;
	}
	
	public double getProgress() {
		if( Utils.assertNull(advice))
			return 0;
		return advice.get( this.advice.size()-1).getProgress();
	}

	public void updateAdvice( long adviceId, IAdvice.Notifications notification ) throws Exception {
		try {
 			process.updateAdvice(adviceId, notification);
 		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	@Override
	protected Results onProgress(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Results onComplete(ITetraNode<IAdviceMap> node, TetraEvent<IAdviceMap> event) {
		// TODO Auto-generated method stub
		return null;
	}
}
