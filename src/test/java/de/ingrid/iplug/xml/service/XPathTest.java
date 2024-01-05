/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2024 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.iplug.xml.service;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class XPathTest {

	@Test
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

	@Test
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
