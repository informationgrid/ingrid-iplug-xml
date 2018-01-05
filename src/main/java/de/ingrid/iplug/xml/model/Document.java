/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2018 wemove digital solutions GmbH
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
 * Set document.
 *
 */
public class Document implements Externalizable {

	private List<Field> _fields = new ArrayList<Field>();
	private String _fileName;
	private String _rootXpath = "/root/myDocument";

	private String _description;

	/**
	 * Get root xPath.
	 * 
	 * @return
	 * 		xPath.
	 */
	public String getRootXpath() {
		return _rootXpath;
	}

	/**
	 * Set root xPath.
	 * 
	 * @param rootXpath
	 */
	public void setRootXpath(String rootXpath) {
		_rootXpath = rootXpath;
	}

	/**
	 * Get description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Set description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		_description = description;
	}

	/**
	 * Get file name.
	 * 
	 * @return
	 * 		File name.
	 */
	public String getFileName() {
		return _fileName;
	}

	/**
	 * Set file name.
	 *  
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	/**
	 * Get fields.
	 * 
	 * @return
	 * 		List of fields.
	 */
	public List<Field> getFields() {
		return _fields;
	}

	/**
	 * Set fields.
	 * 
	 * @param fields
	 */
	public void setFields(List<Field> fields) {
		_fields = fields;
	}

	/**
	 * Add field.
	 * 
	 * @param field
	 */
	public void addField(Field field) {
		_fields.add(field);
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
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

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return _fileName.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Document other = (Document) obj;
		return other._fileName.equals(_fileName);
	}
}
