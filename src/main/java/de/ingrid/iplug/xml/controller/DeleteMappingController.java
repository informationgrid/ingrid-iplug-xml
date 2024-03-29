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

import de.ingrid.admin.command.Command;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.Configuration;
import de.ingrid.iplug.xml.command.SyncPlugDescriptionDirectoryCommand;
import de.ingrid.iplug.xml.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@Controller
@SessionAttributes( { "plugDescription", "postCommandObject"})
public class DeleteMappingController {

	@Autowired
	private Configuration xmlConfig;

	/**
	 * Delete mapping.
	 * 
	 * @param plugdescriptionCommandObject
	 * @param documentIndex
	 * @return
	 * 		Web request "redirect:/iplug-pages/listMappings.html"
	 */
    @RequestMapping(value = "/iplug-pages/deleteMapping.html", method = RequestMethod.POST)
	public String deleteMapping(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) final int documentIndex,
			@ModelAttribute("postCommandObject") final Command postCommandObject,
			final ModelMap model) {
		System.out.println("DeleteMappingController.deleteMapping() : documentIndex = " + documentIndex);
		final List<Document> documents = xmlConfig.mapping;
		final Iterator<Document> iterator = documents.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			iterator.next();
			if (i == documentIndex) {
				postCommandObject.add(new SyncPlugDescriptionDirectoryCommand(plugdescriptionCommandObject, xmlConfig));
		    	iterator.remove();
				break;
			}
			i++;
		}
		
		model.addAttribute("postCommandObject", postCommandObject);
		
        return "redirect:/iplug-pages/listMappings.html";
	}
}
