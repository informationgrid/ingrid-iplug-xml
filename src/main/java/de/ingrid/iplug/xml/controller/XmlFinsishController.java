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
package de.ingrid.iplug.xml.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.ingrid.iplug.xml.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.IUris;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.XmlPlug;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;

@Controller
@RequestMapping("/iplug-pages/finish.html")
@SessionAttributes(value = { "plugDescription", "document" })
public class XmlFinsishController {

	@Autowired
	private Configuration xmlConfig;

	/**
	 * Put all fields to PlugDescription.
	 * 
	 * @param plugdescriptionCommandObject
	 * @param document
	 * @return
	 * 		Web request "redirect:/base/save.html"
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	public String postFinish(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@ModelAttribute("document") final de.ingrid.iplug.xml.model.Document document)
			throws IOException {

		if (!plugdescriptionCommandObject.containsKey("fields")) {
			plugdescriptionCommandObject.put("fields", new ArrayList<String>());
		}

		final List<String> fields = (List<String>) plugdescriptionCommandObject.get("fields");

		final List<Document> documents = xmlConfig.mapping;
		// edit mode
		if (documents.contains(document)) {
			documents.remove(document);
		}
		documents.add(document);

		final List<Field> fieldsFromDocument = document.getFields();
		for (final Field field : fieldsFromDocument) {
			final String fieldName = field.getFieldName();
			fields.add(fieldName);
		}

		return "redirect:" + IUris.SAVE;
	}
}
