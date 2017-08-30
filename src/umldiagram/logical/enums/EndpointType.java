package umldiagram.logical.enums;


/**
 * Enum that represents the endpoints of the relationships.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public enum EndpointType {
    // TODO CHANGE THESE NAMES
	ASSOCIATION("ninguno"),
	GENERALIZACION("generalizacion"),
	AGGREGATION("agregado"),
	COMPOSITION("compuesto");

	String name;

    /**
     * Default constructor of the enum, that records
     * the name received as a parameter.
     * @param n The name of the endpoint type.
     */
	EndpointType(String n) {
		name = n;
	}


    /**
     * Getter to obtain the current name value.
     * @return The current name value.
     */
	public String getName() {
		return name;
	}


    /**
     * Method that returns the endpoint type, for the type
     * that was received as a parameter.
     * @param tipo The type received to be compared.
     * @return The endpoint element in object format.
     */
	public static EndpointType getEndpointType(String tipo) {
		if(ASSOCIATION.getName().equals(tipo))
			return EndpointType.ASSOCIATION;
		else if(GENERALIZACION.getName().equals(tipo))
			return EndpointType.GENERALIZACION;
		else if(AGGREGATION.getName().equals(tipo))
			return EndpointType.AGGREGATION;
		else if(COMPOSITION.getName().equals(tipo))
			return EndpointType.COMPOSITION;
		return null;
	}
}
