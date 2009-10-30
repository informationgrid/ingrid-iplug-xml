package de.ingrid.iplug.xml.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class Document implements Externalizable {

	private List<Field> _fields = new ArrayList<Field>();
	private String _fileName;

	private String _rootXpath = "/root/myDocument";

	private String _description;

	public String getRootXpath() {
		return _rootXpath;
	}

	public void setRootXpath(String rootXpath) {
		_rootXpath = rootXpath;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

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

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		_fileName = in.readUTF();
		_description = in.readUTF();
		_rootXpath = in.readUTF();
		int count = in.readInt();
		_fields.clear();
		for (int i = 0; i < count; i++) {
			Field field = new Field();
			field.readExternal(in);
			_fields.add(field);
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(_fileName);
		out.writeUTF(_description);
		out.writeUTF(_rootXpath);
		out.writeInt(_fields.size());
		for (Field field : _fields) {
			field.writeExternal(out);
		}
	}

	@Override
	public int hashCode() {
		return _fileName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Document other = (Document) obj;
		return other._fileName.equals(_fileName);
	}
}
