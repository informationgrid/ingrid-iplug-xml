package de.ingrid.iplug.xml.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.ingrid.iplug.xml.model.Document;

@Service
public class XmlService {

	public XmlService() {

	}

	public void transformNode(String xpath, File xml, OutputStream outputStream)
			throws IOException, ParserConfigurationException, SAXException,
			XPathExpressionException, TransformerException {

		org.w3c.dom.Document w3cDocument = parseDocument(xml);
		XPathExpression xPathExpression = compileXpath(xpath);
		NodeList nodes = (NodeList) xPathExpression.evaluate(w3cDocument,
				XPathConstants.NODESET);
		Node node = nodes.getLength() > 0 ? nodes.item(0) : null;
		Source xmlSource = new DOMSource(node);
		Source xsltSource = new StreamSource(XmlService.class
				.getResourceAsStream("/extractXPath.xsl"));
		Result result = new StreamResult(outputStream);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer(xsltSource);
		transformer.transform(xmlSource, result);
	}

	private XPathExpression compileXpath(String xpath)
			throws XPathExpressionException {
		final XPathFactory factory = XPathFactory.newInstance();
		XPath newXpath = factory.newXPath();
		return newXpath.compile(xpath);
	}

	public ByteArrayOutputStream getFirstDocFromXml(Document document, File xml)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException, TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		// parse file
		final XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		org.w3c.dom.Document parse = parseDocument(xml);

		XPathExpression expr = xpath.compile(document.getRootXpath() + "[1]");
		Node node = (Node) expr.evaluate(parse, XPathConstants.NODE);

		// create new xml doc
		Source xmlSource = new DOMSource(node);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Result result = new StreamResult(byteArrayOutputStream);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		transformer.transform(xmlSource, result);
		return byteArrayOutputStream;
	}

	public NodeList getDocuments(Document document, File xml, String xPathString)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		final XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		org.w3c.dom.Document parse = parseDocument(xml);

		XPathExpression expr = xpath.compile(xPathString);
		NodeList nodes = (NodeList) expr
				.evaluate(parse, XPathConstants.NODESET);
		return nodes;
	}

	private org.w3c.dom.Document parseDocument(File xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		return docBuilder.parse(xml);
	}

	public List<Comparable> getValues(NodeList nodes)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		int length = nodes.getLength();
		List<Comparable> values = new ArrayList<Comparable>();
		for (int i = 0; i < length; i++) {
			values.add(nodes.item(i).getTextContent());
		}
		return values;
	}

}
