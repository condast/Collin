package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.collin.core.def.IDataObject;
import org.collin.core.def.ITetraImplementation;
import org.collin.core.essence.TetraEvent;
import org.collin.core.essence.TetraEvent.Results;
import org.collin.core.impl.SequenceNode;
import org.collin.core.impl.SequenceQuery;
import org.collin.core.impl.SequenceNode.Nodes;
import org.collin.core.task.AbstractTimedDelegate;
import org.collin.core.transaction.TetraTransaction;
import org.collin.moodle.core.Push;
import org.collin.moodle.images.TeamImages.Team;
import org.collin.moodle.xml.ModuleBuilder;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class AdviceManager extends AbstractTimedDelegate<SequenceNode<IAdviceMap>, IAdviceMap> {

	public static final int DEFAULT_LESSON_TIME = 900;
	private LinkedHashMap<Integer, IAdviceMap> advice;

	private long userId;
	private IAdviceMap currentAdvice;

	private int adviceCounter;
	
	private int nominalTimeLesson;
	
	private static Logger logger = Logger.getLogger(AdviceManager.class.getName());

	public AdviceManager(IDataObject<IAdviceMap> data) {
		super(data);
		SequenceNode<IAdviceMap> sequence = (SequenceNode<IAdviceMap>) super.getData();
		String poll_str = sequence.getValue( StringStyler.xmlStyleString(SequenceNode.AttributeNames.POLL_TIME.name()));
		super.setPollTime( StringUtils.isEmpty( poll_str )? DEFAULT_POLL_TIME: Integer.parseInt(poll_str));
		super.setDuration(( sequence.getDuration() <= 0 )? DEFAULT_COUNT: sequence.getDuration());
		SequenceQuery<IAdviceMap> query = new SequenceQuery<IAdviceMap>( sequence.getParent() );
		String str = query.findUpStream( ModuleBuilder.AttributeNames.DURATION.toXmlStyle());
		this.nominalTimeLesson = StringUtils.isEmpty(str)?DEFAULT_LESSON_TIME: Integer.parseInt(str);
		this.advice = new LinkedHashMap<>();
		this.adviceCounter = 0;
	}

	@Override
	protected Results onStart(ITetraImplementation<SequenceNode<IAdviceMap>, IAdviceMap> node,
			TetraEvent<IAdviceMap> event) {
		return super.onStart(node, event);
	}

	protected void pollAdvice( SequenceNode<IAdviceMap> task, TetraTransaction<IAdviceMap> transaction ) {
		IAdviceMap map = transaction.getData();
		this.userId = map.getUserId();
		addAdvice(map);
	}
	
	protected IAdvice createAdvice( SequenceNode<IAdviceMap> node, IAdviceMap adviceMap, IAdvice.AdviceTypes type  ) {
		this.adviceCounter = 0;//An advice has been given, so restart count
		Random random = new Random();
		List<Team> members = new ArrayList<>( EnumSet.allOf(Team.class));
		members.remove(Team.PLUSKLAS);
		Team member = members.get(random.nextInt(members.size()));
		
		int tracking = this.nominalTimeLesson - super.getCounter();
		double actualProgress= 100 * super.getCounter()/this.nominalTimeLesson;
		double diffProgess = adviceMap.getProgress() - actualProgress;
		IAdvice.Mood mood = Advice.getMood(member, type, diffProgess);
		logger.info("Percent complete: " + adviceMap.getProgress() 
		+ "(" + type + ")"	+ ", Progress Tracking: " + diffProgess + ", Member:" + member.toString() + "(" + mood.toString() +")" );

		IAdvice advice = null;
		String description = null;
		SequenceNode<IAdviceMap> adviceNode = getAdviceNode( (SequenceNode<IAdviceMap>) getData(), adviceMap, type);
		description = StringUtils.isEmpty(node.getDescription())? AdviceMap.createDescription( member, type, tracking ): node.getDescription();
		advice =  ( adviceNode == null )? null: new Advice( adviceMap, type, description, member, mood, adviceNode );
		member = getTeam(tracking);
		switch( type ) {
		case PAUSE:
			advice.addNotification( IAdvice.Notifications.PAUSE, Team.getPath(member, mood ));
			advice.addNotification( IAdvice.Notifications.THANKS, Team.getPath(Team.GINO, mood));
			break;
		default:
			IAdvice.Notifications notification = (tracking > 0)? IAdvice.Notifications.PAUSE: IAdvice.Notifications.HELP;
			advice.addNotification( notification, Team.getPath(member, mood ));
			advice.addNotification( IAdvice.Notifications.THANKS, Team.getPath(Team.GINO, mood));
			break;
		}
		return advice;
	}

	protected Team getTeam( double tracking ) {
		Random random = new Random();
		EnumSet<Team> selection = EnumSet.of(Team.CHARLES, Team.AMANDA);
		List<Team> slow = new ArrayList<>( );
		if( tracking < 0) {
			slow.addAll(selection);
		}else
			slow.addAll( EnumSet.complementOf(selection));
		return slow.get(random.nextInt(slow.size()));
	}
	
	protected boolean addAdvice( IAdviceMap advice ) {
		if( advice == null )
			return false;
		this.advice.put( advice.getAdviceId(), advice );
		this.currentAdvice = advice;
		return true;
	}

	protected IAdviceMap updateAdvice( IAdviceMap adviceMap ) {
		logger.info("Notification: " + adviceMap.getNotification() + 
				": " + getActualPauseTime());
		int pause = super.getActualPauseTime();
		switch( adviceMap.getNotification()) {
		case DONT_CARE:
			super.setActualPauseTime(pause + 10);
			break;
		case THANKS:
			super.setActualPauseTime(pause - 10);
			break;
		case PAUSE:
			super.setActualPauseTime(pause - 10);
			super.setPause(true);
			break;
		case HELP:
			super.setActualPauseTime(pause - 10);
			super.setPause(true);
			break;
		default:
			break;
		}
		return this.advice.remove(adviceMap.getAdviceId());
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
			pollAdvice( task, transaction );

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
			setPause(false);
			result = Results.COMPLETE;//the coach has successfully given an advice
			break;
		case UPDATE:
			updateAdvice(adviceMap);
			setPause(false);
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	protected void onPollEvent(int counter, int duration, IDataObject<IAdviceMap> settings) {
		try {
			adviceCounter = ++adviceCounter%duration;
			if( adviceCounter != 0 )
				return;
			if(advice.isEmpty())
				return;
			IAdvice.AdviceTypes type = ( counter < duration )? IAdvice.AdviceTypes.PROGRESS: IAdvice.AdviceTypes.FAIL;
			IAdviceMap map = advice.get( currentAdvice.getAdviceId() ); 
			IAdvice advice = createAdvice( (SequenceNode<IAdviceMap>) settings, map, type);
			if( advice == null )
				return;
			map.addAdvice( advice);
			Push.sendPushMessage(userId, advice);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	@Override
	protected boolean onPauseEvent(int counter, int duration, IDataObject<IAdviceMap> settings) {
		if( currentAdvice == null )
			return false;
		IAdviceMap map = updateAdvice( currentAdvice);
		if( map == null )
			return true;
		IAdvice advice = createAdvice((SequenceNode<IAdviceMap>) settings, map, IAdvice.AdviceTypes.PAUSE);
		if( advice == null )
			return false;
		map.addAdvice( advice);
		Push.sendPushMessage(userId, advice);
		return true;
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
}