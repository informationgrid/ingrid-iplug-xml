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
		String xpath5 = "./city/@id";
		
		assertNull(testNewInstance(xpath1));
		assertNotNull(testNewInstance(xpath2));
		assertNotNull(testNewInstance(xpath3));
		assertEquals("count(.//city)", testNewInstance(xpath3));
		assertNotNull(testNewInstance(xpath4));
		assertEquals(".[count(.//city)>10]/name", testNewInstance(xpath4));
		assertNotNull(testNewInstance(xpath5));
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
