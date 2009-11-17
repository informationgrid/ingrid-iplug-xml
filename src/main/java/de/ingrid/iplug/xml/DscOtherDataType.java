package de.ingrid.iplug.xml;

import org.springframework.stereotype.Service;

import de.ingrid.admin.object.AbstractDataType;

@Service
public class DscOtherDataType extends AbstractDataType {

    public DscOtherDataType() {
        super(DSC_OTHER, true);
    }
}
