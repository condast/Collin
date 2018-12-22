package org.collin.moodle.core;

import org.collin.core.advice.IAdvice;
import org.collin.core.advice.IAdvice.Mood;
import org.collin.moodle.LanguagePack;
import org.collin.moodle.images.TeamImages;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.js.commons.push.PushOptionsBuilder;

public class PushOptionsAdviceBuilder extends PushOptionsBuilder {

	public static final String S_MOODLE_NOTIFICATION = "Moodle Notification";
	public static final String S_ADVICE_TAG = "advice-tag";
	
	private LanguagePack language;
	
	public PushOptionsAdviceBuilder() {
		super();
		language = LanguagePack.getInstance();
	}

	public byte[] createPayLoad( IAdvice advice, boolean renotify ) {
		LanguagePack.Fields field = LanguagePack.Fields.valueOf(advice.getAdvice().trim());

		addOption( Options.TITLE, S_MOODLE_NOTIFICATION);
		addOption( Options.BODY, field.getMessage());
		addOption( Options.DATA, advice );
		addOption( Options.ICON, TeamImages.Team.getPath(advice));
		addOption( Options.BADGE, TeamImages.Team.getPath(Team.PLUSKLAS));
		addOption( Options.TAG, S_ADVICE_TAG);
			
		addOption( Options.VIBRATE, new int[]{500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500});
		addAction( LanguagePack.Notifications.SHUT_UP.toString(), LanguagePack.Notifications.SHUT_UP.toString(), Team.getPath(Team.NELLY, Mood.ANGRY));
		addAction( LanguagePack.Notifications.THANKS.toString(), LanguagePack.Notifications.THANKS.toString(), Team.getPath(Team.GINO, Mood.HAPPY));
		return createPayLoad(renotify, false );
	}
}