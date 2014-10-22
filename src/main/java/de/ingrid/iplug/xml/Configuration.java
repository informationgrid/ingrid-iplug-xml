package de.ingrid.iplug.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tngtech.configbuilder.annotation.propertyloaderconfiguration.PropertiesFiles;
import com.tngtech.configbuilder.annotation.propertyloaderconfiguration.PropertyLocations;
import com.tngtech.configbuilder.annotation.typetransformer.TypeTransformer;
import com.tngtech.configbuilder.annotation.typetransformer.TypeTransformers;
import com.tngtech.configbuilder.annotation.valueextractor.DefaultValue;
import com.tngtech.configbuilder.annotation.valueextractor.PropertyValue;

import de.ingrid.admin.IConfig;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import de.ingrid.iplug.xml.model.Field;
import de.ingrid.iplug.xml.model.FieldType;
import de.ingrid.iplug.xml.model.Filter;
import de.ingrid.iplug.xml.model.Filter.FilterType;

@PropertiesFiles( {"config"} )
@PropertyLocations(directories = {"conf"}, fromClassLoader = true)
public class Configuration implements IConfig {
    
    private static Log log = LogFactory.getLog(Configuration.class);
    
    @TypeTransformers(Configuration.StringToCommunications.class)
    @PropertyValue("plugdescription.mapping")
    @DefaultValue("")
    public List<Document> documents;
    
    @Override
    public void initialize() {
    }

    @Override
    public void addPlugdescriptionValues( PlugdescriptionCommandObject pdObject ) {
        pdObject.addField("metainfo");
        pdObject.put( "iPlugClass", "de.ingrid.iplug.xml.XmlPlug");
        if(pdObject.get("mapping") == null){
        	if(documents.size() > 0){
        		pdObject.put("mapping", documents);
        	}
        }
    }

    @Override
    public void setPropertiesFromPlugdescription( Properties props, PlugdescriptionCommandObject pd ) {
    	String mappingString = "";
    	List<Document> documents = (List<Document>) pd.get("mapping");
    	for (int i = 0; i < documents.size(); i++) {
			Document document = documents.get(i);
			mappingString = mappingString + "" + document.getDescription() + ",";
			mappingString = mappingString + "" + document.getFileName() + ",";
			mappingString = mappingString + "" + document.getRootXpath();
			List<Field> fields = document.getFields();
			for (int j = 0; j < fields.size(); j++) {
				mappingString = mappingString + "##";
				
				Field field = fields.get(j);
				mappingString = mappingString + "" + field.getFieldName() + ",";
				mappingString = mappingString + "" + field.getFieldType() + ",";
				mappingString = mappingString + "" + field.getXpath() + ",";
				mappingString = mappingString + "" + field.getScore();
				List<Filter> filters = field.getFilters();
				for (int k = 0; k < filters.size(); k++) {
					mappingString = mappingString + ",";
					Filter filter = filters.get(k);
					mappingString = mappingString + "" + filter.getFilterType() + ",";
					mappingString = mappingString + "" + filter.getExpression();
				}
			}
			if(i != documents.size() - 1){
				mappingString = mappingString + "###";
			}
		}
    	if(mappingString.length() > 0){
    		props.setProperty("plugdescription.mapping", mappingString);
    	}
    }
    
    public class StringToCommunications extends TypeTransformer<String, List<Document>> {

        @Override
        public List<Document> transform(String input) {
            List<Document> list = new ArrayList<Document>();
            String[] mappingLists = input.split("###");
            if (mappingLists.length > 0) {
            	for (int i = 0; i < mappingLists.length; i++) {
            		Document document = new Document();
                    String mappingList = mappingLists[i];
    				String[] mappingListEntries = mappingList.split("##");
    				if(mappingListEntries.length > 0){
    					for (int j = 0; j < mappingListEntries.length; j++) {
    						String mappingListEntry = mappingListEntries[j];
    						String[] fields = mappingListEntry.split(",");
    						if(fields.length > 0){
    							if(j == 0){
    								String description = fields[0];
    								String filename = fields[1];
    								String rootxpath = fields[2];
    								
    								document.setDescription(description);
    								document.setFileName(filename);
    								document.setRootXpath(rootxpath);
        						}else{
        							String label = fields[0];
        							String fieldType = fields[1];
        							String xpath = fields[2];
        							float score = Float.parseFloat(fields[3]);
        							
        							Field field = new Field(label, xpath, score, FieldType.valueOf(fieldType));
        							if(fields.length > 4){
        								String filterTypeString = fields[4];
            							Comparable<? extends Serializable> expression = fields[5];
            							
            							Filter filter = new Filter(expression, FilterType.valueOf(filterTypeString));
            							field.addFilter(filter);
        							}
        							document.addField(field);
        						}
    						}
    					}
    				}
    				list.add(document);
    			}
            }
            return list;
        }
    }
}
