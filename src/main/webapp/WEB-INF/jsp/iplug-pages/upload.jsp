<%@ include file="/WEB-INF/jsp/base/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="de.ingrid.admin.security.IngridPrincipal"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
<title>Portal U Administration</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta name="author" content="wemove digital solutions" />
<meta name="copyright" content="wemove digital solutions GmbH" />
<link rel="StyleSheet" href="../css/base/portal_u.css" type="text/css" media="all" />
</head>
<body>
	<div id="header">
		<img src="../images/base/logo.gif" width="168" height="60" alt="Portal U" />
		<h1>Konfiguration</h1>
		<%
		java.security.Principal  principal = request.getUserPrincipal();
		if(principal != null && !(principal instanceof IngridPrincipal.SuperAdmin)) {
		%>
			<div id="language"><a href="../base/auth/logout.html">Logout</a></div>
		<%
		}
		%>
	</div>
	
	<div id="help"><a href="#">[?]</a></div>
	
	<c:set var="active" value="mapping" scope="request"/>
	<c:import url="../base/subNavi.jsp"></c:import>
	
	<div id="contentBox" class="contentMiddle">
		<h1 id="head">Datei auswählen</h1>
		<div class="controls">
			<a href="#" onclick="document.location='../iplug-pages/listMappings.html';">Zurück</a>
			<a href="#" onclick="document.location='../base/welcome.html';">Abbrechen</a>
			<a href="#" onclick="document.getElementById('uploadBean').submit();">Upload</a>
		</div>
		<div class="controls cBottom">
			<a href="#" onclick="document.location='../iplug-pages/listMappings.html';">Zurück</a>
			<a href="#" onclick="document.location='../base/welcome.html';">Abbrechen</a>
			<a href="#" onclick="document.getElementById('uploadBean').submit();">Upload</a>
		</div>
		<div id="content">
			<h2>Wählen Sie eine Xml Datei aus, die Sie indizieren möchten</h2>
			<form:form action="../iplug-pages/upload.html" enctype="multipart/form-data" modelAttribute="uploadBean"> 
				<table id="konfigForm">
					<tr>
						<td class="leftCol">Xml Datei:</td>
						<td>
							<input type="file" name="file"/> <form:errors path="file" cssClass="error" element="div" />
							<c:if test="${!empty error_file}">
							    <div class="error">
                                    <c:choose>
                                        <c:when test="${error_file == 'invalid'}">Ihr ausgewähltes XML Dokument ist ungültig.</c:when>
                                        <c:when test="${error_file == 'empty'}">Bitte wählen Sie ein XML Dokument aus.</c:when>
                                    </c:choose>
							    </div>
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="leftCol">XPath zum Dokument:</td>
						<td>
							<form:input path="rootXpath" />
							<c:if test="${!empty error}">
							    <div class="error">
                                    <c:choose>
                                        <c:when test="${error == 'empty'}">Bitte geben Sie den Pfad zum Wurzelelement an.</c:when>
                                        <c:when test="${error == 'invalid'}">Dieser Pfad existiert nicht oder ist ungültig. Bitte überprüfen Sie Ihre Eingabe.</c:when>
                                    </c:choose>
							    </div>
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="leftCol">Beschreibung:</td>
						<td>
							<form:input path="description" />
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
	<div id="footer" style="height:100px; width:90%"></div>
</body>
</html>