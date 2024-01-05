<?xml version="1.0" encoding="UTF-8"?>
<!--
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
  -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template  match="/">
		<style type="text/css">
			td {font-family:Arial; font-size:12px}
		</style>
		<table style="border:1px solid #1B78B1" bgcolor="#F5F8EB">
			<xsl:call-template name="processChilds" >
				<xsl:with-param name="bgcolor" select="'#FFFFFF'"/>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!--
	###############################
	###### Process Child Elements ######
	###############################
	-->
	<xsl:template name="processChilds">
		<xsl:param name="bgcolor"/>
		
		<!-- recursive call as long as an element has childs -->
		<xsl:if test="child::*">
			<tr>
				<td style="padding-left:50px" colspan="2">
					<table style="border:1px solid #1B78B1" bgcolor="{$bgcolor}">
						<xsl:for-each select="child::*">
							
							<!-- make sure that we get only the first element of one level with same name -->
							<xsl:if test="not(preceding-sibling::*[name(current()) = name()])">
								<tr>
									<td style="background-color:#F7FCFF; width:200px;white-space:nowrap; border: 1px solid #D1E4F1">
										<!-- process node and node value -->
										<xsl:call-template name="getXpathLink">
											<xsl:with-param name="type" select="'node'"/>
											<xsl:with-param name="linkName" select="name()"/>
											<xsl:with-param name="count" select="count(following-sibling::*[name(current()) = name()])"/>
										</xsl:call-template>
									</td>
									<td style="background-color:#F7FCFF; border: 1px solid #D1E4F1">
										<xsl:value-of select="text()"/>
										<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
										<xsl:if test="not(text()) ">
											<font color="#959595" style="font-size:xx-small">
												<xsl:text>In diesem Dokument kein Wert vorhanden</xsl:text>
											</font>
										</xsl:if>
									</td>
								</tr>
								<!-- process attributes and attribute values -->
								<xsl:for-each select="@*">
									<tr>
										<td style="background-color:#F7FCFF;white-space:nowrap; border: 1px solid #D1E4F1">
											<xsl:call-template name="getXpathLink">
												<xsl:with-param name="attr">
													<xsl:text>/@</xsl:text>
													<xsl:value-of select="name()"/>
												</xsl:with-param>
												<xsl:with-param name="type" select="'attribute'"/>
												<xsl:with-param name="linkName" select="concat(name(../.), '/@', name())"/>
											</xsl:call-template>
										</td>
										<td style="background-color:#F7FCFF; border: 1px solid #D1E4F1">
											<xsl:value-of select="."/>
											<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
										</td>
									</tr>
								</xsl:for-each>
								
								
								<xsl:call-template name="processChilds">
									<xsl:with-param name="bgcolor">
										<xsl:choose>
											<xsl:when test="$bgcolor = '#F5F8EB'">
												<xsl:text>#FFFFFF</xsl:text>
											</xsl:when>
											<xsl:when test="$bgcolor = '#FFFFFF'">
												<xsl:text>#F5F8EB</xsl:text>
											</xsl:when>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:for-each>
					</table>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!--
	##############################
	###### Get XPath Link ############
	##############################
	-->
	<xsl:template name="getXpathLink">
		<xsl:param name="type"/>
		<xsl:param name="linkName"/>
		<xsl:param name="attr"/>
		<xsl:param name="count"/>
		<!-- build Xpath variable -->
		<xsl:variable name="xpath">
			<xsl:for-each select="ancestor-or-self::*">
				<xsl:value-of select="name()"/>
				<xsl:if test="not(position()=last())">
					<xsl:text>/</xsl:text>
				</xsl:if>
				<!-- append attribute to Xpath if it is an attribute -->
				<xsl:if test="position() = last()">
					<xsl:value-of select="$attr"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="relXpath">
			<xsl:value-of select="substring-after($xpath,'/')"/>	
		</xsl:variable>
		<!-- display the link -->
		<font color="#921313" style="font-weight:bold;font-size:14pt">
			<xsl:choose>
				<xsl:when test="$type = 'node'">
					<xsl:text>&lt;&gt; </xsl:text>
				</xsl:when>
				<xsl:when test="$type = 'attribute'">
					<xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;@ </xsl:text>
				</xsl:when>
			</xsl:choose>
		</font>
		<a href="addToIndex.html?xpath={$relXpath}" style="color:#000000;font-weight:bold; text-decoration:underline">
			<xsl:value-of select="$linkName" disable-output-escaping="yes"/>
		</a>
		<xsl:if test="$count &gt; 1 and $type='node'">
			<font style="color:#959595; font-size:x-small">
				<xsl:text> n (</xsl:text>
				<xsl:value-of select="$count +1"/>
				<xsl:text>)</xsl:text>
			</font>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
