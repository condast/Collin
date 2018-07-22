package org.collin.ui.language;

import org.condast.commons.i18n.Language;

public class CollinLanguage extends Language {

	private static final String S_ID = CollinLanguage.class.getName();
	
	private static CollinLanguage language = new CollinLanguage(S_ID );
	
	private CollinLanguage(String id) {
		super(id, "NL", "nl");
	}

	public static CollinLanguage getInstance() {
		return language;
	}
}
