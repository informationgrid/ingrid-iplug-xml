package de.ingrid.iplug.xml.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.model.FieldType;
import de.ingrid.iplug.xml.model.Filter;


@Controller
@SessionAttributes("document")
public class AddToIndexController {

	@RequestMapping(value = "/iplug/addToIndex.html", method = RequestMethod.GET)
	public String addToIndex(@ModelAttribute("document") Document document,
			@RequestParam(required = true) final String xpath, ModelMap model) {
		model.addAttribute("xpath", xpath);
		model.addAttribute("fieldTypes", FieldType.values());
		return "/iplug/addToIndex";
	}

	@RequestMapping(value = "/iplug/addToIndex.html", method = RequestMethod.POST)
	public String addToIndexPost(@ModelAttribute("document") Document document,
			@RequestParam(required = true) final String xpath,
			@RequestParam(required = false) final String fieldName,
			@RequestParam(required = false) final String ownFieldName,
			@RequestParam(required = true) final String fieldType,
			@RequestParam(required = true) final float score) {

		String label = !"".equals(ownFieldName) ? ownFieldName : fieldName;
		Field field = new Field(label, xpath, score, FieldType.valueOf(fieldType));
		document.addField(field);

		return "redirect:/iplug/mapping.html";
	}

	@RequestMapping(value = "/iplug/removeFromIndex.html", method = RequestMethod.GET)
	public String removeFromIndex(@ModelAttribute("document") Document document,
			@RequestParam(required = true) final int index) {
		
		List<Field> fields = document.getFields();
		Field field = fields.get(index);
		// delete filters first
		List<Filter> filters = field.getFilters();
		Iterator<Filter> iterator = filters.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		document.getFields().remove(index);
		
		return "redirect:/iplug/mapping.html";
	}
}