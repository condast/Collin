package org.collin.moodle.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.collin.core.impl.SequenceNode;
import org.condast.commons.strings.StringStyler;

public class AdviceFactory {

	public static String S_DEFAULT_LOCATION = "/resources/advice/advice.mdl";
	
	private IAdviceMap advice;
	
	public AdviceFactory( IAdviceMap advice ) {
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