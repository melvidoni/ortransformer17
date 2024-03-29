<?xml version="1.0" encoding="UTF-8"?>
<!--
This file was generated by Altova MapForce 2007

YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.

Refer to the Altova MapForce 2007 Documentation for further details.
http://www.altova.com/mapforce
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="fn xs">
	
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:param name="SQL2003Metamodel_data1" select="''"/>
	<xsl:param name="SQL2003Metamodel_data2" select="''"/>
	<xsl:param name="SQL2003Metamodel_schema_1" select="''"/>
	
	<xsl:template match="/SQLSchema">
		<SQLSchema>
			<xsl:attribute name="xsi:noNamespaceSchemaLocation"	separator=" ">
				<xsl:sequence select="$SQL2003Metamodel_schema_1"/>
			</xsl:attribute>
			<xsl:variable name="var1_instance" select="."/>
			<SchemaObject>
				<xsl:for-each select="$var1_instance/StructuredType">
					<xsl:variable name="var2_StructuredType" select="."/>
					<TypedTable>
						<xsl:if test="$var2_StructuredType/@uname">
							<xsl:variable name="var4_cond_result_exists" as="xs:boolean*">
								<xsl:if test="$var2_StructuredType/@uninstantiable">
									<xsl:for-each
										select="doc($SQL2003Metamodel_data2)/SQLSchema/StructuredType">
										<xsl:variable name="var6_StructuredType" select="." />
										<xsl:if test="$var6_StructuredType/@uname">
											<xsl:if
												test="((xs:string(xs:boolean($var2_StructuredType/@uninstantiable)) = 'false') and (xs:string($var2_StructuredType/@uname) != xs:string(@uname)))">
												<xsl:sequence select="fn:true()" />
											</xsl:if>
										</xsl:if>
									</xsl:for-each>
								</xsl:if>
							</xsl:variable>
							<xsl:if test="fn:exists($var4_cond_result_exists)">
								<xsl:attribute name="uname">
									<xsl:sequence select="fn:concat(xs:string(@uname), '')" />
								</xsl:attribute>
							</xsl:if>
						</xsl:if>
						<StructuredType>
							<xsl:if test="$var2_StructuredType/@uname">
								<xsl:variable name="var8_cond_result_exists" as="xs:boolean*">
									<xsl:if test="$var2_StructuredType/@uninstantiable">
										<xsl:for-each
											select="doc($SQL2003Metamodel_data1)/SQLSchema/StructuredType">
											<xsl:variable name="var10_StructuredType" select=" . " />
											<xsl:if test="$var10_StructuredType/@uname">
												<xsl:if
													test="((xs:string(xs:boolean($var2_StructuredType/@uninstantiable)) = 'false') and (xs:string($var2_StructuredType/@uname) != xs:string(@uname)))">
													<xsl:sequence select="fn:true()" />
												</xsl:if>
											</xsl:if>
										</xsl:for-each>
									</xsl:if>
								</xsl:variable>
								<xsl:if test="fn:exists($var8_cond_result_exists)">
									<xsl:attribute name="uname">
										<xsl:sequence select="xs:string(@uname)" />
									</xsl:attribute>
								</xsl:if>
							</xsl:if>
						</StructuredType>
					</TypedTable>
				</xsl:for-each>
			</SchemaObject>
		</SQLSchema>
	</xsl:template>
</xsl:stylesheet>