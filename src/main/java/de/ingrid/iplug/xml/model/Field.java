package de.ingrid.iplug.xml.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class Field implements Externalizable {

	private String _fieldName;
	private String _xpath;
	private float _score;
	private Filter _filter;
	private FieldType _fieldType;

	public Field(String fieldName, String xpath, float score, Filter filter,
			FieldType fieldType) {
		_fieldName = fieldName;
		_xpath = xpath;
		_score = score;
		_filter = filter;
		_fieldType = fieldType;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public void setFieldName(String fieldName) {
		_fieldName = fieldName;
	}

	public String getXpath() {
		return _xpath;
	}

	public void setXpath(String xpath) {
		_xpath = xpath;
	}

	public float getScore() {
		return _score;
	}

	public void setScore(float score) {
		_score = score;
	}

	public Filter getFilter() {
		return _filter;
	}

	public void setFilter(Filter filter) {
		_filter = filter;
	}

	public FieldType getFieldType() {
		return _fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		_fieldType = fieldType;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		_fieldName = in.readUTF();
		_xpath = in.readUTF();
		_score = in.readFloat();
		_filter.readExternal(in);
		_fieldType = FieldType.valueOf(in.readUTF());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(_fieldName);
		out.writeUTF(_xpath);
		out.writeFloat(_score);
		_filter.writeExternal(out);
		out.writeUTF(_fieldType.name());
	}

}
