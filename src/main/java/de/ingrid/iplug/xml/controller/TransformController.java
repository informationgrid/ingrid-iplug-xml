package de.ingrid.iplug.xml.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xml.sax.SAXException;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.service.XmlService;

@Controller
@SessionAttributes( { "document", "plugDescription" })
public class TransformController {

	private final XmlService _xmlService;

	@Autowired
	public TransformController(XmlService xmlService) {
		_xmlService = xmlService;
	}

	@RequestMapping(value = "/iplug/transform.html", method = RequestMethod.GET)
	public String transform(
			@ModelAttribute("document") Document document,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			ServletRequest request, ServletResponse response)
			throws IOException, XPathExpressionException,
			ParserConfigurationException, SAXException, TransformerException {

		File xml = new File(plugdescriptionCommandObject.getWorkinDirectory()
				+ File.separator + "mapping" + File.separator
				+ document.getFileName());
		OutputStream outputStream = response.getOutputStream();
		_xmlService.transformNode(document.getRootXpath(), xml,
				outputStream);
		return null;
	}
}
