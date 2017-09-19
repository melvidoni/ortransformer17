<?xml version="1.0" encoding="UTF-8"?>
<!--
This file was generated by Altova MapForce 2009sp1

YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.

Refer to the Altova MapForce Documentation for further details.
http://www.altova.com/mapforce
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:grp="http://www.altova.com/Mapforce/grouping"
                exclude-result-prefixes="fn grp xs xsi xsl">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:param name="Param_1" select="''"/>
    <xsl:param name="Param_2" select="''"/>
    <xsl:param name="Param_3" select="''"/>
    <xsl:param name="Param_4" select="''"/>
    <xsl:param name="Param_5" select="''"/>
    <xsl:param name="Param_6" select="''"/>
    <xsl:param name="Param_7" select="''"/>
    <xsl:param name="Param_8" select="''"/>
    <xsl:param name="SQL2003Metamodel_data_1" select="''"/>

    <xsl:template match="/">
        <SQLSchema>
            <xsl:attribute name="xsi:noNamespaceSchemaLocation" separator=" ">
                <xsl:sequence select="$SQL2003Metamodel_data_1"/>
            </xsl:attribute>
            <xsl:variable name="var1_instance" as="node()" select="."/>
            <xsl:for-each select="$var1_instance/Package">
                <xsl:variable name="var2_Package" as="node()" select="."/>
                <xsl:if test="$var2_Package/@uname">
                    <xsl:attribute name="uname">
                        <xsl:sequence select="xs:string(@uname)"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:for-each select="UClass">
                    <xsl:variable name="var4_Class" as="node()" select="."/>
                    <StructuredType>
                        <xsl:attribute name="uname">
                            <xsl:sequence select="xs:string(@uname)"/>
                        </xsl:attribute>
                        <xsl:if test="$var4_Class/@isAbstract">
                            <xsl:attribute name="uninstantiable">
                                <xsl:sequence select="xs:string(xs:boolean(@isAbstract))"/>
                            </xsl:attribute>
                        </xsl:if>
                        <xsl:if test="$var4_Class/@superclass">
                            <xsl:attribute name="supertype">
                                <xsl:sequence select="xs:string(@superclass)"/>
                            </xsl:attribute>
                        </xsl:if>
                        <xsl:for-each select="UAttribute">
                            <Attribute>
                                <xsl:attribute name="uname">
                                    <xsl:sequence select="xs:string(@uname)"/>
                                </xsl:attribute>
                                <Type>
                                    <PredefinedType>
                                        <xsl:attribute name="type">
                                            <xsl:sequence select="xs:string(attributeType)"/>
                                        </xsl:attribute>
                                    </PredefinedType>
                                </Type>
                            </Attribute>
                        </xsl:for-each>
                        <xsl:for-each select="Pseudoattribute">
                            <xsl:variable name="var8_Pseudoattribute" as="node()" select="."/>
                            <Attribute>
                                <xsl:if test="$var8_Pseudoattribute/@uname">
                                    <xsl:attribute name="uname">
                                        <xsl:sequence select="xs:string(@uname)"/>
                                    </xsl:attribute>
                                </xsl:if>
                                <Type>
                                    <ReferenceType>
                                        <StructuredType>
                                            <xsl:for-each select="doc($Param_1)/Package/UClass">
                                                <xsl:variable name="var12_cond_result_exists" as="xs:boolean*">
                                                    <xsl:if test="$var8_Pseudoattribute/@uname">
                                                        <xsl:for-each select="Endpoint">
                                                            <xsl:variable name="var14_Endpoint" as="node()" select="."/>
                                                            <xsl:if test="$var14_Endpoint/@uname">
                                                                <xsl:if test="$var14_Endpoint/@browsable">
                                                                    <xsl:if test="$var14_Endpoint/@endpointType">
                                                                        <xsl:if test="$var14_Endpoint/max/@value">
                                                                            <xsl:if test="((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:string(xs:boolean(@browsable)) = 'true')) and (xs:string(@endpointType) != 'composed')) and (xs:decimal(xs:integer(max/@value)) = xs:decimal(1)))">
                                                                                <xsl:sequence select="fn:true()"/>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:if>
                                                        </xsl:for-each>
                                                    </xsl:if>
                                                </xsl:variable>
                                                <xsl:if test="fn:exists($var12_cond_result_exists)">
                                                    <xsl:attribute name="uname">
                                                        <xsl:sequence select="xs:string(@uname)"/>
                                                    </xsl:attribute>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </StructuredType>
                                    </ReferenceType>
                                    <Arrangement>
                                        <xsl:if test="$var8_Pseudoattribute/max/@value">
                                            <xsl:if test="((xs:decimal(xs:integer(max/@value)) &lt; xs:decimal(99999)) and (xs:decimal(xs:integer(max/@value)) &gt; xs:decimal(1)))">
                                                <xsl:attribute name="max_element_number">
                                                    <xsl:sequence select="xs:string(xs:integer(max/@value))"/>
                                                </xsl:attribute>
                                            </xsl:if>
                                        </xsl:if>
                                        <StructuredType>
                                            <xsl:for-each select="doc($Param_3)/Package/UClass">
                                                <xsl:variable name="var18_cond_result_exists" as="xs:boolean*">
                                                    <xsl:if test="$var8_Pseudoattribute/@name">
                                                        <xsl:for-each select="Endpoint">
                                                            <xsl:variable name="var20_Endpoint" as="node()" select="."/>
                                                            <xsl:if test="$var20_Endpoint/@uname">
                                                                <xsl:if test="$var20_Endpoint/@endpointType">
                                                                    <xsl:if test="$var20_Endpoint/@isComposed">
                                                                        <xsl:if test="$var20_Endpoint/max/@value">
                                                                            <xsl:if test="$var20_Endpoint/@browsable">
                                                                                <xsl:if test="((((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:string(@endpointType) = 'none')) and (xs:string(xs:boolean(@isComposed)) = 'true')) and (xs:decimal(xs:integer(max/@value)) &gt; xs:decimal(1))) and (xs:decimal(xs:integer(max/@value)) &lt; xs:decimal(99999))) and (xs:string(xs:boolean(@browsable)) = 'true'))">
                                                                                    <xsl:sequence select="fn:true()"/>
                                                                                </xsl:if>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:if>
                                                        </xsl:for-each>
                                                    </xsl:if>
                                                </xsl:variable>
                                                <xsl:if test="fn:exists($var18_cond_result_exists)">
                                                    <xsl:attribute name="uname">
                                                        <xsl:sequence select="xs:string(@uname)"/>
                                                    </xsl:attribute>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </StructuredType>
                                        <ReferenceType>
                                            <StructuredType>
                                                <xsl:for-each select="doc($Param_2)/Package/UClass">
                                                    <xsl:variable name="var24_cond_result_exists" as="xs:boolean*">
                                                        <xsl:if test="$var8_Pseudoattribute/@uname">
                                                            <xsl:for-each select="Endpoint">
                                                                <xsl:variable name="var26_Endpoint" as="node()"
                                                                              select="."/>
                                                                <xsl:if test="$var26_Endpoint/@uname">
                                                                    <xsl:if test="$var26_Endpoint/@endpointType">
                                                                        <xsl:if test="$var26_Endpoint/@isComposed">
                                                                            <xsl:if test="$var26_Endpoint/max/@value">
                                                                                <xsl:if test="$var26_Endpoint/@browsable">
                                                                                    <xsl:if test="((((((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:string(@endpointType) != 'composed')) and (xs:string(xs:boolean(@isComposed)) = 'false')) and (xs:string(@endpointType) = 'none')) and (xs:decimal(xs:integer(max/@value)) &gt; xs:decimal(1))) and (xs:decimal(xs:integer(max/@value)) != xs:decimal(-1))) and (xs:decimal(xs:integer(max/@value)) &lt; xs:decimal(99999))) and (xs:string(xs:boolean(@browsable)) = 'true'))">
                                                                                        <xsl:sequence
                                                                                                select="fn:true()"/>
                                                                                    </xsl:if>
                                                                                </xsl:if>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:for-each>
                                                        </xsl:if>
                                                    </xsl:variable>
                                                    <xsl:if test="fn:exists($var24_cond_result_exists)">
                                                        <xsl:attribute name="uname">
                                                            <xsl:sequence select="xs:string(@uname)"/>
                                                        </xsl:attribute>
                                                    </xsl:if>
                                                </xsl:for-each>
                                            </StructuredType>
                                            <StructuredType>
                                                <xsl:for-each select="doc($Param_6)/Package/AssociationClass">
                                                    <xsl:variable name="var30_cond_result_exists" as="xs:boolean*">
                                                        <xsl:if test="$var8_Pseudoattribute/@uname">
                                                            <xsl:for-each select="Endpoint">
                                                                <xsl:variable name="var32_Endpoint" as="node()"
                                                                              select="."/>
                                                                <xsl:if test="$var32_Endpoint/@uname">
                                                                    <xsl:if test="$var32_Endpoint/@endpointType">
                                                                        <xsl:if test="$var32_Endpoint/max/@value">
                                                                            <xsl:if test="$var32_Endpoint/@browsable">
                                                                                <xsl:if test="$var32_Endpoint/@isComposed">
                                                                                    <xsl:if test="((((((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:string(@endpointType) != 'composed')) and (xs:string(@endpointType) = 'none')) and (xs:decimal(xs:integer(max/@value)) &gt; xs:decimal(1))) and (xs:decimal(xs:integer(max/@value)) != xs:decimal(-1))) and (xs:decimal(xs:integer(max/@value)) &lt; xs:decimal(99999))) and (xs:string(xs:boolean(@browsable)) = 'true')) and (xs:string(xs:boolean(@isComposed)) = 'false'))">
                                                                                        <xsl:sequence
                                                                                                select="fn:true()"/>
                                                                                    </xsl:if>
                                                                                </xsl:if>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:for-each>
                                                        </xsl:if>
                                                    </xsl:variable>
                                                    <xsl:if test="fn:exists($var30_cond_result_exists)">
                                                        <xsl:attribute name="uname">
                                                            <xsl:sequence select="xs:string(@uname)"/>
                                                        </xsl:attribute>
                                                    </xsl:if>
                                                </xsl:for-each>
                                            </StructuredType>
                                        </ReferenceType>
                                    </Arrangement>
                                    <Multiset>
                                        <StructuredType>
                                            <xsl:for-each select="doc($Param_5)/Package/UClass">
                                                <xsl:variable name="var36_cond_result_exists" as="xs:boolean*">
                                                    <xsl:if test="$var8_Pseudoattribute/@uname">
                                                        <xsl:for-each select="Endpoint">
                                                            <xsl:variable name="var38_Endpoint" as="node()" select="."/>
                                                            <xsl:if test="$var38_Endpoint/@uname">
                                                                <xsl:if test="$var38_Endpoint/max/@value">
                                                                    <xsl:if test="$var38_Endpoint/@browsable">
                                                                        <xsl:if test="$var38_Endpoint/@isComposed">
                                                                            <xsl:if test="((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:decimal(xs:integer(max/@value)) = xs:decimal(-1))) and (xs:string(xs:boolean(@browsable)) = 'true')) and (xs:string(xs:boolean(@isComposed)) = 'true'))">
                                                                                <xsl:sequence select="fn:true()"/>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:if>
                                                        </xsl:for-each>
                                                    </xsl:if>
                                                </xsl:variable>
                                                <xsl:if test="fn:exists($var36_cond_result_exists)">
                                                    <xsl:attribute name="uname">
                                                        <xsl:sequence select="xs:string(@nombre)"/>
                                                    </xsl:attribute>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </StructuredType>
                                        <ReferenceType>
                                            <StructuredType>
                                                <xsl:for-each select="doc($Param_4)/Package/UClass">
                                                    <xsl:variable name="var42_cond_result_exists" as="xs:boolean*">
                                                        <xsl:if test="$var8_Pseudoattribute/@uname">
                                                            <xsl:for-each select="Endpoint">
                                                                <xsl:variable name="var44_Endpoint" as="node()"
                                                                              select="."/>
                                                                <xsl:if test="$var44_Endpoint/@uname">
                                                                    <xsl:if test="$var44_Endpoint/max/@value">
                                                                        <xsl:if test="$var44_Endpoint/@isComposed">
                                                                            <xsl:if test="$var44_Endpoint/@browsable">
                                                                                <xsl:if test="((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:decimal(xs:integer(max/@value)) = xs:decimal(-1))) and (xs:string(xs:boolean(@isComposed)) != 'true')) and (xs:string(xs:boolean(@browsable)) = 'true'))">
                                                                                    <xsl:sequence select="fn:true()"/>
                                                                                </xsl:if>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:for-each>
                                                        </xsl:if>
                                                    </xsl:variable>
                                                    <xsl:if test="fn:exists($var42_cond_result_exists)">
                                                        <xsl:attribute name="nombre">
                                                            <xsl:sequence select="xs:string(@nombre)"/>
                                                        </xsl:attribute>
                                                    </xsl:if>
                                                </xsl:for-each>
                                            </StructuredType>
                                            <StructuredType>
                                                <xsl:for-each select="doc($Param_7)/Package/AssociationClass">
                                                    <xsl:variable name="var48_cond_result_exists" as="xs:boolean*">
                                                        <xsl:if test="$var8_Pseudoattribute/@uname">
                                                            <xsl:for-each select="Endpoint">
                                                                <xsl:variable name="var50_Endpoint" as="node()"
                                                                              select="."/>
                                                                <xsl:if test="$var50_Endpoint/@uname">
                                                                    <xsl:if test="$var50_Endpoint/max/@value">
                                                                        <xsl:if test="$var50_Endpoint/@isComposed">
                                                                            <xsl:if test="$var50_Endpoint/@browsable">
                                                                                <xsl:if test="((((xs:string($var8_Pseudoattribute/@uname) = xs:string(@name)) and (xs:decimal(xs:integer(max/@value)) = xs:decimal(-1))) and (xs:string(xs:boolean(@isComposed)) = 'false')) and (xs:string(xs:boolean(@browsable)) = 'true'))">
                                                                                    <xsl:sequence select="fn:true()"/>
                                                                                </xsl:if>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:for-each>
                                                        </xsl:if>
                                                    </xsl:variable>
                                                    <xsl:if test="fn:exists($var48_cond_result_exists)">
                                                        <xsl:attribute name="uname">
                                                            <xsl:sequence select="xs:string(@uname)"/>
                                                        </xsl:attribute>
                                                    </xsl:if>
                                                </xsl:for-each>
                                            </StructuredType>
                                        </ReferenceType>
                                    </Multiset>
                                </Type>
                            </Attribute>
                        </xsl:for-each>
                    </StructuredType>
                </xsl:for-each>
                <xsl:for-each select="AssociationClass">
                    <xsl:variable name="var52_AssociationClass" as="node()" select="."/>
                    <StructuredType>
                        <xsl:attribute name="uname">
                            <xsl:sequence select="xs:string(@uname)"/>
                        </xsl:attribute>
                        <xsl:if test="$var52_AssociationClass/@isAbstract">
                            <xsl:attribute name="uninstantiable">
                                <xsl:sequence select="xs:string(xs:boolean(@isAbstract))"/>
                            </xsl:attribute>
                        </xsl:if>
                        <xsl:if test="$var52_AssociationClass/@superclass">
                            <xsl:attribute name="supertype">
                                <xsl:sequence select="xs:string(@superclass)"/>
                            </xsl:attribute>
                        </xsl:if>
                        <xsl:for-each select="UAttribute">
                            <Attribute>
                                <xsl:attribute name="uname">
                                    <xsl:sequence select="xs:string(@uname)"/>
                                </xsl:attribute>
                                <Type>
                                    <PredefinedType>
                                        <xsl:attribute name="type">
                                            <xsl:sequence select="xs:string(attributeType)"/>
                                        </xsl:attribute>
                                    </PredefinedType>
                                </Type>
                            </Attribute>
                        </xsl:for-each>
                        <xsl:for-each select="Pseudoattribute">
                            <xsl:variable name="var56_Pseudoattribute" as="node()" select="."/>
                            <Attribute>
                                <xsl:if test="$var56_Pseudoattribute/@uname">
                                    <xsl:attribute name="uname">
                                        <xsl:sequence select="xs:string(@uname)"/>
                                    </xsl:attribute>
                                </xsl:if>
                                <Type>
                                    <ReferenceType>
                                        <StructuredType>
                                            <xsl:for-each select="doc($Param_8)/Packaeg/UClass">
                                                <xsl:variable name="var60_cond_result_exists" as="xs:boolean*">
                                                    <xsl:if test="$var56_Pseudoattribute/@uname">
                                                        <xsl:for-each select="Endpoint">
                                                            <xsl:variable name="var62_Endpoint" as="node()" select="."/>
                                                            <xsl:if test="$var62_Endpoint/@uname">
                                                                <xsl:if test="$var62_Endpoint/@isComposed">
                                                                    <xsl:if test="$var62_Endpoint/@browsable">
                                                                        <xsl:if test="$var62_Endpoint/@endpointType">
                                                                            <xsl:if test="$var62_Endpoint/max/@value">
                                                                                <xsl:if test="(((((xs:string($var56_Pseudoattribute/@uname) = xs:string(@name)) and (xs:string(xs:boolean(@isComposed)) = 'false')) and (xs:string(xs:boolean(@browsable)) = 'true')) and (xs:string(@endpointType) != 'composed')) and (xs:decimal(xs:integer(max/@value)) = xs:decimal(1)))">
                                                                                    <xsl:sequence select="fn:true()"/>
                                                                                </xsl:if>
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:if>
                                                                </xsl:if>
                                                            </xsl:if>
                                                        </xsl:for-each>
                                                    </xsl:if>
                                                </xsl:variable>
                                                <xsl:if test="fn:exists($var60_cond_result_exists)">
                                                    <xsl:attribute name="uname">
                                                        <xsl:sequence select="xs:string(@uname)"/>
                                                    </xsl:attribute>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </StructuredType>
                                    </ReferenceType>
                                </Type>
                            </Attribute>
                        </xsl:for-each>
                    </StructuredType>
                </xsl:for-each>
            </xsl:for-each>
        </SQLSchema>
    </xsl:template>
</xsl:stylesheet>