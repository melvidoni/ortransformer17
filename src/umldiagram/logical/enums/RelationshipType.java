package umldiagram.logical.enums;


/**
 * Enum that represents the different relationship types among
 * the classes, in the UML model.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 *
 */
public enum RelationshipType {
	ASSOCIATION("Association", EndpointType.ASSOCIATION),
	AGGREGATION("Aggregation", EndpointType.AGGREGATION),
	COMPOSITION("Composition", EndpointType.COMPOSITION),
	GENERALIZATION("Generalization", EndpointType.GENERALIZACION);

	private String name;


	private EndpointType enpointType;


    /**
     * Constructor that initializes the enum instance with the
     * values received as parameters.
     * @param n Name of the relationship type.
     * @param ept type of endpoint.
     */
	RelationshipType(String n, EndpointType ept) {
		name = n;
		enpointType = ept;
	}


    /**
     * Method that returns the enum for the relationship type
     * received as a parameter.
     * @param t The name of relationship to compare.
     * @return The corresponding enum (if found), false otherwise.
     */
	public static RelationshipType getRelType(String t){
		if(ASSOCIATION.getName().equals(t))
			return RelationshipType.ASSOCIATION;
		else if(AGGREGATION.getName().equals(t))
			return RelationshipType.AGGREGATION;
		else if(COMPOSITION.getName().equals(t))
			return RelationshipType.COMPOSITION;
		else if(GENERALIZATION.getName().equals(t))
			return RelationshipType.GENERALIZATION;
		return null;
	}




	/**
	 * Method that returns the enum for the relationship type
	 * received as a parameter.
	 * @param t The enpoint of relationship to compare.
	 * @return The corresponding enum (if found), false otherwise.
	 */
	public static RelationshipType getRelEndpointType(EndpointType t){
		if(ASSOCIATION.enpointType.equals(t))
			return RelationshipType.ASSOCIATION;
		else if(AGGREGATION.enpointType.equals(t))
			return RelationshipType.AGGREGATION;
		else if(COMPOSITION.enpointType.equals(t))
			return RelationshipType.COMPOSITION;
		else if(GENERALIZATION.enpointType.equals(t))
			return RelationshipType.GENERALIZATION;
		return null;
	}



	/**
     * Getter to obtain the name of the relationship type.
     * @return The name of the relationship type.
     */
	public String getName() {
		return name;
	}


	/**
	 * Getter to obtain the endopoint type.
	 * @return The endpoint type.
	 */
	public EndpointType getEnpointType() {
		return enpointType;
	}
}
