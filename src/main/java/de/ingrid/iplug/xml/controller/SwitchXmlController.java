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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.ingrid.iplug.xml.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.XmlPlug;
import de.ingrid.iplug.xml.controller.UploadController.UploadBean;
import de.ingrid.iplug.xml.model.Document;

@Controller
@SessionAttributes( { "plugDescription", "document" })
public class SwitchXmlController {

	@Autowired
	private Configuration xmlConfig;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@ModelAttribute("uploadBean")
	public UploadBean injectUploadBean() {
		return new UploadBean();
	}

    /**
     * Add document to model.
     * 
     * @param plugdescriptionCommandObject
     * @param documentIndex
     * @param model
     * @return
     * 		Web request "/iplug-pages/switchXml.html"
     * @throws IOException
     */
    @RequestMapping(value = "/iplug-pages/switchXml.html", method = RequestMethod.GET)
	public String switchXls(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) final int documentIndex,
			final Model model) throws IOException {
		model.addAttribute("documentIndex", documentIndex);
        return "/iplug-pages/switchXml";
	}

	/**
	 * Upload upload bean.
	 * 
	 * @param documentIndex
	 * @param plugdescriptionCommandObject
	 * @param uploadBean
	 * @param model
	 * @return
	 * 		Web request "redirect:/iplug-pages/listMappings.html"
	 * @throws IOException
	 */
    @RequestMapping(value = "/iplug-pages/switchXml.html", method = RequestMethod.POST)
	public String upload(
			@RequestParam(value = "documentIndex", required = true) final int documentIndex,
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@ModelAttribute("uploadBean") final UploadBean uploadBean, final Model model)
			throws IOException {
		final MultipartFile multipartFile = uploadBean.getFile();
		final byte[] uploadBytes = multipartFile.getBytes();
		final List<Document> documents = xmlConfig.mapping;
		final String fileName = documents.get(documentIndex).getFileName();
		final File mappingDir = new File(plugdescriptionCommandObject
				.getWorkinDirectory(), "mapping/"+fileName);
		final File newXmlFile = new File(mappingDir, fileName);
		final FileOutputStream outputStream = new FileOutputStream(newXmlFile);
		outputStream.write(uploadBytes);
		outputStream.close();
        return "redirect:/iplug-pages/listMappings.html";

	}
}
