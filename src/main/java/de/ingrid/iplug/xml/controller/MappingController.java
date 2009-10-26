package de.ingrid.iplug.xml.controller;

import java.io.ByteArrayOutputStream;
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
	public MappingController(XmlService xmlService){
		_xmlService = xmlService;
		
	}
	
	@RequestMapping(value = "/iplug/mapping.html", method = RequestMethod.GET)
	public String settings(
			@ModelAttribute("document") Document document,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			ModelMap model) throws Exception {
		
		// create xml fragment with get first doc
		File xml = new File(plugdescriptionCommandObject.getWorkinDirectory() +File.separator + "mapping" +File.separator +document.getFileName());
		ByteArrayOutputStream byteArrayOutputStream = _xmlService.getFirstDocFromXml(
				document, xml);
		String oneDoc = new String(byteArrayOutputStream.toByteArray());
		model.addAttribute("frag", oneDoc);

		// index preview
		List<Field> fields = document.getFields();
		String docRootPath = document.getRootXpath() ;
		LinkedHashMap<Integer, LinkedHashMap<String, String>> indexDocs = new LinkedHashMap<Integer, LinkedHashMap<String, String>>(); // doc index | fieldname + values
		
		NodeList nodes = _xmlService.getDocuments(document, xml, docRootPath);
		int length = nodes.getLength();
		for(int i=0; i<length; i++){
			Node item = nodes.item(i); // one index doc
			LinkedHashMap<String, String> fieldAndValues = new LinkedHashMap<String, String>();
			for (Field field : fields) {
				NodeList nodesToIndex = _xmlService.getDocuments(document, xml, docRootPath +"[" +(i+1) +"]" +"/" +field.getXpath()); // a field can have multiple nodes to index
				List<Comparable> values = _xmlService.getValues(nodesToIndex);
				String valueString = "";
				for (Comparable comparable : values) {
					valueString += comparable +" ";
				}
				fieldAndValues.put(field.getFieldName(), valueString);
				indexDocs.put(i, fieldAndValues);
			}
		}
		model.addAttribute("indexDocs", indexDocs);
		
		return "/iplug/mapping";
	}

	

}
