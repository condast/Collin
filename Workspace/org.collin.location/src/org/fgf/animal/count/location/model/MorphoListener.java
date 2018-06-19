package org.fgf.animal.count.location.model;

import java.util.Locale;
import java.util.Scanner;

import org.collin.core.model.ILocaleDescription;
import org.collin.core.model.IMorphoCode;
import org.fgf.animal.count.location.ds.Dispatcher;
import org.fgf.animal.count.location.services.DescriptionService;
import org.fgf.animal.count.location.services.MorphologicalService;

public class MorphoListener {

	public static final String S_RESOURCES = "/resources/Morphology.txt";

	public enum Structure{
		CODE,
		NAME_LATIN,
		NAME_NL,
		ALT_NL,
		NAME_EN,
		SEARCH_GLOBE,
		SEARCH_IVN,
		LENGTH,
		COMP_LEGS,
		WORM_LIKE,
		SNAIL,
		SURFACE,
		LEGS,
		TAIL_EXT,
		MACRO_FAUNA,
		WATER_FLOW,
		WATER_STILL,
		WATER_QUALITY,
		INFO_NL,
		INFO_EN;	
	}

	public MorphoListener() {
		super();
	}

	public void setupTable() {
		Scanner scanner = new Scanner( this.getClass().getResourceAsStream( S_RESOURCES));
		Dispatcher service = Dispatcher.getInstance();
		MorphologicalService ms = new MorphologicalService( service);
		DescriptionService ds = new DescriptionService( service);
		try {
			while( scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] split = line.split("[,]");
				int code = Integer.parseInt( split[Structure.CODE.ordinal()]);
				String name = split[1];
				boolean glb = Boolean.parseBoolean(split[Structure.SEARCH_GLOBE.ordinal()]);
				boolean ivn = Boolean.parseBoolean(split[Structure.SEARCH_IVN.ordinal()]);
				IMorphoCode morpho = ms.create( code, name, glb, ivn );	
				Locale locale = new Locale( "NL-nl"); 
				ILocaleDescription ls = ds.create( morpho, locale, split[Structure.NAME_NL.ordinal()], 
						split[Structure.ALT_NL.ordinal()], 
						split[ Structure.INFO_NL.ordinal()]); 
				morpho.addDescription(locale, ls);
				locale = Locale.ENGLISH; 
				ls = ds.create( morpho, locale, split[Structure.NAME_EN.ordinal()], 
						split[ Structure.INFO_EN.ordinal()]);	
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			scanner.close();
		}
	}
}
