package umldiagram.logical.enums;


/**
 * Enum that represents the different relationship types among
 * the classes, in the UML model.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 *
 */
public enum RelationshipType {
	// TODO CHANGE THESE NAMES
	ASSOCIATION("Asociacion", EndpointType.ASSOCIATION),
	AGGREGATION("Agregacion", EndpointType.AGGREGATION),
	COMPOSITION("Composicion", EndpointType.COMPOSITION),
	GENERALIZATION("Generalizacion", EndpointType.GENERALIZACION);

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
     * @param t The type of relationship to compare.
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
     * Getter to obtain the name of the relationship type.
     * @return The name of the relationship type.
     */
	public String getName() {
		return name;
	}


    /**
     * Getter to obtain the type of endpoint in the relationship.
     * @return The type of endpoint in object format.
     */
	public EndpointType getEndpointType() {
		return enpointType;
	}
}
