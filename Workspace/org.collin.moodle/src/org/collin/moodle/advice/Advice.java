package org.collin.moodle.advice;

import org.collin.core.impl.SequenceNode;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class Advice implements IAdvice {

	private String member;
	private String advice;
	private int repeat;
	private IAdvice.AdviceTypes type;
	private IAdvice.Mood mood;
	private String uri;
	
	public Advice(SequenceNode<IAdviceMap> node) {
		super();
		String str =  node.getValue( StringStyler.styleToEnum( IAdvice.Attributes.MEMBER.name()));
		this.member = StringUtils.isEmpty(str)? Team.GINO.name(): Team.valueOf( str ).name();
		this.type = AdviceTypes.valueOf(node.getType());
		this.mood = getMood(type);
		this.advice = node.getDescription();
		str =  node.getValue( StringStyler.styleToEnum( IAdvice.Attributes.REPEAT.name()));
		this.repeat = StringUtils.isEmpty(str)?0: Integer.parseInt(str);
		this.uri = node.getUri();
	}

	public Advice( String[] arr) {
		this( arr[0], IAdvice.AdviceTypes.valueOf( arr[1].trim().toUpperCase()), IAdvice.Mood.valueOf(arr[2].trim().toUpperCase()), arr[3], Integer.parseInt( arr[4].trim() ));
	}
	
	public Advice( String member, IAdvice.AdviceTypes type, Mood mood, String advice, int repeat) {
		super();
		this.member = member;
		this.type = type;
		this.mood = mood;
		this.advice = advice;
		this.repeat = repeat;
	}

	@Override
	public long getId() {
		return this.type.ordinal();
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

	@Override
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public static Mood getMood( IAdvice.AdviceTypes type ) {
		Mood mood = Mood.DOUBT;
		switch( type){
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

}
