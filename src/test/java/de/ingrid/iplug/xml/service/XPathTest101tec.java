package de.ingrid.iplug.xml.service;


import junit.framework.TestCase;

import org.jdom.JDOMException;
import org.jdom.xpath.XPath;


public class XPathTest101tec extends TestCase {

	public void test101tecXpath() throws Exception {
		String xpath1 = "./PERSONAE\\PERSONA";
		String xpath2 = "./PERSONAE/PERSONA";
		String xpath3 = "count(.//city)";
		String xpath4 = ".[count(.//city)>10]/name";
		
		assertNull(testNewInstance(xpath1));
		assertNotNull(testNewInstance(xpath2));
		assertNotNull(testNewInstance(xpath3));
		assertEquals("count(.//city)", testNewInstance(xpath3));
		assertNotNull(testNewInstance(xpath4));
		assertEquals(".[count(.//city)>10]/name", testNewInstance(xpath4));
	}
	
	
	public String testNewInstance(String xpath)throws Exception{
		XPath newInstance; 
		try{
			newInstance = XPath.newInstance(xpath);
			return newInstance.toString();
		}catch(JDOMException e){
			return null;
		}
		
	}
}
