package transformations.save;



/**
 * Enum with the labels used to store and open files from the UMLR model
 * stored logically and graphically.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public enum SaveLabels {
	DOC_ROOT("components"),
    DOC_CLASSES("classes"),
    DOC_RELS("relationships"),
    DOC_ASSOCCLASS("assocClass"),
    DOC_ATTRIBUTES("attributes"),



	CLASS("class"),
		CLASS_NAME("className"),
		CLASS_ABSTRACT("classAbstract"),
		CLASS_ID("classId"),
		NODE_POINT_X("nodePointX"),
        NODE_POINT_Y("nodePointY"),
	ATTRIBUTE("attribute"),
		ATTRIBUTE_NAME("attrName"),
		ATTRIBUTE_TYPE("attrType"),
		ATTRIBUTE_ID("attrId"),
		ATTRIBUTE_ORDERED("attrOrdered"),
		ATTRIBUTE_UNIQUE("attrUnique"),
	RELATIONSHIP("relationship"),
		REL_NAME("relName"),
		REL_TYPE("relType"),
		REL_ID("relId"),
		ENDPOINT_REL("relEndpoint"),
			END_NAME("epName"),
			END_BROWSABLE("epBrowsable"),
			END_UNIQUE("epUnique"),
			END_ORDERED("epOrdered"),
			END_CARDINALITY("epCardinality"),
			END_TYPE("epType"),
			END_CLASS("epClass"),
            END_ORIGIN("epOrigin"),
	ASSOC_CLASS("associationClass"),
		ASSOC_CLASS_ID("assocClassId");
	
	String name;





    /**
     * Default constructor of the enum, to initialize
     * it with the values received as a parameter.
     * @param n The new name to export.
     */
	SaveLabels(String n){
		name = n;
	}


    /**
     * Getter to obtain the name of the instance.
     * @return The name as a string.
     */
	public String getName(){
		return name;
	}

}
