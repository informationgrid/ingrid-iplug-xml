/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2023 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
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
import java.util.ArrayList;
import java.util.List;

/**
 * Definition of field.
 *
 */
public class Field implements Externalizable {

	private String _fieldName;
	private String _xpath;
	private float _score;
	private List<Filter> _filters = new ArrayList<Filter>();
	private FieldType _fieldType;

	public Field() {
		// externalizable
	}

	public Field(String fieldName, String xpath, float score,
			FieldType fieldType) {
		_fieldName = fieldName;
		_xpath = xpath;
		_score = score;
		_fieldType = fieldType;
	}

	/**
	 * Get field name. 
	 * 
	 * @return
	 * 		Field name. 
	 */
	public String getFieldName() {
		return _fieldName;
	}

	/**
	 * Set field name.
	 * 
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		_fieldName = fieldName;
	}

	/**
	 * Get xPath.
	 * 
	 * @return
	 * 		xPath.
	 */
	public String getXpath() {
		return _xpath;
	}

	/**
	 * Set xPath.
	 * 
	 * @param xpath
	 */
	public void setXpath(String xpath) {
		_xpath = xpath;
	}

	/**
	 * Get score.
	 * 
	 * @return
	 * 		Score.
	 */
	public float getScore() {
		return _score;
	}

	/**
	 * Set score.
	 * 
	 * @param score
	 */
	public void setScore(float score) {
		_score = score;
	}

	/**
	 * Get filters.
	 * 
	 * @return
	 * 		List of filters.
	 * 
	 */
	public List<Filter> getFilters() {
		return _filters;
	}

	/**
	 * Add filter.
	 * 
	 * @param filter
	 */
	public void addFilter(Filter filter) {
		_filters.add(filter);
	}

	/**
	 * Set filters.
	 * 
	 * @param filters
	 */
	public void setFilters(List<Filter> filters) {
		_filters = filters;
	}

	/**
	 * Get field type.
	 * 
	 * @return
	 * 		Field type.
	 */
	public FieldType getFieldType() {
		return _fieldType;
	}

	/**
	 * Set field type.
	 * 
	 * @param fieldType
	 */
	public void setFieldType(FieldType fieldType) {
		_fieldType = fieldType;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		_fieldName = in.readUTF();
		_xpath = in.readUTF();
		_score = in.readFloat();
		_fieldType = FieldType.valueOf(in.readUTF());
		_filters.clear();
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			Filter filter = new Filter();
			filter.readExternal(in);
			_filters.add(filter);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(_fieldName);
		out.writeUTF(_xpath);
		out.writeFloat(_score);
		out.writeUTF(_fieldType.name());
		out.writeInt(_filters.size());
		for (Filter filter : _filters) {
			filter.writeExternal(out);
		}
	}

}
