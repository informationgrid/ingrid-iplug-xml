<%--
  **************************************************-
  Ingrid iPlug XML
  ==================================================
  Copyright (C) 2014 - 2024 wemove digital solutions GmbH
  ==================================================
  Licensed under the EUPL, Version 1.2 or – as soon they will be
  approved by the European Commission - subsequent versions of the
  EUPL (the "Licence");
  
  You may not use this work except in compliance with the Licence.
  You may obtain a copy of the Licence at:
  
  https://joinup.ec.europa.eu/software/page/eupl
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the Licence is distributed on an "AS IS" basis,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the Licence for the specific language governing permissions and
  limitations under the Licence.
  **************************************************#
  --%>
<%@ include file="/WEB-INF/jsp/base/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
<title>iPlug Administration</title>
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta name="author" content="wemove digital solutions" />
<meta name="copyright" content="wemove digital solutions GmbH" />
<link rel="StyleSheet" href="../css/base/portal_u.css" type="text/css" media="all" />
</head>
<body>
	<div id="header">
		<img src="../images/base/logo.gif" width="168" height="60" alt="Portal" />
		<h1>Konfiguration</h1>
		<security:authorize access="isAuthenticated()">
			<div id="language"><a href="../base/auth/logout.html">Logout</a></div>
		</security:authorize>
	</div>
	
	<div id="help"><a href="#">[?]</a></div>
	
	<c:set var="active" value="mapping" scope="request"/>
	<c:import url="../base/subNavi.jsp"></c:import>
	
	
	<div id="contentBox" class="contentMiddle">
		<h1 id="head">Daten Mappings</h1>
		<div class="controls">
            <a href="#" onclick="document.location='../base/fieldQuery.html';">Zurück</a>
            <a href="#" onclick="document.location='../base/welcome.html';">Abbrechen</a>
            <a href="#" onclick="document.location='../base/save.html';">Mapping beenden und speichern</a>
        </div>
        <div class="controls cBottom">
            <a href="#" onclick="document.location='../base/fieldQuery.html';">Zurück</a>
            <a href="#" onclick="document.location='../base/welcome.html';">Abbrechen</a>
            <a href="#" onclick="document.location='../base/save.html';">Mapping beenden und speichern</a>
        </div>
		<div id="content">
			<h2>Folgende XML Dateien sind bereits gemappt</h2>
			<table class="data">
				<tr>
					<th>Datei</th>
					<th>Beschreibung</th>
					<th>&nbsp;</th>
				</tr>
				<c:set var="i" value="0" />
				<c:forEach var="document" items="${mapping}">
				<tr>
					<td>${document.fileName}</td>
					<td>${document.description}</td>
					<td>
						<form action="../iplug-pages/deleteMapping.html" method="POST" style="float:left">
							<input type="hidden" name="documentIndex" value="${i}"/>
							<input type="submit" value="Löschen"/>
						</form>
						<form action="../iplug-pages/editMapping.html" method="GET" style="float:left">
							<input type="hidden" name="documentIndex" value="${i}"/>
							<input type="submit" value="Bearbeiten"/>
						</form>
						<form action="../iplug-pages/switchXml.html" method="GET" style="float:left">
							<input type="hidden" name="documentIndex" value="${i}"/>
							<input type="submit" value="Datei aktualisieren" />
						</form>
					</td>
				</tr>
				<c:set var="i" value="${i + 1}" />
				</c:forEach>
			</table>
			
			<br/>
			<br/>
			<button onclick="document.location = 'upload.html'">Neue XML Datei mappen</button>
			
		</div>
	</div>
	<div id="footer" style="height:100px; width:90%"></div>
</body>
</html>
