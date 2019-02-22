package org.collin.moodle.operators;

import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.collin.core.def.ITetraImplementation;
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
import org.collin.moodle.advice.IAdvice.Notifications;
import org.collin.moodle.advice.IAdviceMap;
import org.collin.moodle.core.MoodleProcess;
import org.collin.moodle.core.Push;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class AdviceManager extends AbstractTask<SequenceNode<IAdviceMap>, IAdviceMap> {

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

	private LinkedHashMap<Long, IAdviceMap> advice;

	private long userId;
	private long currentAdvice;

	private Timer timer;
	private int counter;
	private int maxCount;
	private int pollTime;

	public AdviceManager() {
		this.counter = 0;
		this.maxCount = DEFAULT_COUNT;
		this.pollTime = DEFAULT_POLL_TIME;
		this.advice = new LinkedHashMap<>();
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

	protected void start( SequenceNode<IAdviceMap> task, TetraTransaction<IAdviceMap> transaction ) {
		IAdviceMap map = transaction.getData();
		this.userId = map.getUserId();
		timer.schedule(new PollTask(), pollTime, pollTime);
		process = new MoodleProcess( userId, map.getModuleId() );
		addAdvice(map);
	}

	protected IAdvice createAdvice( SequenceNode<IAdviceMap> node, IAdviceMap adviceMap, IAdvice.AdviceTypes type  ) {
		this.counter = 0;//An advice has been given, so restart count
		SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>( node );
		SequenceNode<IAdviceMap> task = query.getTetra(adviceMap.getModuleId(), adviceMap.getActivityId(), Nodes.TASK);
		IAdvice advice = null;
		for( SequenceNode<IAdviceMap> child: task.getChildren() ) {
			if( type.equals( IAdvice.AdviceTypes.valueOf( child.getType()))) {
				advice = new Advice( adviceMap.getUserId(), adviceMap.getAdviceId(), child );
				adviceMap.addAdvice( advice);
			}
		}
		return advice;
	}

	protected boolean addAdvice( IAdviceMap advice ) {
		if( advice == null )
			return false;
		this.advice.put( advice.getAdviceId(), advice );
		this.currentAdvice = advice.getAdviceId();
		process.addAdvice( advice, null );	
		return true;
	}

	public MoodleProcess getAdvice() {
		return this.process;
	}

	protected void updateAdvice( long adviceId ) {
		try {
			IAdviceMap current = this.advice.get(adviceId);
			if(current == null )
				return;
			process.updateAdvice(adviceId, Notifications.THANKS);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	@Override
	protected Results onProgress(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		TetraEvent.Results result = TetraEvent.Results.COMPLETE;
		TetraTransaction<IAdviceMap> transaction = event.getTransaction();
		IAdviceMap adviceMap = transaction.getData(); 
		if( adviceMap == null ) {
			return result;
		}
		switch( adviceMap.getInteraction()) {
		case CREATE:
			SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap> ( node.getData() );
			SequenceNode<IAdviceMap> task = query.getTetra( adviceMap.getModuleId(), adviceMap.getActivityId(), Nodes.TASK );
			start( task, transaction );

			IAdvice.AdviceTypes type = IAdvice.AdviceTypes.SUCCESS;
			switch( event.getResult()) {
			case FAIL:
				type = IAdvice.AdviceTypes.FAIL;
				break;
			case COMPLETE:
				break;
			default:
				break;
			}
			IAdvice advice = createAdvice( node.getData(), adviceMap, type );
			adviceMap.addAdvice(advice);
			//this.completed = ( advice != null );
			result = Results.COMPLETE;//the coach has successfully given an advice
			break;
		case UPDATE:
			updateAdvice(adviceMap.getAdviceId());
			break;
		default:
			break;
		}
		return result;
	}


	@Override
	protected Results onComplete(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		// TODO Auto-generated method stub
		return null;
	}

	private class PollTask extends TimerTask {

		@Override
		public void run() {
			counter = ++counter%maxCount;
			if( counter != 0 )
				return;
			if(advice.isEmpty())
				return;
			IAdviceMap map = advice.get( currentAdvice ); 
			Push.sendPushMessage(userId, new Advice( userId, map.getAdviceId(), Team.RUBEN.name(), AdviceTypes.FAIL, Mood.NERVOUS, "hoi", 20 ));
		};
	}

}