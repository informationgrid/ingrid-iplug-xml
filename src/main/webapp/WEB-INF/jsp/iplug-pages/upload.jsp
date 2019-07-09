<%--
  **************************************************-
  Ingrid iPlug XML
  ==================================================
  Copyright (C) 2014 - 2019 wemove digital solutions GmbH
  ==================================================
  Licensed under the EUPL, Version 1.1 or – as soon they will be
  approved by the European Commission - subsequent versions of the
  EUPL (the "Licence");
  
  You may not use this work except in compliance with the Licence.
  You may obtain a copy of the Licence at:
  
  http://ec.europa.eu/idabc/eupl5
  
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
<%@page import="de.ingrid.admin.security.IngridPrincipal"%>
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
			<form:form action="../iplug-pages/upload.html" enctype="multipart/form-data" modelAttribute="uploadBean"> 
				<table id="konfigForm">
					<tr>
						<td class="leftCol">Xml Datei:</td>
						<td>
							<div class="input full"><input type="file" name="file"/> <form:errors path="file" cssClass="error" element="div" /></div>
							<c:if test="${!empty error_file}">
							    <div class="error">
                                    <c:choose>
                                        <c:when test="${error_file == 'invalid'}">Die ausgew�hlte Datei entspricht keinem XML Schema.</c:when>
                                        <c:when test="${error_file == 'empty'}">Bitte w�hlen Sie ein XML Dokument aus.</c:when>
                                        <c:when test="${error_file == 'invalid_zip'}">Die ausgew�hlte ZIP-Datei ist fehlerhaft oder der Zugriff wird verweigert.</c:when>
                                    </c:choose>
							    </div>
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="leftCol">XPath zum Dokument:</td>
						<td>
							<div class="input full">
								<form:input path="rootXpath" />
								<c:if test="${!empty error}">
								    <div class="error">
	                                    <c:choose>
	                                        <c:when test="${error == 'empty'}">Bitte geben Sie den Pfad zum Wurzelelement an.</c:when>
	                                        <c:when test="${error == 'invalid'}">Dieser Pfad existiert nicht oder ist ung�ltig. Bitte �berpr�fen Sie Ihre Eingabe.</c:when>
	                                    </c:choose>
								    </div>
								</c:if>
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftCol">Beschreibung:</td>
						<td>
							<div class="input full"><form:input path="description" /></div>
						</td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
	<div id="footer" style="height:100px; width:90%"></div>
</body>
</html>
