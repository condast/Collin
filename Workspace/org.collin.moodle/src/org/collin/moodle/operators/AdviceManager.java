package org.collin.moodle.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.task.AbstractTask;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.advice.Advice;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdvice.AdviceTypes;
import org.collin.moodle.advice.IAdvice.Mood;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.core.Dispatcher;
import org.collin.moodle.core.MoodleProcess;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.commons.Utils;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class AdviceManager extends AbstractTask<IAdviceMap> {

	public static final int DEFAULT_COUNT = 120;//two minutes
	public static final int DEFAULT_POLL_TIME = 1000;//1 seconds

	public enum AttributeNames{
		DURATION,
		POLL_TIME;
		
		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}
	private MoodleProcess process;
	
	private List<IAdviceMap> advice;
	
	private long userId;
	
	private Timer timer;
	private int counter;
	private int maxCount;
	private int pollTime;
	private SequenceNode<IAdviceMap> task;
	
	public AdviceManager() {
		this.counter = 0;
		this.maxCount = DEFAULT_COUNT;
		this.pollTime = DEFAULT_POLL_TIME;
		this.advice = new ArrayList<>();
		timer = new Timer();
	}

	/**
	 * Initialize the task with the given attributes
	 * @param attributes
	 */
	@Override
	public void setParameters(Attributes attrs) {
		super.setParameters(attrs);
		String str = attrs.getValue( AttributeNames.POLL_TIME.toXmlStyle());
		this.pollTime = StringUtils.isEmpty(str)?DEFAULT_POLL_TIME: Integer.valueOf(str);
		str = attrs.getValue( AttributeNames.DURATION.toXmlStyle());
		this.maxCount = StringUtils.isEmpty(str)?DEFAULT_COUNT: Integer.valueOf( str );
	}

	public void start( SequenceNode<IAdviceMap> task, TetraTransaction<IAdviceMap> transaction ) {
		this.task = task;
		IAdviceMap map = transaction.getData();
		this.userId = map.getUserId();
		timer.schedule(new PollTask(), pollTime, pollTime);
		process = new MoodleProcess( userId, map.getModuleId() );
	}
		
	public IAdvice createAdvice( SequenceNode<IAdviceMap> node, IAdviceMap adviceMap, IAdvice.AdviceTypes type  ) {
		this.counter = 0;//An advice has been given, so restart count
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
	
	private class PollTask extends TimerTask {

		@Override
		public void run() {
			counter = ++counter%maxCount;
			if( counter != 0 )
				return;
			Dispatcher dispatcher = Dispatcher.getInstance();
			dispatcher.sendPushMessage(userId, new Advice(Team.RUBEN.name(), AdviceTypes.FAIL, Mood.NERVOUS, "hoi", 20 ));
		};
	}
}
