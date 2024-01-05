/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2024 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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
	@SuppressWarnings("unchecked")
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
