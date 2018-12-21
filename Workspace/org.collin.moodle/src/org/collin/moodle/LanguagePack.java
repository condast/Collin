package org.collin.moodle;

import org.condast.commons.i18n.Language;

public class LanguagePack extends Language {
	
	private static final String S_LANGUAGE = "MoodleLanguage";

	public enum Fields{
		FAIL1,
		FAIL2,
		FAIL3,
		SUCCESS1,
		SUCCESS2,
		SUCCESS3;

		@Override
		public String toString() {
			return getInstance().getString( this);
		}
		
		public String getMessage() {
			return getInstance().getMessage( this );
		}
		
		
	}
	private static LanguagePack language = new LanguagePack();
	
	private LanguagePack() {
		super( S_LANGUAGE, "NL", "nl");
	}
	
	public static LanguagePack getInstance(){
		return language;
	}	
}
