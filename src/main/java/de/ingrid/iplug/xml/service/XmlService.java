package de.ingrid.iplug.xml.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.transform.JDOMSource;
import org.jdom.xpath.XPath;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.model.Filter;
import de.ingrid.iplug.xml.model.Filter.FilterType;

@Service
public class XmlService {

	private static final Log LOG = LogFactory.getLog(XmlService.class);

	private SAXBuilder _saxBuilder;

	public XmlService() {
		_saxBuilder = new SAXBuilder();
		_saxBuilder.setValidation(false);
	}

	public org.jdom.Document createDocument(File xml) throws JDOMException,
			IOException {
		return _saxBuilder.build(xml);
	}

	@SuppressWarnings("unchecked")
	public List<Element> getSubNodes(Element node, String xPathString)
			throws XPathExpressionException, JDOMException {
		return XPath.selectNodes(node, xPathString);
	}

	public List<String> getValues(List<Element> nodes)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		int length = nodes.size();
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			values.add(nodes.get(i).getText());
		}
		return values;
	}

	public boolean documentHasFilters(Document document) {
		List<Field> fields = document.getFields();
		for (Field field : fields) {
			List<Filter> filters = field.getFilters();
			if (filters.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public String getFilterExpression(Document document) {
		List<String> expressions = new ArrayList<String>();
		List<Field> fields = document.getFields();
		for (Field field : fields) {
			List<Filter> filters = field.getFilters();
			for (Filter filter : filters) {
				FilterType filterType = filter.getFilterType();
				switch (filterType) {
				case EQUAL:
					expressions.add(field.getXpath() + " = '"
							+ filter.getExpression() + "'");
					break;
				case NOT_EQUAL:
					expressions.add("not(" + field.getXpath() + " = '"
							+ filter.getExpression() + "')");
					break;
				case CONTAINS:
					expressions.add("contains(" + field.getXpath() + ",'"
							+ filter.getExpression() + "')");
					break;
				case NOT_CONTAINS:
					expressions.add("not(contains(" + field.getXpath() + ",'"
							+ filter.getExpression() + "'))");
					break;
				case GREATER_THAN:
					expressions.add(field.getXpath() + " > "
							+ filter.getExpression());
					break;
				case LOWER_THAN:
					expressions.add(field.getXpath() + " < "
							+ filter.getExpression());
					break;
				default:
					break;
				}
			}
		}

		// build the valid xpath expression string
		String filterExprString = "";
		if (expressions.size() > 0) {
			filterExprString += "[";
			for (int i = 0; i < expressions.size(); i++) {
				if (i > 0) {
					filterExprString += " and ";
				}
				filterExprString += expressions.get(i);
			}
			filterExprString += "]";
		}
		return filterExprString;
	}

	public Element selectRootElement(org.jdom.Document jdomDocument,
			String xpath) throws JDOMException {
		Element singleNode = (Element) org.jdom.xpath.XPath.selectSingleNode(
				jdomDocument, xpath);
		return singleNode;
	}

	public void writeElement(Element rootElement, OutputStream outputStream)
			throws TransformerException {
		Source xmlSource = new JDOMSource(rootElement);
		Source xsltSource = new StreamSource(XmlService.class
				.getResourceAsStream("/extractXPath.xsl"));
		Result result = new StreamResult(outputStream);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer(xsltSource);
		transformer.transform(xmlSource, result);
	}

}
