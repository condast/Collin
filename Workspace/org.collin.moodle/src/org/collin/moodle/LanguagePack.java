package org.collin.moodle;

import java.util.Random;

import org.collin.moodle.advice.IAdvice;
import org.condast.commons.i18n.Language;

public class LanguagePack extends Language {
	
	private static final String S_LANGUAGE = "MoodleLanguage";

	public enum Fields{
		TITLE,
		SAYS,
		FAIL1,
		FAIL2,
		FAIL3,
		SUCCESS1,
		SUCCESS2,
		SUCCESS3,
		RUBEN_FAIL_100,
		RUBEN_FAIL_50,
		RUBEN_FAIL,
		RUBEN_FAIL_M50,
		RUBEN_FAIL_M100,
		RUBEN_SUCCESS_100,
		RUBEN_SUCCESS_50,
		RUBEN_SUCCESS,
		RUBEN_SUCCESS_M50,
		RUBEN_SUCCESS_M100,
		AMANDA_FAIL_100,
		AMANDA_FAIL_50,
		AMANDA_FAIL,
		AMANDA_FAIL_M50,
		AMANDA_FAIL_M100,
		AMANDA_SUCCESS_100,
		AMANDA_SUCCESS_50,
		AMANDA_SUCCESS,
		AMANDA_SUCCESS_M50,
		AMANDA_SUCCESS_M100,
		CHARLES_FAIL_100,
		CHARLES_FAIL_50,
		CHARLES_FAIL,
		CHARLES_FAIL_M50,
		CHARLES_FAIL_M100,
		CHARLES_SUCCESS_100,
		CHARLES_SUCCESS_50,
		CHARLES_SUCCESS,
		CHARLES_SUCCESS_M50,
		CHARLES_SUCCESS_M100,
		GINO_FAIL_100,
		GINO_FAIL_50,
		GINO_FAIL,
		GINO_FAIL_M50,
		GINO_FAIL_M100,
		GINO_SUCCESS_100,
		GINO_SUCCESS_50,
		GINO_SUCCESS,
		GINO_SUCCESS_M50,
		GINO_SUCCESS_M100,
		NELLY_FAIL_100,
		NELLY_FAIL_50,
		NELLY_FAIL,
		NELLY_FAIL_M50,
		NELLY_FAIL_M100,
		NELLY_SUCCESS_100,
		NELLY_SUCCESS_50,
		NELLY_SUCCESS,
		NELLY_SUCCESS_M50,
		NELLY_SUCCESS_M100;
				
		@Override
		public String toString() {
			return getInstance().getString( this);
		}
		
		public String getMessage() {
			return getInstance().getMessage( this );
		}		

		public static Fields getField( IAdvice.AdviceTypes type ) {
			Random random = new Random();
			int value = 1 + random.nextInt(2);
			String str = type.name() + value;
			return Fields.valueOf(str);
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
