package org.collin.moodle.core;

import org.collin.moodle.LanguagePack;
import org.collin.moodle.advice.IAdvice;
import org.collin.moodle.advice.IAdvice.Mood;
import org.collin.moodle.images.TeamImages;
import org.collin.moodle.images.TeamImages.Team;
import org.condast.js.commons.push.PushOptionsBuilder;

public class PushOptionsAdviceBuilder extends PushOptionsBuilder {

	public static final String S_MOODLE_NOTIFICATION = "Moodle Notification";
	public static final String S_ADVICE_TAG = "advice-tag";
	
	public PushOptionsAdviceBuilder() {
		super();
	}

	public byte[] createPayLoad( IAdvice advice, boolean renotify ) {
		LanguagePack language = LanguagePack.getInstance();
		LanguagePack.Fields field = LanguagePack.Fields.getField(advice.getType());

		addOption( Options.TITLE, S_MOODLE_NOTIFICATION);
		addOption( Options.BODY, field.getMessage());
		addOption( Options.DATA, advice );
		addOption( Options.ICON, TeamImages.Team.getPath(advice));
		addOption( Options.BADGE, TeamImages.Team.getPath(Team.PLUSKLAS));
		addOption( Options.TAG, S_ADVICE_TAG);
			
		addOption( Options.VIBRATE, new int[]{500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500});
		addAction( IAdvice.Notifications.HELP.name(), language.getString(IAdvice.Notifications.HELP.toString()), Team.getPath(Team.NELLY, Mood.DOUBT));
		addAction( IAdvice.Notifications.THANKS.name(), language.getString( IAdvice.Notifications.THANKS.toString()), Team.getPath(Team.GINO, Mood.HAPPY));
		return createPayLoad(renotify, false );
	}
}
