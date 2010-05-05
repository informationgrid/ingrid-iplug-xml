package de.ingrid.iplug.xml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ingrid.admin.StringUtils;
import de.ingrid.admin.object.IDocumentProducer;
import de.ingrid.admin.search.Stemmer;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.service.XmlService;
import de.ingrid.utils.IConfigurable;
import de.ingrid.utils.PlugDescription;

/**
 * Produce xml document.
 *
 */
@Service
public class XmlDocumentProducer implements IDocumentProducer, IConfigurable {

	private final XmlService _xmlService;
	private final Stemmer _stemmer;
	private static final Logger LOG = Logger
			.getLogger(XmlDocumentProducer.class);

	@Autowired
	public XmlDocumentProducer(XmlService xmlService, Stemmer stemmer) {
		_xmlService = xmlService;
		_stemmer = stemmer;

	}

	private XmlDocumentIterator _xmlIterator;

	static class XmlDocumentIterator implements Iterator<Document> {
		private XmlDocumentIterator _prev;
		private final XmlService _xmlService;
		private final Stemmer _stemmer;
		private Iterator<Element> _docsIterator;
		private final List<Field> _fields;

		public XmlDocumentIterator(XmlDocumentIterator prev,
				List<Element> docs, List<Field> fields, XmlService xmlService,
				Stemmer stemmer) {
			_prev = prev;
			_fields = fields;
			_docsIterator = docs.iterator();
			_xmlService = xmlService;
			_stemmer = stemmer;
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
		public Document next() {
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
		@SuppressWarnings("unchecked")
		private Document createDocument(Element node) {
			Document doc = new Document();
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
							doc.add(new org.apache.lucene.document.Field(label,
									value, Store.YES, Index.NOT_ANALYZED));
						}
						break;
					case NUMBER:
						for (Comparable value : values) {
							doc.add(new org.apache.lucene.document.Field(label,
									StringUtils.padding(Double
											.parseDouble(value.toString())),
									Store.YES, Index.NOT_ANALYZED));
						}
						break;
					case TEXT:
						for (Comparable value : values) {
							doc
									.add(new org.apache.lucene.document.Field(
											label, value.toString(), Store.YES,
											Index.ANALYZED));
						}
						break;
					default:
						break;
					}

					for (Comparable value : values) {

						try {
							doc.add(new org.apache.lucene.document.Field(
									"content", _stemmer.stem(value.toString()),
									Store.NO, Index.ANALYZED));
						} catch (IOException e) {
							LOG.warn("can not stem content: "
									+ value.toString(), e);
						}
						doc.add(new org.apache.lucene.document.Field("content",
								value.toString(), Store.NO, Index.ANALYZED));
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
		return _xmlIterator.hasNext();
	}

	/* (non-Javadoc)
	 * @see de.ingrid.admin.object.IDocumentProducer#next()
	 */
	public Document next() {
		return _xmlIterator.next();
	}

	/* (non-Javadoc)
	 * @see de.ingrid.utils.IConfigurable#configure(de.ingrid.utils.PlugDescription)
	 */
	@SuppressWarnings("unchecked")
	public void configure(PlugDescription plugDescription) {
		try {
			File workinDirectory = plugDescription.getWorkinDirectory();
			List<de.ingrid.iplug.xml.model.Document> xmlDocuments = (List<de.ingrid.iplug.xml.model.Document>) plugDescription
					.get("mapping");
			_xmlIterator = null;
			for (de.ingrid.iplug.xml.model.Document xmlDocument : xmlDocuments) {
				LOG.debug("document: " + xmlDocument.getFileName() + ", "
						+ xmlDocument.getRootXpath());
				File xmlFile = new File(workinDirectory + File.separator
						+ "mapping" + File.separator
						+ xmlDocument.getFileName());
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
						xmlDocument.getFields(), _xmlService, _stemmer);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
