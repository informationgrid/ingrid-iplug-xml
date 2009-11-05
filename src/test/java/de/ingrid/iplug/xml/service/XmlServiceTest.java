package de.ingrid.iplug.xml.service;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.jdom.Element;

public class XmlServiceTest extends TestCase {

	public void testPomXml() throws Exception {

		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/pom.xml");
		String xPathString = "/project/dependencies/dependency";
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				xPathString);
		List<Element> subNodes = xmlService.getSubNodes(rootElement
				.getParentElement(), rootElement.getName() + "/version");
		assertEquals(18, subNodes.size());

	}
}
