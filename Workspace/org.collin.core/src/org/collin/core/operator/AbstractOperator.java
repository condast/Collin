package org.collin.core.operator;

import java.util.Calendar;
import java.util.Date;

import org.collin.core.def.ITetraNode;
import org.collin.core.essence.TetraEvent;
import org.collin.core.transaction.TetraTransaction;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public abstract class AbstractOperator<D extends Object> implements IOperator<D> {

	private enum AttributeNames{
		ENABLED,
		TOTAL_TIME;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}
	}

	private ITetraNode<D> source;
	
	private Date start;
	
	private long totalTime;//seconds
	
	private boolean enabled;
	
	public AbstractOperator() {}

	protected Date getStart() {
		return start;
	}

	protected long getTotalTime() {
		return totalTime;
	}

	protected ITetraNode<D> getSource() {
		return source;
	}
	
	protected boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setParameters(Attributes attrs) {
		String enabled_str = attrs.getValue( AttributeNames.ENABLED.toXmlStyle());
		this.enabled = StringUtils.isEmpty(enabled_str)?true: Boolean.parseBoolean( enabled_str);
		String tt = attrs.getValue( AttributeNames.TOTAL_TIME.toXmlStyle());
		this.totalTime = StringUtils.isEmpty(tt)?3600: Long.parseLong(tt);
	}

	protected abstract TetraEvent.Results onStart( ITetraNode<D> source, TetraEvent<D> event);

	protected abstract TetraEvent.Results onProgress( ITetraNode<D> source, double progress, TetraEvent<D> event);

	protected abstract TetraEvent.Results onComplete( ITetraNode<D> source, TetraEvent<D> event);

	@Override
	public boolean select(ITetraNode<D> source, TetraEvent<D> event) {
		this.source = source;
		if(!enabled) {
			return source.select(source.getType(), event);
		}
		TetraEvent.Results retval = TetraEvent.Results.FAIL;	
		TetraTransaction<D> transaction = event.getTransaction(); 
		switch( event.getTransaction().getState()) {
		case START:
			start = Calendar.getInstance().getTime();
			retval = onStart(source, event);
			break;
		case PROGRESS:
			retval = onProgress(source, transaction.getProgress(), event);
			break;
		case COMPLETE:
			retval = onComplete(source, event);
			break;
		default:
			break;
		}
		return !TetraEvent.Results.FAIL.equals( retval );
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
