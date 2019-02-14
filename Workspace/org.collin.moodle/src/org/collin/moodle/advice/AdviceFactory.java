package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.collin.core.impl.SequenceNode;
import org.condast.commons.strings.StringStyler;

public class AdviceFactory {

	public static String S_DEFAULT_LOCATION = "/resources/advice/advice.mdl";
	
	private Collection<AdviceMap> advices;
	private IAdviceMap advice;
	
	public AdviceFactory( IAdviceMap advice ) {
		advices = new ArrayList<>();
		this.advice = advice;
	}
	
	protected String getAdviceURI(SequenceNode<IAdviceMap> root, IAdvice.AdviceTypes type, int index ) {
		List<SequenceNode<IAdviceMap>> children = root.getChildren();
		List<SequenceNode<IAdviceMap>> temp = new ArrayList<>();
		for( SequenceNode<IAdviceMap> nd: children ) {
			String type_str = StringStyler.styleToEnum(nd.getType());
			IAdvice.AdviceTypes nttype = IAdvice.AdviceTypes.valueOf(type_str);
			if(! nttype.equals(type ))
				continue;
			temp.add(nd);
		}
		if( temp.isEmpty())
			return null;
		int select = (index >= temp.size())?0: index;
		return temp.get(select).getUri();
	}
	
	public void load( Class<?> clss, String path ) {
		Scanner scanner = new Scanner( clss.getResourceAsStream(path));
		try{
			int index = 0;
			while( scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if( line.startsWith("#"))
					continue;
				String[] split = line.split("[,;]");
				AdviceMap advice = null;//new AdviceMap( split ); 
				//advice.setUri( getAdviceURI(advice.getType(), index));
				advices.add( advice);
			}
		}
		finally {
			scanner.close();
		}
	}
	
	public Map<Long, IAdvice> getAdvice( IAdvice.AdviceTypes type ) {
		Map<Long, IAdvice> results = new HashMap<>();
		if( type == null )
			return results;
		for( IAdvice advice: advice.getAdvice() ) {			
			if( advice.getType().equals( type ))
				results.put(advice.getId(), advice);
		}
		return results;
	}
}