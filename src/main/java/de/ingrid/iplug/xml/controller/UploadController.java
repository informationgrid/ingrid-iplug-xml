package de.ingrid.iplug.xml.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.service.XmlService;

@Controller
@RequestMapping(value = "/iplug/upload.html")
@SessionAttributes(value = { "uploadBean", "plugDescription", "document",
		"rootElement", "xsltOutput" })
public class UploadController {

	private static final Log LOG = LogFactory.getLog(UploadController.class);

	private final XmlService _xmlService;

	public static class UploadBean {

		private MultipartFile _multipartFile;

		private String _description;

		private String _rootXpath;

		public MultipartFile getFile() {
			return _multipartFile;
		}

		public void setFile(MultipartFile multipartFile) {
			_multipartFile = multipartFile;
		}

		public void setDescription(String description) {
			_description = description;
		}

		public String getDescription() {
			return _description;
		}

		public void setRootXpath(String rootXpath) {
			_rootXpath = rootXpath;
		}

		public String getRootXpath() {
			return _rootXpath;
		}

	}

	public static class XsltOutput {
		private byte[] _content;

		public XsltOutput(byte[] content) {
			_content = content;
		}

		public byte[] getContent() {
			return _content;
		}
	}

	@Autowired
	public UploadController(XmlService xmlService) {
		_xmlService = xmlService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String firstRow() {
		return "/iplug/upload";
	}

	@ModelAttribute("uploadBean")
	public UploadBean injectUploadBean() {
		return new UploadBean();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String upload(
			@ModelAttribute("uploadBean") UploadBean uploadBean,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			Model model) throws IOException, JDOMException,
			TransformerException {
		MultipartFile multipartFile = uploadBean.getFile();
		byte[] uploadBytes = multipartFile.getBytes();
		File workinDirectory = plugdescriptionCommandObject
				.getWorkinDirectory();
		File mappingDir = new File(workinDirectory, "mapping");
		mappingDir.mkdirs();

		int length = mappingDir.listFiles().length;
		File newXmlFile = new File(mappingDir, multipartFile
				.getOriginalFilename()
				+ "_" + length);

		FileOutputStream fileOutputStream = new FileOutputStream(newXmlFile);
		fileOutputStream.write(uploadBytes);
		fileOutputStream.flush();
		fileOutputStream.close();

		Document document = new Document();
		document.setFileName(newXmlFile.getName());
		document.setRootXpath(uploadBean.getRootXpath());
		document.setDescription(uploadBean.getDescription());
		model.addAttribute("document", document);

		LOG.info("parse xml file: " + newXmlFile.getAbsolutePath());
		org.jdom.Document jdomDocument = _xmlService.createDocument(newXmlFile);
		LOG.info("parsing finish");
		LOG.info("select root document...");
		Element rootElement = _xmlService.selectRootElement(jdomDocument,
				uploadBean.getRootXpath());
		model.addAttribute("rootElement", rootElement);
		LOG.info("select root document finished");

		LOG.info("run xslt over rootDocument...");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		_xmlService.writeElement(rootElement, outputStream);
		outputStream.flush();
		byte[] byteArray = outputStream.toByteArray();
		XsltOutput xsltOutput = new XsltOutput(byteArray);
		outputStream.close();
		LOG.info("run xslt over rootDocument finished");
		model.addAttribute("xsltOutput", xsltOutput);

		return "redirect:/iplug/mapping.html";

	}

}
