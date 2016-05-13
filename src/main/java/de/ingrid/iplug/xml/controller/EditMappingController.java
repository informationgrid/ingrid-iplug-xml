/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2016 wemove digital solutions GmbH
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.XmlPlug;
import de.ingrid.iplug.xml.controller.UploadController.XsltOutput;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.service.XmlService;

@Controller
@SessionAttributes( { "plugDescription", "document", "rootElement",
		"xsltOutput" })
public class EditMappingController {

	private static final Log LOG = LogFactory
			.getLog(EditMappingController.class);
	private final XmlService _xmlService;

	@Autowired
	public EditMappingController(final XmlService xmlService) {
		_xmlService = xmlService;
	}

	/**
	 * Edit xml.
	 * 
	 * @param plugdescriptionCommandObject
	 * @param documentIndex
	 * @param model
	 * @return
	 * 		Web request "redirect:/iplug-pages/mapping.html"
	 * @throws IOException
	 * @throws JDOMException
	 * @throws TransformerException
	 */
    @RequestMapping(value = "/iplug-pages/editMapping.html", method = RequestMethod.GET)
	public String editXml(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) final int documentIndex,
			final Model model) throws IOException, JDOMException,
			TransformerException {
		final List<Document> documents = XmlPlug.conf.mapping;
		final Document document = documents.get(documentIndex);
		model.addAttribute("document", document);

		final File mappingDir = new File(plugdescriptionCommandObject.getWorkinDirectory(), "mapping");
		
		final File pathToMappingDir = new File (mappingDir, document.getFileName());
		final File[] listAllFiles = pathToMappingDir.listFiles();
		final File newXmlFile = new File(pathToMappingDir, listAllFiles[0].getName());

		LOG.info("parse xml file: " + newXmlFile.getAbsolutePath());
		final org.jdom.Document jdomDocument = _xmlService.createDocument(newXmlFile);
		LOG.info("parsing finish");
		LOG.info("select root document...");
		final Element rootElement = _xmlService.selectRootElement(jdomDocument,
				document.getRootXpath());
		model.addAttribute("rootElement", rootElement);
		LOG.info("select root document finished");

		LOG.info("run xslt over rootDocument...");
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		_xmlService.writeElement(rootElement, outputStream);
		outputStream.flush();
		final byte[] byteArray = outputStream.toByteArray();
		final XsltOutput xsltOutput = new XsltOutput(byteArray);
		outputStream.close();
		LOG.info("run xslt over rootDocument finished");
		model.addAttribute("xsltOutput", xsltOutput);

        return "redirect:/iplug-pages/mapping.html";
	}
}
