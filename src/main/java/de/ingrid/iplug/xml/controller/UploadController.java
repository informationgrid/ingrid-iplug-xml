package de.ingrid.iplug.xml.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

@Controller
@RequestMapping(value = "/iplug/upload.html")
@SessionAttributes(value = { "uploadBean", "plugDescription", "document" })
public class UploadController {

	public static class UploadBean {

		private MultipartFile _multipartFile;

		public MultipartFile getFile() {
			return _multipartFile;
		}

		public void setFile(MultipartFile multipartFile) {
			_multipartFile = multipartFile;
		}

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
			Model model) throws IOException {
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
		model.addAttribute("document", document);

		return "redirect:/iplug/settings.html";

	}

}
