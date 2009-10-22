package de.ingrid.iplug.xml;

import org.springframework.stereotype.Service;

import de.ingrid.iplug.IPlugdescriptionFieldFilter;

@Service
public class MappingFilter implements IPlugdescriptionFieldFilter {

	@Override
	public boolean filter(Object key) {
		return key.toString().equals("mapping") ? true : false;
	}

}
