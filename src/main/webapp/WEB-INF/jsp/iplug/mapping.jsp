 <%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ include file="/WEB-INF/jsp/base/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="org.xml.sax.XMLReader"%>
<%@page import="org.xml.sax.helpers.XMLReaderFactory"%><html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
<title>Portal U Administration</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta name="author" content="wemove digital solutions" />
<meta name="copyright" content="wemove digital solutions GmbH" />
<link rel="StyleSheet" href="../css/base/portal_u.css" type="text/css" media="all" />
<link rel="StyleSheet" href="../css/iplug/iplug_xml.css" type="text/css" media="all" />
</head>
<body>
	<div id="header">
		<img src="../images/base/logo.gif" width="168" height="60" alt="Portal U" />
		<h1>Konfiguration</h1>
		<div id="language"><a href="#">Englisch</a></div>
	</div>
	
	<div id="help"><a href="#">[?]</a></div>
	
	<c:set var="active" value="mapping" scope="request"/>
	<c:import url="../base/subNavi.jsp"></c:import>
	
	<div id="contentBox" class="contentMiddle">
		<h1 id="head">Mapping der Daten auf den Index</h1>
		<div class="controls">
			<a href="#" onclick="document.location='settings.html';">Zurück</a>
			<a href="#" onclick="document.location='welcome.html';">Abbrechen</a>
			<a href="#" onclick="document.getElementById('submit').submit();">Mapping Beenden und Speichern</a>
		</div>
		<div class="controls cBottom">
			<a href="#" onclick="document.location='settings.html';">Zurück</a>
			<a href="#" onclick="document.location='welcome.html';">Abbrechen</a>
			<a href="#" onclick="document.getElementById('submit').submit();">Mapping Beenden und Speichern</a>
		</div>
		<form action="finish.html" method="post" style="display:none" id="submit">
			
		</form>
		
		<div id="content">
			<h2>Definieren Sie, was indexiert werden soll.</h2>
			
			<div style="overflow:auto">
				<x:parse var="xml">
					${frag}
				</x:parse>
				<c:import url="/WEB-INF/jsp/iplug/xsl/extractXPath.xsl" var="xsl"/>
		      	
	      		<x:transform doc="${xml}" xslt="${xsl}"/>
	      	</div>      	   
	      	
	      	<c:if test="${!empty document.fields}">
		      	<h2><br/>Index Vorschau:</h2>
				<div style="overflow:auto">
				<table class="data">
		      		<tr>
				      	<th width="25">&nbsp;</th>
				      	<c:forEach var="field" items="${document.fields}" varStatus="fieldStatus">
				      		<th>
				      			<b>${field.fieldName}</b> <a href="removeFromIndex.html?index=${fieldStatus.index}"><img src="/images/iplug/delete.png" align="absmiddle"/></a>
				      			<c:forEach var="filter" items="${field.filters}" varStatus="filterStatus">
				      				<br/>
				      				${filter.filterType} ${filter.expression} <a href="removeFilter.html?fieldIndex=${fieldStatus.index}&filterIndex=${filterStatus.index}"><img src="/images/iplug/delete.png" align="absmiddle"/></a>
				      			</c:forEach>
				      			<br/>
				      			<a href="addFilter.html?fieldIndex=${fieldStatus.index}"><img src="/images/iplug/add.png" align="absmiddle"/> Filter</a>
				      		</th>
				      	</c:forEach>
			      	</tr>
			      	<c:forEach var="doc" items="${indexDocs}" begin="0" end="19">
			      		<tr>
			      			<td>${doc.key +1}</td>
			      			<c:forEach var="field" items="${document.fields}">
			      			<td>${doc.value[field.fieldName]}</td>
			      			</c:forEach>
			      		</tr>
			      	</c:forEach>
			    </table> 
		      	</div>
	
			    <div style="clear:both"></div>
			    <br/><br/>
		    </c:if> 	
        </div>	
	</div>
	<div id="footer" style="height:100px; width:90%"></div>
</body>
</html>