/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2018 wemove digital solutions GmbH
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

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
@RequestMapping(value = "/iplug-pages/upload.html")
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

		public void setFile(final MultipartFile multipartFile) {
			_multipartFile = multipartFile;
		}

		public void setDescription(final String description) {
			_description = description;
		}

		public String getDescription() {
			return _description;
		}

		public void setRootXpath(final String rootXpath) {
			_rootXpath = rootXpath;
		}

		public String getRootXpath() {
			return _rootXpath;
		}

	}

	public static class XsltOutput {
		private final byte[] _content;

		public XsltOutput(final byte[] content) {
			_content = content;
		}

		public byte[] getContent() {
			return _content;
		}
	}

	@Autowired
	public UploadController(final XmlService xmlService) {
		_xmlService = xmlService;
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String firstRow() {
        return "/iplug-pages/upload";
	}

	@ModelAttribute("uploadBean")
	public UploadBean injectUploadBean() {
		return new UploadBean();
	}

	/**
	 * Upload XML file.
	 * 
	 * @param uploadBean
	 * @param plugdescriptionCommandObject
	 * @param model
	 * @return
	 * 		Web request "redirect:/iplug-pages/mapping.html";
	 * @throws IOException
	 * @throws JDOMException
	 * @throws TransformerException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String upload(
			@ModelAttribute("uploadBean") final UploadBean uploadBean,
			@ModelAttribute("plugDescription") final PlugdescriptionCommandObject plugdescriptionCommandObject,
			final Model model) throws IOException, JDOMException,
			TransformerException {
		final MultipartFile multipartFile = uploadBean.getFile();
		final byte[] uploadBytes = multipartFile.getBytes();
		final File mappingDir = createDirectoryForMapping(plugdescriptionCommandObject
				.getWorkinDirectory(), "mapping");
		String timestamp = new SimpleDateFormat( "yyyyMMddHHmmss" ).format(new Date()).toString();
		final String mappingFilename = multipartFile.getOriginalFilename()+ "_" + timestamp;
		File newXmlFile = new File (createDirectoryForMapping(mappingDir, mappingFilename), mappingFilename);
		
		final FileOutputStream fileOutputStream = new FileOutputStream(newXmlFile);
		fileOutputStream.write(uploadBytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		
		final Document document = new Document();
		
		if(multipartFile.getContentType().equals("application/zip")){
			ZipFile zipFile;
			try {
				zipFile = new ZipFile(newXmlFile);	
			} catch (ZipException e) {
				LOG.warn("invalid zip file");
				model.addAttribute("error_file","invalid_zip");
				return firstRow(); 
			}
			
			Enumeration<?> entries = zipFile.entries();
			File firstZipFile = null;
			byte[] buffer = new byte[16384];
			int len;
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String entryFileName = newXmlFile.getName() + "_" + entry.getName();
				File extractedFileFromZip = new File(newXmlFile.getParentFile(),entryFileName);
			
				if(firstZipFile == null){
					firstZipFile = extractedFileFromZip;
				}
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(extractedFileFromZip));
				BufferedInputStream bis = new BufferedInputStream(zipFile
						.getInputStream(entry));
				while ((len = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, len);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			zipFile.close();
			
			if(newXmlFile.isFile()){
				newXmlFile.delete();
			}
			newXmlFile = firstZipFile;
		}
		
		document.setFileName(mappingFilename);
		document.setRootXpath(uploadBean.getRootXpath());
		document.setDescription(uploadBean.getDescription());
		model.addAttribute("document", document);

		LOG.info("parse xml file: " + newXmlFile.getAbsolutePath());
		
		final String rootPath = uploadBean.getRootXpath();
        org.jdom.Document jdomDocument = null;
		try {
			jdomDocument = _xmlService.createDocument(newXmlFile);
		} catch (Exception e) {
			if(uploadBytes.length == 0){
				LOG.warn("empty file");
				model.addAttribute("error_file","empty");
			}else{
				LOG.warn("invalid file '" + multipartFile.getOriginalFilename() + "'");
				model.addAttribute("error_file", "invalid");
			}
			
			if (rootPath == null || "".equals(rootPath)) {
	            LOG.warn("invalid root element '" + rootPath + "'");
	            model.addAttribute("error", "empty");
	        }
			return firstRow();
		}
		
		LOG.info("parsing finish");
		LOG.info("select root document...");
        if (rootPath == null || "".equals(rootPath)) {
            LOG.warn("invalid root element '" + rootPath + "'");
            model.addAttribute("error", "empty");
            return firstRow();
        }
        Element rootElement = null;
        try {
            rootElement = _xmlService.selectRootElement(jdomDocument, rootPath);
        } catch (final Exception e) {
        }
        if (rootElement == null) {
            LOG.warn("invalid root element '" + rootPath + "'");
            model.addAttribute("error", "invalid");
            return firstRow();
        }
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
	
	public File createDirectoryForMapping(File mappingWorkDirectory, String mappingFileDirectory){
		final File mappingFileDir = new File(mappingWorkDirectory, mappingFileDirectory);
		mappingFileDir.mkdirs();
		return mappingFileDir;
	}	
}
