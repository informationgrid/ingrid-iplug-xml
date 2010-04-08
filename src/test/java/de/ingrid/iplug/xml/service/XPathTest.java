package de.ingrid.iplug.xml.service;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

public class XPathTest extends TestCase {
	
	public void testKstXpath() throws Exception {
		String xpath1 = "./PERSONAE\\PERSONA";
		String xpath2 = "./PERSONAE/PERSONA";
		String xpath3 = "count(.//city)";
		String xpath4 = ".[count(.//city)>10]/name";
		
		assertNull(testNewInstance(xpath1));
		assertNotNull(testNewInstance(xpath2));
		assertNotNull(testNewInstance(xpath3));
		assertNull(testNewInstance(xpath4));
	}
	
	public String testNewInstance(String xpath) throws Exception {
		try {
			// XPath x1 = new XP
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			xp.compile(xpath);
			return xp.toString();
		} catch (Exception e) {
			return null;
		}
	}
}
