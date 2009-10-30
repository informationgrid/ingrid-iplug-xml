package de.ingrid.iplug.xml.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.controller.UploadController.UploadBean;
import de.ingrid.iplug.xml.model.Document;

@Controller
@SessionAttributes( { "plugDescription", "document" })
public class SwitchXmlController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@ModelAttribute("uploadBean")
	public UploadBean injectUploadBean() {
		return new UploadBean();
	}

	@RequestMapping(value = "/iplug/switchXml.html", method = RequestMethod.GET)
	public String switchXls(
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			@RequestParam(value = "documentIndex", required = true) int documentIndex,
			Model model) throws IOException {
		model.addAttribute("documentIndex", documentIndex);
		return "/iplug/switchXml";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/iplug/switchXml.html", method = RequestMethod.POST)
	public String upload(
			@RequestParam(value = "documentIndex", required = true) int documentIndex,
			@ModelAttribute("plugDescription") PlugdescriptionCommandObject plugdescriptionCommandObject,
			@ModelAttribute("uploadBean") UploadBean uploadBean, Model model)
			throws IOException {
		MultipartFile multipartFile = uploadBean.getFile();
		byte[] uploadBytes = multipartFile.getBytes();
		List<Document> documents = (List<Document>) plugdescriptionCommandObject.get("mapping");
		String fileName = documents.get(documentIndex).getFileName();
		File mappingDir = new File(plugdescriptionCommandObject
				.getWorkinDirectory(), "mapping");
		File newXmlFile = new File(mappingDir, fileName);
		FileOutputStream outputStream = new FileOutputStream(newXmlFile);
		outputStream.write(uploadBytes);
		outputStream.close();
		return "redirect:/iplug/listMappings.html";

	}
}