/*
 * **************************************************-
 * Ingrid iPlug XML
 * ==================================================
 * Copyright (C) 2014 - 2024 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.iplug.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import de.ingrid.admin.IConfig;
import de.ingrid.admin.command.PlugdescriptionCommandObject;
import de.ingrid.iplug.xml.model.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class Configuration implements IConfig {
    
    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(Configuration.class);

    private XStream xstream;
    
    public Configuration() {
        xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
    }

    @Value("plugdescription.fields")
    public String fields;
    

    public List<Document> mapping;

    // contains the mapping after filtering with ones from working dir
    public List<Document> mappingFiltered;


    @Override
    public void initialize() {}
    
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
    }

    @Override
    public void setPropertiesFromPlugdescription( Properties props, PlugdescriptionCommandObject pd ) {
    	if (!this.mapping.isEmpty()){
        	props.setProperty("plugdescription.mapping", xstream.toXML(this.mapping));
    	}
    }

    @Value("${plugdescription.mapping:}")
    private void setDocumentMapping( String input ) {
        List<Document> map;
        if ("".equals( input )) {
            map = new ArrayList<>();
        } else {
            map = (List<Document>) xstream.fromXML(input);
        }
        mapping = map;
    }
}
