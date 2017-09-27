package umldiagram.logical;


import umldiagram.logical.enums.EndpointType;
import umldiagram.logical.enums.RelationshipType;



/**
 * Class that represents and endpoint in a UML relationship
 * between two classes.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 *
 */
public class RelationshipEndpoint {
	private String name;
	private boolean browsable;
	private boolean unique;
	private boolean ordered;
	private String cardinality;
	private EndpointType type;
	private UmlClass lclass;




    /**
     * Constructor to create a new relationship endpoint using the
     * information received as parameters.
     * @param n The role name.
     * @param b If it is browsable.
     * @param u If it is unique.
     * @param o If it is ordered.
     * @param c The endpoint cardinality.
     * @param lc The related class.
     * @param rType The type of relationship endpoint.
     */
	public RelationshipEndpoint(String n, boolean b, boolean u, boolean o, String c, UmlClass lc,
							RelationshipType rType) {
		// Set the direct values
		name = n;
		browsable = b;
		unique = u;
		ordered = o;
		cardinality = c;
		lclass = lc;

		// Now the type
		switch(rType) {
			case GENERALIZATION: type = EndpointType.GENERALIZACION;
								 break;

			case ASSOCIATION: type = EndpointType.ASSOCIATION;
							  break;

			case AGGREGATION: type = EndpointType.AGGREGATION;
							  break;

			case COMPOSITION: type = EndpointType.COMPOSITION;
							  break;
		}
	}










    /**
     * Method that converts the endpoint into a string,
     * following a established naming convention.
     * @return The endpoint as a string.
     */
	public String toString() {
		return this.lclass + ": " + this.name + " [" + this.cardinality + "]";
	}


    /**
     * Getter to obtain the name of the endpoint.
     * @return The endpoint name.
     */
	public String getName() {
		return name;
	}


    /**
     * Method that replaces the current name of the
     * endpoint, for the one received as a parameter.
     * @param n The new name of the endpoint.
     */
	public void setName(String n) {
		name = n;
	}


    /**
     * Getter to obtain the type of the endpoint.
     * @return The type as an object.
     */
	public EndpointType getType() {
		return type;
	}


    /**
     * Setter to replace the current endpoint type
     * with the one received as a parameter
     * @param t New type of the endpoint.
     */
	public void setType(EndpointType t) {
		type = t;
	}


    /**
     * Getter to obtain the class this endpoint is linked to.
     * @return The class in umldiagram.logical object format.
     */
	public UmlClass getClassOf() {
		return lclass;
	}


    /**
     * Method to replace the class the endpoint is related to,
     * with the one received as a parameter.
     * @param c The new class to be linked to.
     */
	public void setClass(UmlClass c) {
		lclass = c;
	}


    /**
     * Getter to obtain the cardinality of the endpoint.
     * @return The cardinality as a string.
     */
	public String getCardinality() {
		return cardinality;
	}


    /**
     * Setter to replace the cardinality with the one
     * received as a parameter.
     * @param c The new cardinality for the endpoint.
     */
	public void setCardinality(String c) {
		cardinality = c;
	}


    /**
     * Getter to obtain if the endpoint is browsable or not.
     * @return true if it is browsable, false otherwise.
     */
	public boolean isBrowsable() {
		return browsable;
	}


    /**
     * Setter to replace the browsable feature of the endpoint.
     * @param b The new value for the feature.
     */
	public void setBrowsable(boolean b) {
		browsable = b;
	}


    /**
     * Getter to obtain if the endpoint is unique or not,
     * @return true if it is unique, false otherwise.
     */
	public boolean isUnique() {
		return unique;
	}


    /**
     * Setter to replace the unique feature of the endpoint.
     * @param u The new value for the feature.
     */
	public void setUnique(boolean u) {
		unique = u;
	}


    /**
     * Getter to obtain if the endpoint is ordered or not,
     * @return true if it is ordered, false otherwise.
     */
	public boolean isOrdered() {
		return ordered;
	}


    /**
     * Setter to replace the ordered feature of the endpoint.
     * @param o The new value for the feature.
     */
	public void setOrdered(boolean o) {
		ordered = o;
	}
}
