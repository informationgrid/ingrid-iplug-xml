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
package de.ingrid.iplug.xml.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xml.sax.SAXException;

import de.ingrid.iplug.xml.controller.UploadController.XsltOutput;

@Controller
@SessionAttributes( { "xsltOutput" })
public class TransformController {

    /**
     * Transform of XsltOutput.
     * 
     * @param xsltOutput
     * @param request
     * @param response
     * @return
     * 		null
     * @throws IOException
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     * @throws JDOMException
     */
    @RequestMapping(value = "/iplug-pages/transform.html", method = RequestMethod.GET)
	public String transform(
			@ModelAttribute("xsltOutput") final XsltOutput xsltOutput,
			final ServletRequest request, final ServletResponse response)
			throws IOException, XPathExpressionException,
			ParserConfigurationException, SAXException, TransformerException,
			JDOMException {
		final OutputStream outputStream = response.getOutputStream();
		outputStream.write(xsltOutput.getContent());
		outputStream.flush();
		return null;
	}
}
