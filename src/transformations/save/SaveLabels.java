package transformations.save;



/**
 * Enum with the labels used to store and open files from the UMLR model
 * stored logically and graphically.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public enum SaveLabels {
	CLASS("[class]"),
		CLASS_NAME("[className]"),
		ABSTRACT_CLASS("[classAbstract]"),
		ID_CLASS("[classId]"),
		NODE_POINT("[nodePoint]"),
	ATTRIBUTE("[attribute]"),
		ATTRIBUTE_NAME("[attrName]"),
		ATTRIBUTE_TYPE("[attrType]"),
		ATTRIBUTE_ID("[attrId]"),
		ORDERED_ATTRIBUTE("[attrOrdered]"),
		UNIQUE_ATTRIBUTE("[attrUnique]"),
	RELATIONSHIP("[relationship]"),
		REL_NAME("[relName]"),
		REL_TYPE("[relType]"),
		REL_ID("[relId]"),
		REL_END("[relEndpoint]"),
			END_NAME("[epName]"),
			BROWSABLE_END("[epBrowsable]"),
			UNIQUE_END("[epUnique]"),
			ORDERED_END("[epOrdered]"),
			END_CARDINALITY("[epCardinality]"),
			END_TYPE("[epType]"),
			END_CLASS("[epClass]"),
	ASSOC_CLASS("[associationClass]"),
		ASSOC_CLASS_ID("[assocClassId]"),
		
	// Closure labels
	CLASS_CLOSURE("[/c]"),
	RELATIONSHIP_CLOSURE("[/r]"),
	RELATIONSHIP_END_CLOSURE("[/er]"),
	ATTRIBUTE_CLOSURE("[/a]"),
	ASSOC_CLASS_CLOSURE("[/ca]");
	
	
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



    /**
     * Method to obtain an instance of the label, based on the
     * name received as a parameter.
     * @param n The name used to search for a label.
     * @return The instance, if the name matches, or null otherwise.
     */
	public static SaveLabels getLabels(String n){
		if(CLASS.getName().equals(n))
			return CLASS;
		else if(CLASS_NAME.getName().equals(n))
			return CLASS_NAME;
		else if(ABSTRACT_CLASS.getName().equals(n))
			return ABSTRACT_CLASS;
		else if(ID_CLASS.getName().equals(n))
			return ID_CLASS;
		else if(NODE_POINT.getName().equals(n))
			return NODE_POINT;
		else if(ATTRIBUTE.getName().equals(n))
			return ATTRIBUTE;
		else if(ATTRIBUTE_NAME.getName().equals(n))
			return ATTRIBUTE_NAME;
		else if(ATTRIBUTE_TYPE.getName().equals(n))
			return ATTRIBUTE_TYPE;
		else if(ATTRIBUTE_ID.getName().equals(n))
			return ATTRIBUTE_ID;
		else if(ORDERED_ATTRIBUTE.getName().equals(n))
			return ORDERED_ATTRIBUTE;
		else if(UNIQUE_ATTRIBUTE.getName().equals(n))
			return UNIQUE_ATTRIBUTE;
		else if(RELATIONSHIP.getName().equals(n))
			return RELATIONSHIP;
		else if(REL_NAME.getName().equals(n))
			return REL_NAME;
		else if(REL_TYPE.getName().equals(n))
			return REL_TYPE;
		else if(REL_ID.getName().equals(n))
			return REL_ID;
		else if(REL_END.getName().equals(n))
			return REL_END;
		else if(END_NAME.getName().equals(n))
			return END_NAME;
		else if(BROWSABLE_END.getName().equals(n))
			return BROWSABLE_END;
		else if(UNIQUE_END.getName().equals(n))
			return UNIQUE_END;
		else if(ORDERED_END.getName().equals(n))
			return ORDERED_END;
		else if(END_CARDINALITY.getName().equals(n))
			return END_CARDINALITY;
		else if(END_TYPE.getName().equals(n))
			return END_TYPE;
		else if(END_CLASS.getName().equals(n))
			return END_CLASS;
		else if(ASSOC_CLASS.getName().equals(n))
			return ASSOC_CLASS;
		else if(ASSOC_CLASS_ID.getName().equals(n))
			return ASSOC_CLASS_ID;
		else if(CLASS_CLOSURE.getName().equals(n))
			return CLASS_CLOSURE;
		else if(RELATIONSHIP_CLOSURE.getName().equals(n))
			return RELATIONSHIP_CLOSURE;
		else if(RELATIONSHIP_END_CLOSURE.getName().equals(n))
			return RELATIONSHIP_END_CLOSURE;
		else if(ATTRIBUTE_CLOSURE.getName().equals(n))
			return ATTRIBUTE_CLOSURE;
		else if(ASSOC_CLASS_CLOSURE.getName().equals(n))
			return ASSOC_CLASS_CLOSURE;
		
		return null;
	}

}
