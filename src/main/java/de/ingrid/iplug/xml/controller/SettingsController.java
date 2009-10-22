package de.ingrid.iplug.xml.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;

@Controller
@SessionAttributes( { "document", "plugDescription" })
public class SettingsController {

	@RequestMapping(value = "/iplug/settings.html", method = RequestMethod.GET)
	public String settings(
			@ModelAttribute("document") Document document,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			ModelMap model) {
		return "/iplug/settings";
	}
	
	@RequestMapping(value = "/iplug/settings.html", method = RequestMethod.POST)
	public String submitSettings(
			@ModelAttribute("document") Document document,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			ModelMap model, HttpServletRequest request) {
		
		return "redirect:/iplug/mapping.html";
	}

}
