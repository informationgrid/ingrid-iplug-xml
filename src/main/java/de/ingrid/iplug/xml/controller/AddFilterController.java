/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2020 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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

    /**
     * Add filter to index field.
     * 
     * @param document
     * @param fieldIndex
     * @param model
     * @return
     * 		Web request "/iplug-pages/addFilter"
     */
    @RequestMapping(value = "/iplug-pages/addFilter.html", method = RequestMethod.GET)
	public String addFilter(@ModelAttribute("document") final Document document,
			@RequestParam(required = true) final int fieldIndex, final ModelMap model) {
		model.addAttribute("fieldIndex", fieldIndex);

		model.addAttribute("filterTypes", FilterType.values());
        return "/iplug-pages/addFilter";
	}

    /**
     * Add filter to index field.
     * 
     * @param document
     * @param fieldIndex
     * @param filterTypeString
     * @param expression
     * @return
     * 		Web request "redirect:/iplug-pages/mapping.html"
     */
    @RequestMapping(value = "/iplug-pages/addFilter.html", method = RequestMethod.POST)
	public String addFilterPost(
			@ModelAttribute("document") final Document document,
			@RequestParam(required = true) final int fieldIndex,
			@RequestParam(value = "filterType", required = true) final String filterTypeString,
			@RequestParam(required = true) final String expression) {

		final List<Field> fields = document.getFields();
		final Field field = fields.get(fieldIndex);
		final Filter filter = new Filter(expression, FilterType.valueOf(filterTypeString));

		field.addFilter(filter);
        return "redirect:/iplug-pages/mapping.html";
	}

    /**
     * Remove filter from index field.
     * 
     * @param document
     * @param fieldIndex
     * @param filterIndex
     * @return
     * 		Web request "redirect:/iplug-pages/mapping.html"	
     */
    @RequestMapping(value = "/iplug-pages/removeFilter.html", method = RequestMethod.GET)
	public String removeFilter(@ModelAttribute("document") final Document document,
			@RequestParam(required = true) final int fieldIndex,
			@RequestParam(required = true) final int filterIndex) {

		final List<Field> fields = document.getFields();
		final Field field = fields.get(fieldIndex);
		final List<Filter> filters = field.getFilters();
		filters.remove(filterIndex);

        return "redirect:/iplug-pages/mapping.html";
	}
}
