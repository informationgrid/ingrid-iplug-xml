package de.ingrid.iplug.xml.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.Command;
import de.ingrid.admin.command.FileDeleteCommand;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
@Controller
@SessionAttributes( { "plugDescription", "postCommandObject"})
public class DeleteMappingController {

	/**
	 * Delete mapping.
	 * 
	 * @param plugdescriptionCommandObject
	 * @param documentIndex
	 * @return
	 * 		Web request "redirect:/iplug-pages/listMappings.html"
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/iplug-pages/deleteMapping.html", method = RequestMethod.POST)
	public String deleteMapping(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) final int documentIndex,
			@ModelAttribute("postCommandObject") final Command postCommandObject,
			final ModelMap model) {
		System.out.println("DeleteMappingController.deleteMapping() : documentIndex = " + documentIndex);
		final List<Document> documents = (List<Document>) plugdescriptionCommandObject.get("mapping");
		final Iterator<Document> iterator = documents.iterator();
		int i = 0;
		List<String> list = new ArrayList<String>(); 
		while (iterator.hasNext()) {
			iterator.next();
			if (i == documentIndex) {
				postCommandObject.add(new FileDeleteCommand(plugdescriptionCommandObject.getWorkinDirectory() + "\\mapping\\" + documents.get(i).getFileName()));
				iterator.remove();
				break;
			}
			i++;
		}
		
		model.addAttribute("postCommandObject", postCommandObject);
		
        return "redirect:/iplug-pages/listMappings.html";
	}
}
