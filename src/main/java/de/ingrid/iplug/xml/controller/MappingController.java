package de.ingrid.iplug.xml.controller;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.service.XmlService;

@Controller
@SessionAttributes( { "document", "plugDescription" })
public class MappingController {

	private final XmlService _xmlService;

	@Autowired
	public MappingController(XmlService xmlService) {
		_xmlService = xmlService;

	}

	@RequestMapping(value = "/iplug/mapping.html", method = RequestMethod.GET)
	public String settings(
			@ModelAttribute("document") Document document,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			ModelMap model) throws Exception {

		
		File xml = new File(plugdescriptionCommandObject.getWorkinDirectory()
				+ File.separator + "mapping" + File.separator
				+ document.getFileName());

		// index preview
		List<Field> fields = document.getFields();
		String docRootPath = document.getRootXpath();
		// doc index  |  field name +  values
		LinkedHashMap<Integer, LinkedHashMap<String, String>> indexDocs = new LinkedHashMap<Integer, LinkedHashMap<String, String>>(); 

		String filterString = "";
		if(_xmlService.documentHasFilters(document)){
			filterString = _xmlService.getFilterExpression(document);
		}
		
		NodeList docs = _xmlService.getDocuments(document, xml, docRootPath +filterString);
		int length = docs.getLength();
		for (int i = 0; i < length; i++) {
			Node doc = docs.item(i); // one index doc
			LinkedHashMap<String, String> fieldAndValues = new LinkedHashMap<String, String>();
				
			for (Field field : fields) {
				NodeList subNodes = _xmlService.getSubNodes(doc, field.getXpath());
				List<Comparable> values = _xmlService.getValues(subNodes);
				
				// build the combined value string for jsp 
				String valueString = "";
				for (Comparable comparable : values) {
					valueString += comparable + " ";
				}
				fieldAndValues.put(field.getFieldName(), valueString);
				indexDocs.put(i, fieldAndValues);
			}
		}
		model.addAttribute("indexDocs", indexDocs);

		return "/iplug/mapping";
	}

}
