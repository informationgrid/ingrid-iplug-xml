package de.ingrid.iplug.xml.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class Field implements Externalizable {

	private String _fieldName;
	private String _xpath;
	private float _score;
	private List<Filter> _filters = new ArrayList<Filter>();
	private FieldType _fieldType;

	public Field(String fieldName, String xpath, float score, FieldType fieldType) {
		_fieldName = fieldName;
		_xpath = xpath;
		_score = score;
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

	public List<Filter> getFilters() {
		return _filters;
	}
	
	public void addFilter(Filter filter){
		_filters.add(filter);
	}

	public void setFilters(List<Filter> filters) {
		_filters = filters;
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
		_fieldType = FieldType.valueOf(in.readUTF());
		_filters.clear();
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			Filter filter = new Filter();
			filter.readExternal(in);
			_filters.add(filter);
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(_fieldName);
		out.writeUTF(_xpath);
		out.writeFloat(_score);
		out.writeInt(_filters.size());
		for (Filter filter : _filters) {
			filter.writeExternal(out);
		}
		out.writeUTF(_fieldType.name());
	}

}
