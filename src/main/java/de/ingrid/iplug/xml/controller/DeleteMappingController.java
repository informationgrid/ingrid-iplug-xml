package de.ingrid.iplug.xml.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
@Controller
@SessionAttributes( { "plugDescription" })
public class DeleteMappingController {

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/iplug/deleteMapping.html", method = RequestMethod.POST)
	public String deleteMapping(
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) int documentIndex) {
		System.out.println("DeleteMappingController.deleteMapping() : documentIndex = " + documentIndex);
		List<Document> documents = (List<Document>) plugdescriptionCommandObject.get("mapping");
		Iterator<Document> iterator = documents.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			iterator.next();
			if (i == documentIndex) {
				iterator.remove();
				break;
			}
			i++;
		}
		return "redirect:/base/save.html";
	}
}
