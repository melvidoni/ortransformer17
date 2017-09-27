package transformations.ort.enums;



/**
 * Enum with the XML labels used during the first UML model translation
 * towards XML. These are predefined UMLR labels.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public enum XMLLabels {
	ROOT("diagram"),
	
	CLASS("class"),
	CLASS_NAME("name"),
	CLASS_ID("id"),
	CLASS_ABSTRACT("abstract"),
    NODE_POINT_X("coordX"),
    NODE_POINT_Y("coordY"),
	
	ATTRIBUTE("attribute"),
	ATTRIBUTE_ID("id"),
	ATTRIBUTE_NAME("name"),
	ATTRIBUTE_TYPE("type"),
	ATTRIBUTE_ORDERED("ordered"),
	ATTRIBUTE_UNIQUE("unique"),
	
	RELATIONSHIP("relationship"),
	REL_ID("id"),
	REL_NAME("name"),
	REL_TYPE("type"),
	
	REL_START("origin"),
	REL_DEST("destination"),

	REL_EP_NAME("name"),
    REL_EP_CARD("cardinality"),
    REL_EP_CLASS("class"),
    REL_EP_TYPE("type"),
    REL_EP_BROWSABLE("browsable"),
    REL_EP_UNIQUE("unique"),
    REL_EP_ORDERED("ordered"),

	ASSOCIATION_CLASS("associationClass"),
	ASSOC_CLASS_ID("id");


	private String name;





	/**
	 * Default constructor of the class, that initializes
	 * the instance with the values received as parameters.
	 * @param n Name to be stored in the enum-
	 */
	XMLLabels(String n){
		name = n;
	}


	/**
	 * Getter to obtain the name of the enum instance.
	 * @return The name as a string.
	 */
	public String getName(){
		return name;
	}


}
