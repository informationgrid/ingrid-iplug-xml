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
import org.jdom.Attribute;
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

	private static Log LOG = LogFactory.getLog(XmlService.class);
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
	public List<Element> getSubNodes(Element node, String xPathString) {
		XPath newInstance;
		try {
			newInstance = XPath.newInstance(xPathString);
			LOG.info("try to select node list with xpath ["
					+ newInstance.getXPath() + "] from element [" + node + "]");
			return newInstance.selectNodes(node);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			LOG.info("xPath expression is invalid. Function getSubNodes return null.");
			return null;
		}
	}

	public List<Element> selectSubNodesFromParentLevel(Element element,
			String xpath) throws XPathExpressionException, JDOMException {
		Element fromElement = element.getDocument().getRootElement() != null ? element
				.getDocument().getRootElement()
				: element;
		String rootXpath = buildRootXpath(element) + xpath;
		
		LOG.info(xpath + " -> " + rootXpath);
		return getSubNodes(fromElement, rootXpath);
	}

	private String buildRootXpath(Element element) {
		String s = "";
		Element parentElement = element.getParentElement();
		if (parentElement != null) {
			s = buildRootXpath(parentElement);
		}
		String nameSpacePrefix = !"".equals(element.getNamespacePrefix()) ? element
				.getNamespacePrefix()
				+ ":"
				: "";
		s = s + "/" + nameSpacePrefix + element.getName();
		System.out.println("return: " + s);
		return s;
	}

	public Element selectRootElement(org.jdom.Document jdomDocument,
			String xpath) throws JDOMException {
		XPath newInstance = XPath.newInstance(xpath);
		LOG.info("try to select single node with xpath ["
				+ newInstance.getXPath() + "] from element [" + jdomDocument
				+ "]");
		Element singleNode = (Element) newInstance
				.selectSingleNode(jdomDocument);
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

	public List<String> getValues(List nodes)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		int length = nodes.size();
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			try {
				Element element = (Element) nodes.get(i);
				values.add(element.getText());
			} catch (Exception e) {
				Attribute attribute = (Attribute) nodes.get(i);
				values.add(attribute.getValue());
			} 			
		}
		return values;
	}

}
