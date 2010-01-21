package de.ingrid.iplug.xml.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.IUris;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;

@Controller
@RequestMapping("/iplug-pages/finish.html")
@SessionAttributes(value = { "plugDescription", "document" })
public class XmlFinsishController {
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	public String postFinish(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@ModelAttribute("document") final de.ingrid.iplug.xml.model.Document document)
			throws IOException {

		if (!plugdescriptionCommandObject.containsKey("fields")) {
			plugdescriptionCommandObject.put("fields", new ArrayList<String>());
		}

		final List<String> fields = (List<String>) plugdescriptionCommandObject
				.get("fields");

		if (!plugdescriptionCommandObject.containsKey("mapping")) {
			plugdescriptionCommandObject.put("mapping",
					new ArrayList<Document>());
		}
		final List<Document> documents = (List<Document>) plugdescriptionCommandObject
				.get("mapping");
		// edit mode
		if (documents.contains(document)) {
			documents.remove(document);
		}
		documents.add(document);

		final List<Field> fieldsFromDocument = document.getFields();
		for (final Field field : fieldsFromDocument) {
			final String fieldName = field.getFieldName();
			fields.add(fieldName);
		}

		return "redirect:" + IUris.SAVE;
	}
}
