/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2019 wemove digital solutions GmbH
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
package de.ingrid.iplug.xml.service;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.jdom.Element;

public class XmlServiceTest extends TestCase {

	public void testPomXmlDependencies() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/pom.xml");
		String xPathString = "/project/dependencies";
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				xPathString);
		List<Element> subNodes = xmlService.selectSubNodesFromParentLevel(
				rootElement, "/dependency/version");
		assertEquals(18, subNodes.size());
	}

	public void testPomXmlDependency() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/pom.xml");
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				"/project/dependencies/dependency");
		List<Element> subNodes = xmlService.selectSubNodesFromParentLevel(
				rootElement, "/version");
		assertEquals(18, subNodes.size());
	}

	public void testPomXmlProject() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/pom.xml");
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				"/project");
		List<Element> subNodes = xmlService.selectSubNodesFromParentLevel(
				rootElement, "/dependencies/dependency/version");
		System.out.println(subNodes);
		assertEquals(18, subNodes.size());
	}

	public void testPomXmlVersion() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/pom.xml");
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				"/project/dependencies/dependency/version");
		List<Element> subNodes = xmlService.selectSubNodesFromParentLevel(
				rootElement, "/text()");
		System.out.println(subNodes);
		assertEquals(18, subNodes.size());
	}

	public void testPomXmlLimitSize() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/pom.xml");
		String xPathString = "/project";
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				xPathString);
		List<Element> subNodes = xmlService
				.selectSubNodesFromParentLevel(rootElement,
						"/dependencies/dependency[position() < 3]/version");
		assertEquals(2, subNodes.size());
	}

	public void testParseNsg() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/nsg.xml");
		String xPathString = "/MD_Metadata";
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				xPathString);
		List<Element> subNodes = xmlService.selectSubNodesFromParentLevel(
				rootElement, "/dateStamp/Date");
		assertEquals(1, subNodes.size());
		Element element = subNodes.get(0);
		assertEquals("2009-04-18", element.getValue());
	}

	public void testParseNsgGcoDate() throws Exception {
		XmlService xmlService = new XmlService();
		File xml = new File("src/test/resources/nsg.xml");
		org.jdom.Document createDocument = xmlService.createDocument(xml);
		Element rootElement = xmlService.selectRootElement(createDocument,
				"/MD_Metadata/dateStamp");
		System.out.println(rootElement);
		List<Element> subNodes = xmlService.selectSubNodesFromParentLevel(
				rootElement, "/Date");
		assertEquals(1, subNodes.size());
		Element element = subNodes.get(0);
		assertEquals("2009-04-18", element.getValue());
	}
}
