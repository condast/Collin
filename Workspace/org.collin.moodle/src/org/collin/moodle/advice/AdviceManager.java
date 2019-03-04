package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.task.AbstractDelegate;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.advice.IAdvice.Notifications;
import org.collin.moodle.core.MoodleProcess;
import org.collin.moodle.core.Push;
import org.collin.moodle.images.TeamImages.Team;
import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class AdviceManager extends AbstractDelegate<SequenceNode<IAdviceMap>, IAdviceMap> {

	public static final int DEFAULT_COUNT = 120;//two minutes
	public static final int DEFAULT_POLL_TIME = 1000;//1 seconds

	private MoodleProcess process;

	private LinkedHashMap<Integer, IAdviceMap> advice;

	private long userId;
	private int currentAdvice;

	private Timer timer;
	private int counter;
	private int duration;
	private int pollTime;
	
	private static Logger logger = Logger.getLogger(AdviceManager.class.getName());

	public AdviceManager(IDataObject<IAdviceMap> data) {
		super(data);
		SequenceNode<IAdviceMap> sequence = (SequenceNode<IAdviceMap>) super.getData();
		this.counter = 0;
		String poll_str = sequence.getValue( StringStyler.xmlStyleString(SequenceNode.AttributeNames.POLL_TIME.name()));
		this.pollTime = StringUtils.isEmpty( poll_str )? DEFAULT_POLL_TIME: Integer.parseInt(poll_str);
		this.duration = ( sequence.getDuration() <= 0 )?DEFAULT_COUNT: sequence.getDuration();
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
		String str = attrs.getValue( SequenceNode.AttributeNames.POLL_TIME.toXmlStyle());
		this.pollTime = StringUtils.isEmpty(str)?DEFAULT_POLL_TIME: Integer.valueOf(str);
		str = attrs.getValue( SequenceNode.AttributeNames.DURATION.toXmlStyle());
		this.duration = StringUtils.isEmpty(str)?DEFAULT_COUNT: Integer.valueOf( str );
	}

	/**
	 * The 'tracking' is a measure for the progress. If it is 
	 * >= 0:  student is performing faster than expected
	 * <0: student is progressing slower than expected.
	 * @param progress (0-100%)
	 * @return
	 */
	protected double getExpectedProgress( ) {
		SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>( (SequenceNode<IAdviceMap>) super.getData() );
		SequenceNode<IAdviceMap> course = query.searchParent(Nodes.COURSE, (SequenceNode<IAdviceMap>) super.getData());
		int studyTime = course.getDuration();
		Calendar calendar = Calendar.getInstance();
		//Calculate the expected progress in percent at the time this method is called
		double expectedProgress = ( calendar.getTimeInMillis() - super.getStart().getTime())/(10*studyTime);		
		return expectedProgress;	
	}
	
	
	@Override
	protected Results onStart(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		return super.onStart(node, event);
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
		Random random = new Random();
		List<Team> members = new ArrayList<>( EnumSet.allOf(Team.class));
		members.remove(Team.PLUSKLAS);
		Team member = members.get(random.nextInt(members.size()));
		
		double tracking = getExpectedProgress() - adviceMap.getProgress();//expected progress - current progress
		IAdvice.Mood mood = Advice.getMood(member, type, tracking);
		logger.info("Percent complete" + adviceMap.getProgress() + ", Tracking: " + tracking + ", Member:" + member.toString() + "(" + mood.toString() +")" );
		SequenceNode<IAdviceMap> adviceNode = getAdviceNode(node, adviceMap, type);
		String description = StringUtils.isEmpty(node.getDescription())? AdviceMap.createDescription( member, type, tracking ): node.getDescription();
		return ( adviceNode == null )? null: new Advice( adviceMap.getUserId(), adviceMap.getAdviceId(), description, member, mood, adviceNode );
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

	protected void updateAdvice( int adviceId ) {
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
			SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap> ( node.getSource() );
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
			IAdvice advice = createAdvice( node.getSource(), adviceMap, type );
			if( advice != null )
				adviceMap.addAdvice( advice);
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
	
	/**
	 * Get the advice node for the given type and progress
	 * @param node
	 * @param adviceMap
	 * @param type
	 * @param progress
	 * @return
	 */
	protected static SequenceNode<IAdviceMap> getAdviceNode( SequenceNode<IAdviceMap> node, IAdviceMap adviceMap, IAdvice.AdviceTypes type ) {
		SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>( node );
		SequenceNode<IAdviceMap> task = query.getTetra(adviceMap.getModuleId(), adviceMap.getActivityId(), Nodes.TASK);
		for( SequenceNode<IAdviceMap> prog: task.getChildren() ) {
			String progress_str = prog.getValue(ModuleBuilder.AttributeNames.PERCENT.toXmlStyle());
			
			//Always return the advice if a progress wasn't included
			int percent = StringUtils.isEmpty(progress_str)?Integer.MAX_VALUE: Integer.parseInt(progress_str);
			if( adviceMap.getProgress() > percent )
				continue;
			for( SequenceNode<IAdviceMap> child: prog.getChildren() ) {
				if( type.equals( IAdvice.AdviceTypes.valueOf( child.getType())))
					return child;
			}
		}
		return null;
	}

	private class PollTask extends TimerTask {

		@Override
		public void run() {
			try {
				counter = ++counter%duration;
				if( counter != 0 )
					return;
				if(advice.isEmpty())
					return;
				IAdviceMap map = advice.get( currentAdvice ); 
				IAdvice advice = createAdvice( (SequenceNode<IAdviceMap>) getData(), map, IAdvice.AdviceTypes.FAIL);
				if( advice != null )
					map.addAdvice( advice);
				Push.sendPushMessage(userId, advice);
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		};
	}

}