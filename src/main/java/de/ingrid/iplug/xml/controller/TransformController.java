package de.ingrid.iplug.xml.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xml.sax.SAXException;

import de.ingrid.iplug.xml.controller.UploadController.XsltOutput;

@Controller
@SessionAttributes( { "xsltOutput" })
public class TransformController {

	@RequestMapping(value = "/iplug/transform.html", method = RequestMethod.GET)
	public String transform(
			@ModelAttribute("xsltOutput") XsltOutput xsltOutput,
			ServletRequest request, ServletResponse response)
			throws IOException, XPathExpressionException,
			ParserConfigurationException, SAXException, TransformerException,
			JDOMException {
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(xsltOutput.getContent());
		outputStream.flush();
		return null;
	}
}
