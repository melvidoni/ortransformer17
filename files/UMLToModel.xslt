<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="2.1">
	<xsl:output indent="yes" omit-xml-declaration="yes" />
	<xsl:param name="UMLMetamodel" select="''" />
	<xsl:template match="diagram">
		<Package uname="Package" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				 xsi:noNamespaceSchemaLocation="{$UMLMetamodel}">
			<xsl:apply-templates />
		</Package>
	</xsl:template>
	<xsl:template match="class">
		<xsl:variable name="class" select="@name" />
		<xsl:variable name="classId" select="@id" />
		<xsl:variable name="superClassesAux">
			<xsl:for-each select="../relationship">
				<xsl:if test="@type = 'Generalization'">
					<xsl:for-each select="origin">
						<xsl:variable name="originClassId" select="@class" />
						<xsl:if test="$classId = $originClassId">
							<xsl:for-each select="../destination">
								<xsl:variable name="destinationClassId" select="@class" />
								<xsl:value-of select="string('-')" />
								<xsl:for-each select="../../class">
									<xsl:if test="@id = $destinationClassId">
										<xsl:value-of select="@name" />
									</xsl:if>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="superClasses">
			<xsl:choose>
				<xsl:when test="string-length($superClassesAux) = 0">
					<xsl:value-of select="string('')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="substring($superClassesAux,2)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!--<xsl:if test="contains($superClasses,'-') =  true">
			<xsl:message terminate="yes">
				Error 1: There is at least one multiple hierarchy.
				<xsl:value-of select="$superClasses" />
				//
			</xsl:message>
		</xsl:if>-->
		<UClass uname="{@name}" isAbstract="{@abstract}" superclass="{$superClasses}">
			<xsl:for-each select="attribute">
				<UAttribute uname="{@name}" isOrdered="{@ordered}"
							isUnique="{@unique}">
					<min type="LiteralInteger" value="1" />
					<max type="LiteralInteger" value="1" />
					<attributeType>
						<xsl:value-of select="@type" />
					</attributeType>
				</UAttribute>
			</xsl:for-each>
			<xsl:for-each select="../relationship">
				<xsl:if test="@type != 'Generalization'">
					<xsl:variable name="isOriginComposed">
						<xsl:for-each select="destination">
							<xsl:variable name="destinationType" select="@type" />
							<xsl:choose>
								<xsl:when test="$destinationType = string('composed')">
									<xsl:value-of select="string('true')" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="string('false')" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:variable>
					<xsl:variable name="isDestinationComposed">
						<xsl:for-each select="origin">
							<xsl:variable name="originType" select="@type" />
							<xsl:choose>
								<xsl:when test="$originType = string('composed')">
									<xsl:value-of select="string('true')" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="string('false')" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:variable>
					<xsl:for-each select="origin">
						<xsl:variable name="originClassId" select="@class" />
						<xsl:if test="$classId = $originClassId">
							<xsl:if test="@browsable = 'true'">
								<Endpoint uname="{@name}" isOrdered="{@ordered}"
										  isUnique="{@unique}" isComposed="{$isOriginComposed}"
										  browsable="{@browsable}" endpointType="{@type}">
									<xsl:variable name="cardinality" select="@cardinality" />
									<xsl:variable name="min"
										select="substring-before($cardinality,'..')" />
									<xsl:variable name="max"
										select="substring-after($cardinality,'..')" />
									<min type="LiteralInteger" value="{$min}" />
									<xsl:choose>
										<xsl:when test="$max = '*'">
											<max type="LiteralUnlimitedNatural" value="-1" />
										</xsl:when>
										<xsl:otherwise>
											<max type="LiteralInteger" value="{$max}" />
										</xsl:otherwise>
									</xsl:choose>
								</Endpoint>
							</xsl:if>
							<xsl:for-each select="../destination">
								<xsl:if test="@browsable = 'true'">
									<Pseudoattribute uname="{@name}">
										<xsl:variable name="cardinality" select="@cardinality" />
										<xsl:variable name="min"
											select="substring-before($cardinality,'..')" />
										<xsl:variable name="max"
											select="substring-after($cardinality,'..')" />
										<min type="LiteralInteger" value="{$min}" />
										<xsl:choose>
											<xsl:when test="$max = '*'">
												<max type="LiteralUnlimitedNatural" value="-1" />
											</xsl:when>
											<xsl:otherwise>
												<max type="LiteralInteger" value="{$max}" />
											</xsl:otherwise>
										</xsl:choose>
									</Pseudoattribute>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="destination">
						<xsl:variable name="destinationClassId" select="@class" />
						<xsl:if test="$classId = $destinationClassId">
							<xsl:if test="@browsable = 'true'">
								<Endpoint uname="{@name}" isOrdered="{@ordered}"
										  isUnique="{@unique}" isComposed="{$isDestinationComposed}"
										  browsable="{@browsable}" endpointType="{@type}">
									<xsl:variable name="cardinality" select="@cardinality" />
									<xsl:variable name="min"
										select="substring-before($cardinality,'..')" />
									<xsl:variable name="max"
										select="substring-after($cardinality,'..')" />
									<min type="LiteralInteger" value="{$min}" />
									<xsl:choose>
										<xsl:when test="$max = '*'">
											<max type="LiteralUnlimitedNatural" value="-1" />
										</xsl:when>
										<xsl:otherwise>
											<max type="LiteralInteger" value="{$max}" />
										</xsl:otherwise>
									</xsl:choose>
								</Endpoint>
							</xsl:if>
							<xsl:for-each select="../origin">
								<xsl:if test="@browsable = 'true'">
									<Pseudoattribute uname="{@name}">
										<xsl:variable name="cardinality" select="@cardinality" />
										<xsl:variable name="min"
											select="substring-before($cardinality,'..')" />
										<xsl:variable name="max"
											select="substring-after($cardinality,'..')" />
										<min type="LiteralInteger" value="{$min}" />
										<xsl:choose>
											<xsl:when test="$max = '*'">
												<max type="LiteralUnlimitedNatural" value="-1" />
											</xsl:when>
											<xsl:otherwise>
												<max type="LiteralInteger" value="{$max}" />
											</xsl:otherwise>
										</xsl:choose>
									</Pseudoattribute>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select="../associationClass">
				<xsl:variable name="associationClassName">
					<xsl:for-each select="class">
						<xsl:value-of select="@name" />
					</xsl:for-each>
				</xsl:variable>
				<xsl:for-each select="relationship">
					<xsl:if test="@type != 'Generalization'">
						<xsl:variable name="isOriginComposed">
							<xsl:for-each select="destination">
								<xsl:variable name="destinationType" select="@type" />
								<xsl:choose>
									<xsl:when test="$destinationType = string('composed')">
										<xsl:value-of select="string('true')" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="string('false')" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</xsl:variable>
						<xsl:variable name="isDestinationComposed">
							<xsl:for-each select="origin">
								<xsl:variable name="originType" select="@type" />
								<xsl:choose>
									<xsl:when test="$originType = string('composed')">
										<xsl:value-of select="string('true')" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="string('false')" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</xsl:variable>
						<xsl:for-each select="origin">
							<xsl:variable name="originClassId" select="@class" />
							<xsl:if test="$classId = $originClassId">
								<xsl:if test="@browsable = 'true'">
									<Endpoint uname="{@name}" isOrdered="{@ordered}"
											  isUnique="{@unique}" isComposed="{$isOriginComposed}"
											  browsable="{@browsable}" endpointType="{@type}">
										<xsl:variable name="cardinality" select="@cardinality" />
										<xsl:variable name="min"
											select="substring-before($cardinality,'..')" />
										<xsl:variable name="max"
											select="substring-after($cardinality,'..')" />
										<min type="LiteralInteger" value="1" />
										<max type="LiteralInteger" value="1" />
									</Endpoint>
								</xsl:if>
								<xsl:for-each select="../destination">
									<xsl:variable name="acName"
										select="concat($class, $associationClassName)" />
									<Pseudoattribute uname="{$acName}">
										<xsl:variable name="cardinality" select="@cardinality" />
										<xsl:variable name="min"
											select="substring-before($cardinality,'..')" />
										<xsl:variable name="max"
											select="substring-after($cardinality,'..')" />
										<min type="LiteralInteger" value="{$min}" />
										<xsl:choose>
											<xsl:when test="$max = '*'">
												<max type="LiteralUnlimitedNatural" value="-1" />
											</xsl:when>
											<xsl:otherwise>
												<max type="LiteralInteger" value="{$max}" />
											</xsl:otherwise>
										</xsl:choose>
									</Pseudoattribute>
								</xsl:for-each>
							</xsl:if>
						</xsl:for-each>
						<xsl:for-each select="destination">
							<xsl:variable name="destinationClassId" select="@class" />
							<xsl:if test="$classId = $destinationClassId">
								<xsl:if test="@browsable = 'true'">
									<Endpoint uname="{@name}" isOrdered="{@ordered}"
											  isUnique="{@unique}" isComposed="{$isDestinationComposed}"
											  browsable="{@browsable}" endpointType="{@type}">
										<xsl:variable name="cardinality" select="@cardinality" />
										<xsl:variable name="min"
											select="substring-before($cardinality,'..')" />
										<xsl:variable name="max"
											select="substring-after($cardinality,'..')" />
										<min type="LiteralInteger" value="1" />
										<max type="LiteralInteger" value="1" />
									</Endpoint>
								</xsl:if>
								<xsl:for-each select="../origin">
									<xsl:variable name="acName"
										select="concat($class, $associationClassName)" />
									<Pseudoattribute uname="{$acName}">
										<xsl:variable name="cardinality" select="@cardinality" />
										<xsl:variable name="min"
											select="substring-before($cardinality,'..')" />
										<xsl:variable name="max"
											select="substring-after($cardinality,'..')" />
										<min type="LiteralInteger" value="{$min}" />
										<xsl:choose>
											<xsl:when test="$max = '*'">
												<max type="LiteralUnlimitedNatural" value="-1" />
											</xsl:when>
											<xsl:otherwise>
												<max type="LiteralInteger" value="{$max}" />
											</xsl:otherwise>
										</xsl:choose>
									</Pseudoattribute>
								</xsl:for-each>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>
				</xsl:for-each>
			</xsl:for-each>
		</UClass>
	</xsl:template>
	<xsl:template match="relationship">
		<xsl:if test="@type != 'Generalization'">
			<Association uname="{@name}" associationType="{@type}" />
		</xsl:if>
	</xsl:template>
	<xsl:template match="associationClass">
		<xsl:for-each select="class">
			<xsl:variable name="class" select="@name" />
			<xsl:variable name="classId" select="@nameId" />
			<xsl:variable name="superClassesAux">
				<xsl:for-each select="../relationship">
					<xsl:if test="@type = 'Generalization'">
						<xsl:for-each select="origin">
							<xsl:variable name="originClassId" select="@class" />
							<xsl:if test="$classId = $originClassId">
								<xsl:for-each select="../destination">
									<xsl:variable name="destinationClassId" select="@class" />
									<xsl:value-of select="string('-')" />
									<xsl:for-each select="../../class">
										<xsl:if test="@id = $destinationClassId">
											<xsl:value-of select="@name" />
										</xsl:if>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			<xsl:variable name="superClasses">
				<xsl:choose>
					<xsl:when test="string-length($superClassesAux) = 0">
						<xsl:value-of select="string('')" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="substring($superClassesAux,2)" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:if test="contains($superClasses,'-') =  true">
				<xsl:message terminate="yes">
					Error 1: There is at least one class with multiple hierarchy.
					<xsl:value-of select="$superClasses" />
					//
				</xsl:message>
			</xsl:if>
			<AssociationClass uname="{@name}" isAbstract="{@abstract}"
							  superclass="{$superClasses}">
				<xsl:for-each select="attribute">
					<UAttribute uname="{@name}" isOrdered="{@ordered}"
								isUnique="{@unique}">
						<min type="LiteralInteger" value="1" />
						<max type="LiteralInteger" value="1" />
						<attributeType>
							<xsl:value-of select="@type" />
						</attributeType>
					</UAttribute>
				</xsl:for-each>
				<xsl:for-each select="../../relationship">
					<xsl:variable name="isOriginComposed">
						<xsl:for-each select="destination">
							<xsl:variable name="destinationType" select="@type" />
							<xsl:choose>
								<xsl:when test="$destinationType = string('composed')">
									<xsl:value-of select="string('true')" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="string('false')" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:variable>
					<xsl:variable name="isDestinationComposed">
						<xsl:for-each select="origin">
							<xsl:variable name="originType" select="@type" />
							<xsl:choose>
								<xsl:when test="$originType = string('composed')">
									<xsl:value-of select="string('true')" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="string('false')" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:variable>
					<xsl:for-each select="origin">
						<xsl:if test="@class = $classId">
							<Endpoint uname="{@name}" isOrdered="{@ordered}"
									  isUnique="{@unique}" isComposed="{$isOriginComposed}" browsable="{@browsable}"
									  endpointType="{@type}">
								<xsl:variable name="cardinality" select="@cardinality" />
								<xsl:variable name="min"
									select="substring-before($cardinality,'..')" />
								<xsl:variable name="max"
									select="substring-after($cardinality,'..')" />
								<minima tipo="LiteralInteger" valor="{$min}" />
								<xsl:choose>
									<xsl:when test="$max = '*'">
										<max type="LiteralUnlimitedNatural" value="-1" />
									</xsl:when>
									<xsl:otherwise>
										<max type="LiteralInteger" value="{$max}" />
									</xsl:otherwise>
								</xsl:choose>
							</Endpoint>
							<xsl:variable name="originClassId" select="@class" />
							<xsl:for-each select="../../../class">
								<xsl:if test="@id = $originClassId">
									<Pseudoattribute uname="{@name}">
										<min type="LiteralInteger" value="1" />
										<max type="LiteralInteger" value="1" />
									</Pseudoattribute>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
					<xsl:for-each select="destination">
						<xsl:if test="@class = $classId">
							<Endpoint uname="{@name}" isOrdered="{@ordered}"
									  isUnique="{@unique}" isComposed="{$isDestinationComposed}"
									  browsable="{@browsable}" endpointType="{@type}">
								<xsl:variable name="cardinality" select="@cardinality" />
								<xsl:variable name="min"
									select="substring-before($cardinality,'..')" />
								<xsl:variable name="max"
									select="substring-after($cardinality,'..')" />
								<min type="LiteralInteger" value="{$min}" />
								<xsl:choose>
									<xsl:when test="$max = '*'">
										<max type="LiteralUnlimitedNatural" value="-1" />
									</xsl:when>
									<xsl:otherwise>
										<max type="LiteralInteger" value="{$max}" />
									</xsl:otherwise>
								</xsl:choose>
							</Endpoint>
							<xsl:variable name="destinationClassId" select="@class" />
							<xsl:for-each select="../../../class">
								<xsl:if test="@id = $destinationClassId">
									<Pseudoattribute uname="{@name}">
										<min type="LiteralInteger" value="1" />
										<max type="LiteralInteger" value="1" />
									</Pseudoattribute>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>
					</xsl:for-each>
				</xsl:for-each>
				<xsl:for-each select="../relationship">
					<xsl:variable name="isOriginComposed">
						<xsl:for-each select="destination">
							<xsl:variable name="destinationType" select="@type" />
							<xsl:choose>
								<xsl:when test="$destinationType = string('composed')">
									<xsl:value-of select="string('true')" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="string('false')" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:variable>
					<xsl:variable name="isDestinationComposed">
						<xsl:for-each select="origin">
							<xsl:variable name="originType" select="@type" />
							<xsl:choose>
								<xsl:when test="$originType = string('composed')">
									<xsl:value-of select="string('true')" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="string('false')" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</xsl:variable>
					<xsl:variable name="originClassName">
						<xsl:for-each select="origin">
							<xsl:variable name="originClassId" select="@class" />
							<xsl:for-each select="../../../class">
								<xsl:if test="@id = $originClassId">
									<xsl:value-of select="@name" />
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:variable>
					<xsl:variable name="destinationClassName">
						<xsl:for-each select="destination">
							<xsl:variable name="destinationClassId" select="@class" />
							<xsl:for-each select="../../../class">
								<xsl:if test="@id = $destinationClassId">
									<xsl:value-of select="@name" />
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:variable>
					<xsl:for-each select="origin">
						<xsl:variable name="originType" select="@type" />
						<xsl:variable name="ordered" select="@ordered" />
						<xsl:variable name="unique" select="@unique" />
						<xsl:variable name="browsable" select="@browsable" />
						<xsl:variable name="cardinality" select="@cardinality" />
						<xsl:variable name="originName" select="@name" />
						<xsl:variable name="name"
							select="concat($destinationClassName, $class)" />
						<Endpoint uname="{$name}" isOrdered="{$ordered}"
								  isUnique="{$unique}" isComposed="{$isOriginComposed}" browsable="{$browsable}"
								  endpointType="{$originType}">
							<xsl:variable name="min"
								select="substring-before($cardinality,'..')" />
							<xsl:variable name="max"
								select="substring-after($cardinality,'..')" />
							<min type="LiteralInteger" value="{$min}" />
							<xsl:choose>
								<xsl:when test="$max = '*'">
									<max type="LiteralUnlimitedNatural" value="-1" />
								</xsl:when>
								<xsl:otherwise>
									<max type="LiteralInteger" value="{$max}" />
								</xsl:otherwise>
							</xsl:choose>
						</Endpoint>
						<Pseudoattribute uname="{$originName}">
							<min type="LiteralInteger" value="1" />
							<max type="LiteralInteger" value="1" />
						</Pseudoattribute>
					</xsl:for-each>
					<xsl:for-each select="destination">
						<xsl:variable name="destinationType" select="@type" />
						<xsl:variable name="ordered" select="@ordered" />
						<xsl:variable name="unique" select="@unique" />
						<xsl:variable name="browsable" select="@browsable" />
						<xsl:variable name="cardinality" select="@cardinality" />
						<xsl:variable name="destinationName" select="@name" />
						<xsl:variable name="name"
							select="concat($originClassName, $class)" />
						<Enpoint uname="{$name}" isOrdered="{$ordered}"
								 isUnique="{$unique}" isComposed="{$isDestinationComposed}" browsable="{$browsable}"
								 endpointType="{$destinationType}">
							<xsl:variable name="min"
								select="substring-before($cardinality,'..')" />
							<xsl:variable name="max"
								select="substring-after($cardinality,'..')" />
							<min type="LiteralInteger" value="{$min}" />
							<xsl:choose>
								<xsl:when test="$max = '*'">
									<max type="LiteralUnlimitedNatural" value="-1" />
								</xsl:when>
								<xsl:otherwise>
									<max type="LiteralInteger" value="{$max}" />
								</xsl:otherwise>
							</xsl:choose>
						</Enpoint>
						<Pseudoattribute uname="{$destinationName}">
							<min type="LiteralInteger" value="1" />
							<max type="LiteralInteger" value="1" />
						</Pseudoattribute>
					</xsl:for-each>
				</xsl:for-each>
			</AssociationClass>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>