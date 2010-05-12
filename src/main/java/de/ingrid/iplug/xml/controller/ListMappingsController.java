package de.ingrid.iplug.xml.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.ingrid.admin.command.FileDeleteCommand;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;

@Controller
@SessionAttributes("plugDescription")
public class ListMappingsController {

    /**
     * List mappings.
     * 
     * @param commandObject
     * @param model
     * @return
     * 		Web request "/iplug-pages/listMappings"
     */
    @RequestMapping(value = "/iplug-pages/listMappings.html", method = RequestMethod.GET)
	public String listMappings(@ModelAttribute("plugDescription") final PlugdescriptionCommandObject commandObject, final ModelMap model) {
    	File mappingDir = new File (commandObject.getWorkinDirectory(), "mapping");
    	String[] mappingDirFiles = mappingDir.list();
    	List<Document> mappingList = new ArrayList<Document>();
    	mappingList = (List<Document>) commandObject.get("mapping");
    	boolean fileDelete = true;
    	if(mappingDirFiles != null && mappingDirFiles.length > 0){
    		for(int i=0; i<mappingDirFiles.length;i++){
    			String filenameDir = mappingDirFiles[i];
    			for(int j=0; j<mappingList.size();j++){
        			String filenameMapping  = mappingList.get(j).getFileName();
        			if(filenameMapping.equals(filenameDir)){
        				fileDelete=false;
        				break;
        			}else{
        				fileDelete=true;
        			}
        		}
    			
    			File tmpFile = new File(mappingDir, filenameDir);
    			boolean fileIsFile = tmpFile.isFile();
        		boolean fileIsDirectory = tmpFile.isDirectory();
        		
        		if(fileDelete){
        			deleteUnusedMappingFilesFromDirectory(tmpFile);
        		}
        		
        		if(!fileIsFile && !fileIsDirectory){
        			Iterator<Document> iterator = mappingList.iterator();
        			int index = 0;
        			while (iterator.hasNext()) {
        				iterator.next();
        				if (index == i) {
        					iterator.remove();
        					break;
        				}
        				index++;
        			}
        		}
        	}
    	}else{
    		Iterator<Document> iterator = mappingList.iterator();
			while (iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
    	}
    	
    	return "/iplug-pages/listMappings";
	}
    
    /**
	 * Function to delete unused mapping files or directories.
	 * 
	 * @param filePath
	 */
	public void deleteUnusedMappingFilesFromDirectory(File filePath){
		if(filePath.isDirectory()){
			File[] files = filePath.listFiles();
			for(int i=0; i < files.length ;i++){
				if(files[i].isFile()){
					files[i].delete();
				}else{
					deleteUnusedMappingFilesFromDirectory(files[i]);
				}
			}
			filePath.delete();
		}else if(filePath.isFile()){
			filePath.delete();
		}
	}
}