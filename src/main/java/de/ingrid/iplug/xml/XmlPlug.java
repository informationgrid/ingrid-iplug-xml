package de.ingrid.iplug.xml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ingrid.iplug.HeartBeatPlug;
import de.ingrid.iplug.IPlugdescriptionFieldFilter;
import de.ingrid.iplug.PlugDescriptionFieldFilters;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHitDetail;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;

@Service
public class XmlPlug extends HeartBeatPlug {

	@Autowired
	public XmlPlug(IPlugdescriptionFieldFilter... filters) {
		super(10000, new PlugDescriptionFieldFilters(filters));
	}

	@Override
	public IngridHits search(IngridQuery query, int start, int length)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngridHitDetail getDetail(IngridHit hit, IngridQuery query,
			String[] requestedFields) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngridHitDetail[] getDetails(IngridHit[] hits, IngridQuery query,
			String[] requestedFields) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
