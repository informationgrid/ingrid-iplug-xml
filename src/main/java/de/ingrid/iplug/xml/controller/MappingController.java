package de.ingrid.iplug.xml.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.service.XmlService;

@Controller
@SessionAttributes( { "document", "plugDescription", "rootElement" })
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
			@ModelAttribute("rootElement") Element rootElement, ModelMap model)
			throws Exception {

		// index preview
		List<Field> fields = document.getFields();
		// doc index | field name + values
		Map<Integer, LinkedHashMap<String, String>> indexDocs = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();

		String filterString = "";
		if (_xmlService.documentHasFilters(document)) {
			filterString = _xmlService.getFilterExpression(document);
		}

		List<Element> docs = _xmlService.selectSubNodesFromParentLevel(
				rootElement, filterString + "[position() < 21]");
		int length = docs.size();
		for (int i = 0; i < length; i++) {
			Element doc = docs.get(i); // one index doc
			LinkedHashMap<String, String> fieldAndValues = new LinkedHashMap<String, String>();

			for (Field field : fields) {
				String xpath = field.getXpath();
				List subNodes = _xmlService.getSubNodes(doc, xpath);
				List<String> values = _xmlService.getValues(subNodes);
				// build the combined value string for jsp
				String valueString = "";
				for (String s : values) {
					valueString += s + " ";
				}
				fieldAndValues.put(field.getXpath(), valueString);
				indexDocs.put(i, fieldAndValues);
			}
		}
		model.addAttribute("indexDocs", indexDocs);

		return "/iplug/mapping";
	}

}
