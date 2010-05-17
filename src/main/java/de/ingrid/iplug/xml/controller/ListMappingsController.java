package de.ingrid.iplug.xml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.Command;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.command.SyncPlugDescriptionDirectoryCommand;

@Controller
@SessionAttributes("plugDescription")
public class ListMappingsController {

    /**
     * List mappings.
     * 
     * @param commandObject
     * @param model
     * @return
     * 		Web request "/iplug-pages/listMappings"
     */
    @RequestMapping(value = "/iplug-pages/listMappings.html", method = RequestMethod.GET)
	public String listMappings(@ModelAttribute("plugDescription") final PlugdescriptionCommandObject commandObject, final ModelMap model) {
    	SyncPlugDescriptionDirectoryCommand command = new SyncPlugDescriptionDirectoryCommand(commandObject);
    	command.execute();
    	return "/iplug-pages/listMappings";
	}
}