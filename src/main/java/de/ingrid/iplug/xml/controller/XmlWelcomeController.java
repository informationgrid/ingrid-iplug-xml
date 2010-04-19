package de.ingrid.iplug.xml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(value = "/iplug-pages/welcome.html")
@SessionAttributes("plugDescription")
public class XmlWelcomeController {

	/**
	 * Redirect to listMapping.html.	
	 * 
	 * @return
	 * 		Web request "redirect:/iplug-pages/listMappings.html"
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String welcome() {
        return "redirect:/iplug-pages/listMappings.html";
	}
}
