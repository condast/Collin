package org.collin.moodle.advice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.collin.core.impl.SequenceNode;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class Advice implements IAdvice {

	private long adviceId;
	private long userId;
	private String member;
	private String advice;
	private int repeat;
	private IAdvice.AdviceTypes type;
	private IAdvice.Mood mood;
	private String uri;
	private Map<Notifications, String> notifications;

	public Advice( IAdviceMap advice, IAdvice.AdviceTypes type, String description, Team member, Mood mood, SequenceNode<IAdviceMap> node) {
		this( advice, type, description, member, mood, node.getUri(), 0 );
		String str = node.getValue( StringStyler.styleToEnum( IAdvice.Attributes.REPEAT.name()));
		this.repeat = StringUtils.isEmpty(str)?0: Integer.parseInt(str);
	}
	
	public Advice( IAdviceMap advice, IAdvice.AdviceTypes type, String description, Team member, Mood mood, String uri, int repeat) {
		super();
		this.userId = advice.getUserId();
		this.adviceId = advice.getId();
		this.member = member.name();
		this.type = type;
		this.mood = mood;
		this.advice = description;
		this.repeat = repeat;
		this.uri = uri;
		this.notifications = new HashMap<>();
	}

	public Advice(long userId, long adviceId, String[] arr) {
		this( userId, adviceId, arr[0], IAdvice.AdviceTypes.valueOf( arr[1].trim().toUpperCase()), IAdvice.Mood.valueOf(arr[2].trim().toUpperCase()), arr[3], Integer.parseInt( arr[4].trim() ));
	}
	
	public Advice( long userId, long adviceId, String member, IAdvice.AdviceTypes type, Mood mood, String advice, int repeat) {
		super();
		this.userId = userId;
		this.member = member;
		this.type = type;
		this.adviceId = adviceId;
		this.mood = mood;
		this.advice = advice;
		this.repeat = repeat;
		this.notifications = new LinkedHashMap<>();
	}

	@Override
	public long getId() {
		return this.adviceId;
	}
	
	@Override
	public long getUserId() {
		return this.userId;
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
	public String getDescription() {
		return this.advice;
	}

	@Override
	public void addNotification( IAdvice.Notifications notification, String uri ) {
		this.notifications.put(notification, uri );
	}

	@Override
	public void removeNotification( IAdvice.Notifications notification ) {
		this.notifications.remove(notification);
	}

	public Map<Notifications, String> getNotifications() {
		return notifications;
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

	@Override
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Get the mood for the given team member, for the type (success/fail/progress)
	 * and tracking. Tracking is defined as the difference between the expected
	 * progress and the actual progress (>0 => better than expected, < 0 slower)  
	 * @param member
	 * @param type
	 * @param tracking
	 * @return
	 */
	public static Mood getMood( Team member, IAdvice.AdviceTypes type, double tracking ) {
		Mood mood = Mood.ANIMATED;
		switch( type){
		case SUCCESS:
			switch ( member ) {
			case RUBEN:
				mood = (tracking < -50)?Mood.HAPPY: (tracking<0)? Mood.ANIMATED: (tracking>100)?Mood.ANGRY: (tracking>50)?Mood.DOUBT: Mood.ANIMATED;
				break;
			case NELLY:
				mood = (tracking < -50)?Mood.HAPPY: (tracking<0)? Mood.ANIMATED: (tracking>100)?Mood.NERVOUS: (tracking>50)?Mood.DOUBT: Mood.ANIMATED;
				break;
			case AMANDA:
				mood = (tracking < -50)?Mood.DOUBT: (tracking<0)? Mood.HAPPY: (tracking>100)?Mood.SAD: (tracking>50)?Mood.NERVOUS: Mood.ANIMATED;
				break;
			case CHARLES:
				mood = (tracking < -50)?Mood.NERVOUS: (tracking<0)? Mood.HAPPY: (tracking>100)?Mood.NERVOUS: (tracking>50)?Mood.DOUBT: Mood.ANIMATED;
				break;
			case GINO:
				mood = (tracking <- 50)?Mood.DOUBT: (tracking<0)? Mood.HAPPY: (tracking>100)?Mood.SCARED: (tracking>50)?Mood.NERVOUS: Mood.HAPPY;
				break;
			default:
				break;
			}
			break;
		case FAIL:
			switch ( member ) {
			case RUBEN:
				mood = (tracking > 50)?Mood.ANIMATED: (tracking>0)? Mood.NERVOUS: (tracking>-50)?Mood.DOUBT: Mood.ANGRY;
				break;
			case NELLY:
				mood = (tracking > 50)?Mood.ANIMATED: (tracking>0)? Mood.ANIMATED: (tracking>-50)?Mood.ANIMATED: Mood.DOUBT;
				break;
			case AMANDA:
				mood = (tracking > 50)?Mood.NERVOUS: (tracking>0)? Mood.ANIMATED: (tracking>-50)?Mood.ANIMATED: Mood.SAD;
				break;
			case CHARLES:
				mood = (tracking > 50)?Mood.SCARED: (tracking>0)? Mood.DOUBT: (tracking>-50)?Mood.NERVOUS: Mood.SCARED;
				break;
			case GINO:
				mood = (tracking > 50)?Mood.ANIMATED: (tracking>0)? Mood.HAPPY: (tracking>-50)?Mood.NERVOUS: Mood.SCARED;
				break;
			default:
				break;
			}
			break;
		case PAUSE:
			switch ( member ) {
			case RUBEN:
				mood = (tracking > 50)?Mood.ANIMATED: (tracking>0)? Mood.NERVOUS: (tracking>-50)?Mood.DOUBT: Mood.ANGRY;
				break;
			case NELLY:
				mood = (tracking > 50)?Mood.ANIMATED: (tracking>0)? Mood.ANIMATED: (tracking>-50)?Mood.ANIMATED: Mood.DOUBT;
				break;
			case AMANDA:
				mood = (tracking > 50)?Mood.NERVOUS: (tracking>0)? Mood.ANIMATED: (tracking>-50)?Mood.ANIMATED: Mood.SAD;
				break;
			case CHARLES:
				mood = (tracking > 50)?Mood.SCARED: (tracking>0)? Mood.DOUBT: (tracking>-50)?Mood.NERVOUS: Mood.SCARED;
				break;
			case GINO:
				mood = (tracking > 50)?Mood.ANIMATED: (tracking>0)? Mood.HAPPY: (tracking>-50)?Mood.NERVOUS: Mood.SCARED;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return mood;
	}
}
