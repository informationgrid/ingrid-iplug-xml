package de.ingrid.iplug.xml;

import org.springframework.stereotype.Service;

import de.ingrid.iplug.IPlugdescriptionFieldFilter;

@Service
public class MappingFilter implements IPlugdescriptionFieldFilter {

	/* (non-Javadoc)
	 * @see de.ingrid.iplug.IPlugdescriptionFieldFilter#filter(java.lang.Object)
	 */
	@Override
	public boolean filter(Object key) {
		return key.toString().equals("mapping") ? true : false;
	}

}
