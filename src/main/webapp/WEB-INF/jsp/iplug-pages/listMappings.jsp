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
		<h1 id="head">Daten Mappings</h1>
		<div id="content">
			<h2>Folgende XML Dateien sind bereits gemappt</h2>
			<table class="data">
				<tr>
					<th>Datei</th>
					<th>Beschreibung</th>
					<th>&nbsp;</th>
				</tr>
				<c:set var="i" value="0" />
				<c:forEach var="document" items="${plugDescription['mapping']}">
				<tr>
					<td>${document.fileName}</td>
					<td>${document.description}</td>
					<td>
						<form action="/iplug-pages/deleteMapping.html" method="POST" style="float:left">
							<input type="hidden" name="documentIndex" value="${i}"/>
							<input type="submit" value="L�schen"/>
						</form>
						<form action="/iplug-pages/editMapping.html" method="GET" style="float:left">
							<input type="hidden" name="documentIndex" value="${i}"/>
							<input type="submit" value="Bearbeiten"/>
						</form>
						<form action="/iplug-pages/switchXml.html" method="GET" style="float:left">
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