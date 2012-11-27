package de.ingrid.iplug.xml.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.ingrid.admin.command.Command;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;

/**
 * Command for synchronization of Plug description and mapping directory. 
 *
 */
public class SyncPlugDescriptionDirectoryCommand extends Command{
	
	private PlugdescriptionCommandObject plugDescription;
	
	public SyncPlugDescriptionDirectoryCommand (PlugdescriptionCommandObject pg){
		plugDescription = pg;
	}
	
	/* (non-Javadoc)
	 * @see de.ingrid.admin.command.Command#execute()
	 */
	public void execute(){
		if(plugDescription != null){
			syncPlugDescriptionWithMappingDirectory();	
		}
	}
	
	/**
	 * Method for synchronization Plug description and  
	 * mapping directory.
	 * 
	 */
	public void syncPlugDescriptionWithMappingDirectory(){
		File mappingDir = new File (plugDescription.getWorkinDirectory(), "mapping");
    	String[] mappingDirFiles = mappingDir.list();
    	List<Document> mappingList = new ArrayList<Document>();
    	mappingList = (List<Document>) plugDescription.get("mapping");
    	if(mappingList != null){
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
	        			deleteMappingFilesFromDirectory(tmpFile);
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
	    	    if (mappingList == null)
	    	        return;
	    	    
	    		Iterator<Document> iterator = mappingList.iterator();
				while (iterator.hasNext()) {
					iterator.next();
					iterator.remove();
				}
	    	}
    	}
    }
	
	/**
	 * Function to delete unused mapping files or directories.
	 * 
	 * @param filePath
	 */
	public void deleteMappingFilesFromDirectory(File filePath){
		if(filePath.isDirectory()){
			File[] files = filePath.listFiles();
			for(int i=0; i < files.length ;i++){
				if(files[i].isFile()){
					files[i].delete();
				}else{
					deleteMappingFilesFromDirectory(files[i]);
				}
			}
			filePath.delete();
		}else if(filePath.isFile()){
			filePath.delete();
		}
	}
}
