package de.ingrid.iplug.xml.controller;

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
import de.ingrid.iplug.xml.model.Filter;
import de.ingrid.iplug.xml.model.Filter.FilterType;


@Controller
@SessionAttributes("document")
public class AddFilterController {


	//@Autowired
	public AddFilterController() {
	}

	@RequestMapping(value = "/iplug/addFilter.html", method = RequestMethod.GET)
	public String addFilter(@ModelAttribute("document") Document document,
			@RequestParam(required = true) final int fieldIndex, ModelMap model) {
		model.addAttribute("fieldIndex", fieldIndex);
		
		model.addAttribute("filterTypes", FilterType.values());
		return "/iplug/addFilter";
	}

	@RequestMapping(value = "/iplug/addFilter.html", method = RequestMethod.POST)
	public String addFilterPost(
			@ModelAttribute("document") Document document,
			@RequestParam(required = true) final int fieldIndex,
			@RequestParam(value = "filterType", required = true) final String filterTypeString,
			@RequestParam(required = true) final String expression) {
		
		List<Field> fields = document.getFields();
		Field field = fields.get(fieldIndex);
		Filter filter = new Filter(expression, FilterType.valueOf(filterTypeString));
		
		field.addFilter(filter);
		return "redirect:/iplug/mapping.html";
	}

	@RequestMapping(value = "/iplug/removeFilter.html", method = RequestMethod.GET)
	public String removeFilter(@ModelAttribute("document") Document document,
			@RequestParam(required = true) final int fieldIndex,
			@RequestParam(required = true) final int filterIndex) {

		List<Field> fields = document.getFields();
		Field field = fields.get(fieldIndex);
		List<Filter> filters = field.getFilters();
		filters.remove(filterIndex);

		return "redirect:/iplug/mapping.html";
	}
}
