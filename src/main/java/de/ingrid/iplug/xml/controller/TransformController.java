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

    /**
     * Transform of XsltOutput.
     * 
     * @param xsltOutput
     * @param request
     * @param response
     * @return
     * 		null
     * @throws IOException
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     * @throws JDOMException
     */
    @RequestMapping(value = "/iplug-pages/transform.html", method = RequestMethod.GET)
	public String transform(
			@ModelAttribute("xsltOutput") final XsltOutput xsltOutput,
			final ServletRequest request, final ServletResponse response)
			throws IOException, XPathExpressionException,
			ParserConfigurationException, SAXException, TransformerException,
			JDOMException {
		final OutputStream outputStream = response.getOutputStream();
		outputStream.write(xsltOutput.getContent());
		outputStream.flush();
		return null;
	}
}
