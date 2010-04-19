package de.ingrid.iplug.xml.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.controller.UploadController.XsltOutput;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.service.XmlService;

@Controller
@SessionAttributes( { "plugDescription", "document", "rootElement",
		"xsltOutput" })
public class EditMappingController {

	private static final Log LOG = LogFactory
			.getLog(EditMappingController.class);
	private final XmlService _xmlService;

	@Autowired
	public EditMappingController(final XmlService xmlService) {
		_xmlService = xmlService;
	}

	/**
	 * Edit xml.
	 * 
	 * @param plugdescriptionCommandObject
	 * @param documentIndex
	 * @param model
	 * @return
	 * 		Web request "redirect:/iplug-pages/mapping.html"
	 * @throws IOException
	 * @throws JDOMException
	 * @throws TransformerException
	 */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/iplug-pages/editMapping.html", method = RequestMethod.GET)
	public String editXml(
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) final int documentIndex,
			final Model model) throws IOException, JDOMException,
			TransformerException {
		final List<Document> documents = (List<Document>) plugdescriptionCommandObject
				.get("mapping");
		final Document document = documents.get(documentIndex);
		model.addAttribute("document", document);

		final File mappingDir = new File(plugdescriptionCommandObject
				.getWorkinDirectory(), "mapping");
		final File newXmlFile = new File(mappingDir, document.getFileName());

		LOG.info("parse xml file: " + newXmlFile.getAbsolutePath());
		final org.jdom.Document jdomDocument = _xmlService.createDocument(newXmlFile);
		LOG.info("parsing finish");
		LOG.info("select root document...");
		final Element rootElement = _xmlService.selectRootElement(jdomDocument,
				document.getRootXpath());
		model.addAttribute("rootElement", rootElement);
		LOG.info("select root document finished");

		LOG.info("run xslt over rootDocument...");
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		_xmlService.writeElement(rootElement, outputStream);
		outputStream.flush();
		final byte[] byteArray = outputStream.toByteArray();
		final XsltOutput xsltOutput = new XsltOutput(byteArray);
		outputStream.close();
		LOG.info("run xslt over rootDocument finished");
		model.addAttribute("xsltOutput", xsltOutput);

        return "redirect:/iplug-pages/mapping.html";
	}
}
