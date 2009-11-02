package de.ingrid.iplug.xml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.ingrid.admin.StringUtils;
import de.ingrid.admin.object.IDocumentProducer;
import de.ingrid.admin.search.Stemmer;
import de.ingrid.iplug.xml.model.FieldType;
import de.ingrid.iplug.xml.service.XmlService;
import de.ingrid.utils.IConfigurable;
import de.ingrid.utils.PlugDescription;

@Service
public class XmlDocumentProducer implements IDocumentProducer, IConfigurable{
	
	private final XmlService _xmlService;
	private final Stemmer _stemmer;
	private static final Logger LOG = Logger.getLogger(XmlDocumentProducer.class);

	@Autowired
	public XmlDocumentProducer(XmlService xmlService, Stemmer stemmer) {
		_xmlService = xmlService;
		_stemmer = stemmer;
		
	}

	private XmlDocumentIterator _xmlIterator;
	
	static class XmlDocumentIterator implements Iterator<Document> {
		private XmlDocumentIterator _prev;
		
		
		private de.ingrid.iplug.xml.model.Document _xmlDocument;

		private final NodeList _documentsToIndex;

		private final Stemmer _stemmer;

		private int _counter;

		private final XmlService _xmlService; 
		
		
		public XmlDocumentIterator(de.ingrid.iplug.xml.model.Document xmlDocument, XmlDocumentIterator prev, 
				NodeList documentsToIndex, Stemmer stemmer, XmlService xmlService){
			_xmlDocument = xmlDocument;
			_prev = prev;
			_documentsToIndex = documentsToIndex;
			_stemmer = stemmer;
			_xmlService = xmlService;
			_counter =0;
		}

		public boolean hasNext() {
			boolean hasNext = false;
			if(_prev != null){
				hasNext = _prev.hasNext();
			}
			if(!hasNext){
				hasNext = _counter < _documentsToIndex.getLength();
			}
			System.out.println("file: " +_xmlDocument.getFileName() +" hasNext: " +hasNext);
			return hasNext;
		}

		public Document next() {
			if(_prev != null && _prev.hasNext()){
				return _prev.next();
			}
			Node node = _documentsToIndex.item(_counter);
			_counter++;
			Document document;
			document = createDocument(node, _xmlDocument);
			return document;
		}

		@SuppressWarnings("unchecked")
		private Document createDocument(Node node, de.ingrid.iplug.xml.model.Document xmlDocument) {
			Document doc = new Document();
			List<de.ingrid.iplug.xml.model.Field> fields = xmlDocument.getFields();
			for (de.ingrid.iplug.xml.model.Field field : fields) {
				try {
					FieldType fieldType = field.getFieldType();
					String label = field.getFieldName();
					NodeList subNodes = _xmlService.getSubNodes(node, field.getXpath());
					List<Comparable> values = _xmlService.getValues(subNodes);
					
					switch (fieldType) {
					case BOOLEAN:
					case KEYWORD:
						for (Comparable value : values) {
						doc.add(new Field(label, value.toString(), Store.YES,
								Index.NOT_ANALYZED));
						}
						break;
					case NUMBER:
						for (Comparable value : values) {
							doc.add(new Field(label,StringUtils.padding(Double
									.parseDouble(value.toString())), Store.YES, Index.NOT_ANALYZED));
						}
						break;
					case TEXT:
						for (Comparable value : values) {
							doc.add(new Field(label, value.toString(), Store.YES, Index.ANALYZED));
						}
						break;
					default:
						break;
					}
					
					for (Comparable value : values) {
						
						try {
							doc.add(new Field("content", _stemmer.stem(value.toString()), Store.NO, Index.ANALYZED));
						} catch (IOException e) {
							LOG.warn("can not stem content: " + value.toString(), e);
						}
						doc.add(new Field("content", value.toString(), Store.NO,Index.ANALYZED));
					}
					
				} catch (Exception e) {
					LOG.error("cant create document", e);
				}
			}

			return doc;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}

	public boolean hasNext() {
		return _xmlIterator.hasNext();
	}

	public Document next() {
		return _xmlIterator.next();
	}

	@SuppressWarnings("unchecked")
	public void configure(PlugDescription plugDescription) {
		LOG.debug("!!!!!!!!!!!!!!!!!!!!!! configure");
		File workinDirectory = plugDescription.getWorkinDirectory();
		List<de.ingrid.iplug.xml.model.Document> xmlDocuments = (List<de.ingrid.iplug.xml.model.Document>) plugDescription.get("mapping");
		_xmlIterator = null;
		for (de.ingrid.iplug.xml.model.Document xmlDocument : xmlDocuments) {
			LOG.debug("document: " +xmlDocument.getFileName() +", " +xmlDocument.getRootXpath());
			File xmlFile = new File(workinDirectory +File.separator +"mapping" +File.separator +xmlDocument.getFileName());
			String filterString = "";
			if(_xmlService.documentHasFilters(xmlDocument)){
				filterString = _xmlService.getFilterExpression(xmlDocument);
			}
			
			NodeList documentsToIndex;
			try {
				documentsToIndex = _xmlService.getDocuments(xmlDocument, xmlFile, xmlDocument.getRootXpath() +filterString);
				_xmlIterator = new XmlDocumentIterator(xmlDocument, _xmlIterator, documentsToIndex, _stemmer, _xmlService);
				LOG.debug("add new XmlDocumentIterator");
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
