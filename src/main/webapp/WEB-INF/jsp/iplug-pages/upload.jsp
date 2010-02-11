<%@ include file="/WEB-INF/jsp/base/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
		<div id="language"><a href="#">Englisch</a></div>
	</div>
	
	<div id="help"><a href="#">[?]</a></div>
	
	<c:set var="active" value="mapping" scope="request"/>
	<c:import url="../base/subNavi.jsp"></c:import>
	
	<div id="contentBox" class="contentMiddle">
		<h1 id="head">Datei ausw�hlen</h1>
		<div class="controls">
			<a href="#" onclick="document.location='../iplug-pages/listMappings.html';">Zur�ck</a>
			<a href="#" onclick="document.location='../base/welcome.html';">Abbrechen</a>
			<a href="#" onclick="document.getElementById('uploadBean').submit();">Upload</a>
		</div>
		<div class="controls cBottom">
			<a href="#" onclick="document.location='../iplug-pages/listMappings.html';">Zur�ck</a>
			<a href="#" onclick="document.location='../base/welcome.html';">Abbrechen</a>
			<a href="#" onclick="document.getElementById('uploadBean').submit();">Upload</a>
		</div>
		<div id="content">
			<h2>W�hlen Sie eine Xml Datei aus, die Sie indizieren m�chten</h2>
			<form:form  enctype="multipart/form-data" modelAttribute="uploadBean"> 
				<table id="konfigForm">
					<tr>
						<td class="leftCol">Xml Datei:</td>
						<td><input type="file" name="file"/> <form:errors path="file" cssClass="error" element="div" /></td>
					</tr>
					<tr>
						<td class="leftCol">XPath zum Dokument:</td>
						<td>
							<form:input path="rootXpath" />
							<c:if test="${!empty error}">
							    <div class="error">
                                    <c:choose>
                                        <c:when test="${error == 'empty'}">Bitte geben Sie den Pfad zum Wurzelelement an.</c:when>
                                        <c:when test="${error == 'invalid'}">Dieser Pfad existiert nicht. Bitte �berpr�fen Sie Ihre Eingabe.</c:when>
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