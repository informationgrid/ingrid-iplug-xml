DEPRECATED: This iPlug is not maintained anymore.

XML iPlug
========

The XML-iPlug connects the data of XML Files to the InGrid data space.

Features
--------

- index XML Files at a certain schedule
- user friendly, GUI based mapping of XML Data to an index
- provides search functionality on the indexed data
- GUI for easy administration


Requirements
-------------

- a running InGrid Software System

Installation
------------

Download from https://distributions.informationgrid.eu/ingrid-iplug-xml/
 
or

build from source with `mvn clean package`.

Execute

```
java -jar ingrid-iplug-xml-x.x.x-installer.jar
```

and follow the install instructions.

Obtain further information at http://www.ingrid-oss.eu/ (sorry only in German)


Contribute
----------

- Issue Tracker: https://github.com/informationgrid/ingrid-iplug-xml/issues
- Source Code: https://github.com/informationgrid/ingrid-iplug-xml
 
### Setup Eclipse project

* import project as Maven-Project
* right click on project and select Maven -> Select Maven Profiles ... (Ctrl+Alt+P)
* choose profile "development"
* run "mvn compile" from Commandline (unpacks base-webapp) 
* run e de.ingrid.iplug.xml.XmlPlug as Java Application
* in browser call "http://localhost:10016" with login "admin/admin"

### Setup IntelliJ IDEA project

* choose action "Add Maven Projects" and select pom.xml
* in Maven panel expand "Profiles" and make sure "development" is checked
* run e de.ingrid.iplug.xml.XmlPlug
* in browser call "http://localhost:10016" with login "admin/admin"

Support
-------

If you are having issues, please let us know: info@informationgrid.eu

License
-------

The project is licensed under the EUPL license.
