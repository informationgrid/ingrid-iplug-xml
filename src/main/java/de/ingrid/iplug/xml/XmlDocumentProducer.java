/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
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

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ingrid.admin.StringUtils;
import de.ingrid.admin.object.IDocumentProducer;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.service.XmlService;
import de.ingrid.utils.ElasticDocument;
import de.ingrid.utils.IConfigurable;
import de.ingrid.utils.PlugDescription;

/**
 * Produce xml document.
 *
 */
@Service
public class XmlDocumentProducer implements IDocumentProducer, IConfigurable {

	private final XmlService _xmlService;
	private static final Logger LOG = Logger
			.getLogger(XmlDocumentProducer.class);

	@Autowired
	public XmlDocumentProducer(XmlService xmlService) {
		_xmlService = xmlService;

	}

	private XmlDocumentIterator _xmlIterator;

	static class XmlDocumentIterator implements Iterator<Map<String, Object>> {
		private XmlDocumentIterator _prev;
		private final XmlService _xmlService;
		private Iterator<Element> _docsIterator;
		private final List<Field> _fields;

		public XmlDocumentIterator(XmlDocumentIterator prev,
				List<Element> docs, List<Field> fields, XmlService xmlService) {
			_prev = prev;
			_fields = fields;
			_docsIterator = docs.iterator();
			_xmlService = xmlService;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			boolean hasNext = false;
			if (_prev != null) {
				hasNext = _prev.hasNext();
			}
			if (!hasNext) {
				hasNext = _docsIterator.hasNext();
			}
			return hasNext;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public ElasticDocument next() {
			if (_prev != null && _prev.hasNext()) {
				return _prev.next();
			}
			Element element = _docsIterator.next();
			return createDocument(element);
		}

		/**
		 * Create document with element node.
		 * 
		 * @param node
		 * @return
		 * 		Document.
		 */
		private ElasticDocument createDocument(Element node) {
			ElasticDocument doc = new ElasticDocument();
			for (de.ingrid.iplug.xml.model.Field field : _fields) {
				try {
					de.ingrid.iplug.xml.model.FieldType fieldType = field
							.getFieldType();
					String label = field.getFieldName();
					List<Element> subNodes = _xmlService.getSubNodes(node,
							field.getXpath());
					List<String> values = _xmlService.getValues(subNodes);

					switch (fieldType) {
					case KEYWORD:
						for (String value : values) {
						    doc.put( label, value );
						}
						break;
					case NUMBER:
						for (Comparable<?> value : values) {
						    doc.put( label, StringUtils.padding(Double.parseDouble(value.toString())) );
						}
						break;
					case TEXT:
						for (Comparable<?> value : values) {
						    doc.put( label, value.toString() );
						}
						break;
					default:
						break;
					}

					for (Comparable<?> value : values) {
						doc.put( "content", value.toString() );
					}

				} catch (Exception e) {
					LOG.error("cant create document", e);
				}
			}

			return doc;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("not implemented");
		}

	}

	/* (non-Javadoc)
	 * @see de.ingrid.admin.object.IDocumentProducer#hasNext()
	 */
	public boolean hasNext() {
		try {
			return _xmlIterator.hasNext();	
		} catch (NullPointerException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see de.ingrid.admin.object.IDocumentProducer#next()
	 */
	public ElasticDocument next() {
		return _xmlIterator.next();
	}

	/* (non-Javadoc)
	 * @see de.ingrid.utils.IConfigurable#configure(de.ingrid.utils.PlugDescription)
	 */
	public void configure(PlugDescription plugDescription) {
		try {
			File workinDirectory = plugDescription.getWorkinDirectory();
			List<de.ingrid.iplug.xml.model.Document> xmlDocuments = XmlPlug.conf.mapping;
			_xmlIterator = null;
			for (de.ingrid.iplug.xml.model.Document xmlDocument : xmlDocuments) {
				LOG.debug("document: " + xmlDocument.getFileName() + ", "
						+ xmlDocument.getRootXpath());
				File xmlPath = new File (workinDirectory + File.separator
						+ "mapping" + File.separator, xmlDocument.getFileName());
				File[] xmlFiles = xmlPath.listFiles();
				if(xmlFiles != null){
					for (int i = 0; i < xmlFiles.length; i++) {
						File xmlFile = xmlFiles[i];
						LOG.info("parse xml file: " + xmlFile.getAbsolutePath());
						org.jdom.Document jdomDocument = _xmlService
								.createDocument(xmlFile);
						LOG.info("parsing finish");
						LOG.info("select root document...");
						Element rootElement = _xmlService.selectRootElement(
								jdomDocument, xmlDocument.getRootXpath());

						String filterString = "";
						if (_xmlService.documentHasFilters(xmlDocument)) {
							filterString = _xmlService.getFilterExpression(xmlDocument);
						}

						List<Element> docs = _xmlService.getSubNodes(rootElement
								.getParentElement(), rootElement.getName()
								+ filterString);

						_xmlIterator = new XmlDocumentIterator(_xmlIterator, docs,
								xmlDocument.getFields(), _xmlService);
					}	
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
