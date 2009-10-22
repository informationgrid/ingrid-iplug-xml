<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ino="http://namespaces.softwareag.com/tamino/response2">
	<xsl:template match="/">
		<style type="text/css">
			td {font-family:Arial; font-size:12px}
		</style>
		<table style="border:1px solid #176798" bgcolor="#EEFAFF">
			<xsl:call-template name="processChilds">
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
		<tr>
			<td style="background-color:#F8F8F8; width:200px;white-space:nowrap; border: 1px solid #E6E6E6">
				<!-- process node and node value -->
				<xsl:call-template name="getXpathLink">
					<xsl:with-param name="type" select="'node'"/>
					<xsl:with-param name="linkName" select="name()"/>
					<xsl:with-param name="count" select="count(following-sibling::*[name(current()) = name()])"/>
				</xsl:call-template>
			</td>
			<td style="background-color:#F8F8F8; border: 1px solid #E6E6E6">
				<xsl:value-of select="text()"/>
				<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
				<xsl:if test="not(text()) ">
					<font color="#959595" style="font-size:xx-small">
						<xsl:text>In diesem Dokument kein Wert vorhanden</xsl:text>
					</font>
				</xsl:if>
			</td>
		</tr>
		<!-- process atrributes and attribute values -->
		<xsl:for-each select="@*">
			<tr>
				<td style="background-color:#F8F8F8;white-space:nowrap; border: 1px solid #E6E6E6">
					<xsl:call-template name="getXpathLink">
						<xsl:with-param name="attr">
							<xsl:text>/@</xsl:text>
							<xsl:value-of select="name()"/>
						</xsl:with-param>
						<xsl:with-param name="type" select="'attribute'"/>
						<xsl:with-param name="linkName" select="concat(name(../.), '/@', name())"/>
					</xsl:call-template>
				</td>
				<td style="background-color:#F8F8F8; border: 1px solid #E6E6E6">
					<xsl:value-of select="."/>
					<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
				</td>
			</tr>
		</xsl:for-each>
		<!-- recursive call as long as an element has childs -->
		<xsl:if test="child::*">
			<tr>
				<td style="padding-left:50px" colspan="2">
					<table style="border:1px solid #176798" bgcolor="{$bgcolor}">
						<xsl:for-each select="child::*">
							<!-- make sure that we get only the first element of one level with same name -->
							<xsl:if test="not(preceding-sibling::*[name(current()) = name()])">
								<xsl:if test="not(name() = 'urlparameters')">
									<xsl:call-template name="processChilds">
										<xsl:with-param name="bgcolor">
											<xsl:choose>
												<xsl:when test="$bgcolor = '#EEFAFF'">
													<xsl:text>#FFFFFF</xsl:text>
												</xsl:when>
												<xsl:when test="$bgcolor = '#FFFFFF'">
													<xsl:text>#EEFAFF</xsl:text>
												</xsl:when>
											</xsl:choose>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
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
			<xsl:text>/</xsl:text>
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
		<a href="addFilter.jsp?xpath={$xpath}&amp;connectionIndex={//urlparameters/parameter[@name='connectionIndex']/@value}&amp;mappingIndex={//urlparameters/parameter[@name='mappingIndex']/@value}" style="color:#000000;font-weight:bold">
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
