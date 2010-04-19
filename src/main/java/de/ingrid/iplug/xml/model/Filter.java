package de.ingrid.iplug.xml.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class Filter implements Externalizable {

	public static enum FilterType {
		GREATER_THAN, LOWER_THAN, CONTAINS, NOT_CONTAINS, EQUAL, NOT_EQUAL
	}

	private Comparable<? extends Serializable> _expression;

	private FilterType _filterType = FilterType.GREATER_THAN;

	public Filter() {
		// externalizable
	}

	public Filter(Comparable<? extends Serializable> expression,
			FilterType filterType) {
		_expression = expression;
		_filterType = filterType;
	}

	/**
	 * Get expression.
	 * 
	 * @return
	 * 		Expression.
	 */
	public Comparable<? extends Serializable> getExpression() {
		return _expression;
	}

	/**
	 * Get filter type.
	 * 
	 * @return
	 * 		Filter type.
	 */
	public FilterType getFilterType() {
		return _filterType;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		_filterType = FilterType.valueOf(in.readUTF());
		_expression = (Comparable<? extends Serializable>) in.readObject();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(_filterType.name());
		out.writeObject(_expression);
	}

}
