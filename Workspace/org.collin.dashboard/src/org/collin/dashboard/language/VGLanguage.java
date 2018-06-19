package org.collin.dashboard.language;

import org.condast.commons.i18n.Language;

public class VGLanguage extends Language {
	
	private static final String S_LANGUAGE = "VGLanguage";

	private static VGLanguage language = new VGLanguage();
	
	private VGLanguage() {
		super( S_LANGUAGE, "NL", "nl");
	}
	
	public static VGLanguage getInstance(){
		return language;
	}	
}
