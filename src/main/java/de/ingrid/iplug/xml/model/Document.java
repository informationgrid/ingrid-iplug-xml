package de.ingrid.iplug.xml.model;

import java.util.ArrayList;
import java.util.List;

public class Document {

	private String _fileName;
	private String _rootExpression = "@*|node()";
	private String _description;
	
	public String getRootExpression() {
		return _rootExpression;
	}

	public void setRootExpression(String rootExpression) {
		_rootExpression = rootExpression;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	private List<Field> _fields = new ArrayList<Field>();

	public String getFileName() {
		return _fileName;
	}

	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	public List<Field> getFields() {
		return _fields;
	}

	public void setFields(List<Field> fields) {
		_fields = fields;
	}

	public void addField(Field field) {
		_fields.add(field);
	}

}
