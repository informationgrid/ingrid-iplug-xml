package de.ingrid.iplug.xml;

import org.springframework.stereotype.Service;

import de.ingrid.admin.object.AbstractDataType;

@Service
public class XmlDataType extends AbstractDataType {
	
	public XmlDataType() {
		super("xml");
	}
}
