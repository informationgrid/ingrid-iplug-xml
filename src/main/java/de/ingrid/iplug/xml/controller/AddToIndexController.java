/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 wemove digital solutions GmbH
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

    /**
     * Add xPath and field types to model.
     * 
     * @param document
     * @param xpath
     * @param model
     * @return
     * 		Web request "/iplug-pages/addToIndex"
     */
    @RequestMapping(value = "/iplug-pages/addToIndex.html", method = RequestMethod.GET)
	public String addToIndex(@ModelAttribute("document") final Document document,
			@RequestParam(required = true) final String xpath, final ModelMap model) {
		model.addAttribute("xpath", xpath);
		model.addAttribute("fieldTypes", FieldType.values());
        return "/iplug-pages/addToIndex";
	}

    /**
     * Add field to document.
     * 
     * @param document
     * @param xpath
     * @param fieldName
     * @param ownFieldName
     * @param fieldType
     * @param score
     * @return
     * 		Web request "redirect:/iplug-pages/mapping.html"
     */
    @RequestMapping(value = "/iplug-pages/addToIndex.html", method = RequestMethod.POST)
	public String addToIndexPost(@ModelAttribute("document") final Document document,
			@RequestParam(required = true) final String xpath,
			@RequestParam(required = false) final String fieldName,
			@RequestParam(required = false) final String ownFieldName,
			@RequestParam(required = true) final String fieldType,
			@RequestParam(required = true) final float score) {

		final String label = !"".equals(ownFieldName) ? ownFieldName : fieldName;
		final Field field = new Field(label, xpath, score, FieldType.valueOf(fieldType));
		document.addField(field);

        return "redirect:/iplug-pages/mapping.html";
	}

    /**
     * Remove field from document by index. 
     * 
     * @param document
     * @param index
     * @return
     * 		Web request "redirect:/iplug-pages/mapping.html"
     */
    @RequestMapping(value = "/iplug-pages/removeFromIndex.html", method = RequestMethod.GET)
	public String removeFromIndex(@ModelAttribute("document") final Document document,
			@RequestParam(required = true) final int index) {

		final List<Field> fields = document.getFields();
		final Field field = fields.get(index);
		// delete filters first
		final List<Filter> filters = field.getFilters();
		final Iterator<Filter> iterator = filters.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		document.getFields().remove(index);

        return "redirect:/iplug-pages/mapping.html";
	}
}
