package de.ingrid.iplug.xml.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;

@Controller
@SessionAttributes( { "plugDescription", "document" })
public class EditMappingController {

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/iplug/editMapping.html", method = RequestMethod.GET)
	public String editXml(
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) int documentIndex,
			Model model) throws IOException {
		List<Document> documents = (List<Document>) plugdescriptionCommandObject
				.get("mapping");
		Document document = documents.get(documentIndex);
		model.addAttribute("document", document);
		return "redirect:/iplug/mapping.html";
	}
}
