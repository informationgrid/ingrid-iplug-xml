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
package de.ingrid.iplug.xml;

import de.ingrid.admin.elasticsearch.IndexScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tngtech.configbuilder.ConfigBuilder;

import de.ingrid.admin.JettyStarter;
import de.ingrid.elasticsearch.search.IndexImpl;
import de.ingrid.iplug.HeartBeatPlug;
import de.ingrid.iplug.IPlugdescriptionFieldFilter;
import de.ingrid.iplug.PlugDescriptionFieldFilters;
import de.ingrid.utils.IRecordLoader;
import de.ingrid.utils.IngridCall;
import de.ingrid.utils.IngridDocument;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHitDetail;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.dsc.Record;
import de.ingrid.utils.metadata.IMetadataInjector;
import de.ingrid.utils.processor.IPostProcessor;
import de.ingrid.utils.processor.IPreProcessor;
import de.ingrid.utils.query.IngridQuery;

@Service
public class XmlPlug extends HeartBeatPlug implements IRecordLoader {

	private static Logger log = LogManager.getLogger(XmlPlug.class);

	private final IndexImpl _indexSearcher;
	public static Configuration conf;
	private final IndexScheduler indexScheduler;

	@Autowired
	public XmlPlug(final IndexImpl indexSearcher,
			final IPlugdescriptionFieldFilter[] fieldFilters,
			final IMetadataInjector[] metadataInjectors,
			final IPreProcessor[] preProcessors, final IPostProcessor[] postProcessors, IndexScheduler indexScheduler) {
		super(60000, new PlugDescriptionFieldFilters(fieldFilters),
				metadataInjectors, preProcessors, postProcessors);
		_indexSearcher = indexSearcher;
		this.indexScheduler = indexScheduler;
	}

	/* (non-Javadoc)
	 * @see de.ingrid.iplug.HeartBeatPlug#close()
	 */
	@Override
	public void close() {
		_indexSearcher.close();
	}

	/* (non-Javadoc)
	 * @see de.ingrid.utils.ISearcher#search(de.ingrid.utils.query.IngridQuery, int, int)
	 */
	public IngridHits search(final IngridQuery query, final int start,
			final int length) throws Exception {
        preProcess(query);
		return _indexSearcher.search(query, start, length);
	}

	/* (non-Javadoc)
	 * @see de.ingrid.utils.IDetailer#getDetail(de.ingrid.utils.IngridHit, de.ingrid.utils.query.IngridQuery, java.lang.String[])
	 */
	public IngridHitDetail getDetail(final IngridHit hit,
			final IngridQuery query, final String[] fields) {
		return _indexSearcher.getDetail(hit, query, fields);
	}

	/* (non-Javadoc)
	 * @see de.ingrid.utils.IDetailer#getDetails(de.ingrid.utils.IngridHit[], de.ingrid.utils.query.IngridQuery, java.lang.String[])
	 */
	public IngridHitDetail[] getDetails(final IngridHit[] hitArray,
			final IngridQuery query, final String[] fields) {
		return _indexSearcher.getDetails(hitArray, query, fields);
	}

	/* (non-Javadoc)
	 * @see de.ingrid.utils.IRecordLoader#getRecord(de.ingrid.utils.IngridHit)
	 */
	@Override
	public Record getRecord(final IngridHit hit) {
        return _indexSearcher.getRecord(hit);
	}
	
	public static void main(String[] args) throws Exception {
        conf = new ConfigBuilder<>(Configuration.class).withCommandLineArgs(args).build();
        new JettyStarter( conf );
    }

	@Override
	public IngridDocument call(IngridCall info) {
		IngridDocument doc = null;

		if ("index".equals(info.getMethod())) {
			indexScheduler.triggerManually();
			doc = new IngridDocument();
			doc.put("success", true);
		}
		log.warn("The following method is not supported: " + info.getMethod());

		return doc;
	}
}
