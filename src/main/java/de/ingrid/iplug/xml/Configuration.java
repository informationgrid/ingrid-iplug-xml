package de.ingrid.iplug.xml;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.tngtech.configbuilder.annotation.propertyloaderconfiguration.PropertiesFiles;
import com.tngtech.configbuilder.annotation.propertyloaderconfiguration.PropertyLocations;
import com.tngtech.configbuilder.annotation.valueextractor.DefaultValue;
import com.tngtech.configbuilder.annotation.valueextractor.PropertyValue;

import de.ingrid.admin.IConfig;
import de.ingrid.admin.command.PlugdescriptionCommandObject;

@PropertiesFiles( {"config"} )
@PropertyLocations(directories = {"conf"}, fromClassLoader = true)
public class Configuration implements IConfig {
    
    private static Log log = LogFactory.getLog(Configuration.class);
    
    @PropertyValue("plugdescription.fields")
    public String fields;
    
    @PropertyValue("plugdescription.mapping")
    @DefaultValue("")
    public String mapping;
    
    @PropertyValue("plugdescription.isRecordLoader")
    @DefaultValue("false")
    public boolean recordLoader;
    
    @PropertyValue("plugdescription.ranking")
    public String rankings;
    
    private XStream xstream;
    
    @Override
    public void initialize() {
    }

    @Override
    public void addPlugdescriptionValues( PlugdescriptionCommandObject pdObject ) {
        pdObject.put( "iPlugClass", "de.ingrid.iplug.xml.XmlPlug");
        
        if(pdObject.getFields().length == 0){
        	if(fields != null){
        		String[] fieldsList = fields.split(",");
        		for(String field : fieldsList){
        			pdObject.addField(field);
        		}
        	}
    	}
        
        pdObject.setRecordLoader(recordLoader);
        
        if(pdObject.getRankingTypes().length == 0){
        	if(rankings != null){
        		String[] rankingList = rankings.split(",");
        		boolean score = false;
				boolean date = false;
				boolean notRanked = false;
				for(String ranking : rankingList){
        			if(ranking.equals("score")){
        				score = true;
        			}else if (ranking.equals("date")) {
        				date = true;
					}else if (ranking.equals("notRanked")) {
						notRanked = true;
					}
        		}
				pdObject.setRankinTypes(score, date, notRanked);
        	}
    	}
        
        if(pdObject.get("mapping") == null){
        	if(!mapping.equals("")){
        		xstream = new XStream();
            	pdObject.put("mapping", xstream.fromXML(mapping));
        	}
        }
    }

    @Override
    public void setPropertiesFromPlugdescription( Properties props, PlugdescriptionCommandObject pd ) {
    	if(pd.get("mapping") != null){
    		xstream = new XStream();
        	props.setProperty("plugdescription.mapping", xstream.toXML(pd.get("mapping")));
    	}
    }
}
