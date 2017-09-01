package umldiagram.logical;


import umldiagram.logical.enums.EndpointType;
import umldiagram.logical.enums.RelationshipType;

/**
 * Class that represents an UML relationship among classes.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class Relationship {
	private String id;
	private String name;
	private RelationshipEndpoint origin;
	private RelationshipEndpoint end;
	private RelationshipType type;
	private boolean isAssocClass;


    /**
     * Constructor of the class, that sets the id
     * of the relationship with the one received as parameter.
     * @param idR The new id of the class.
     */
	public Relationship(String idR){
		id = idR;
        isAssocClass = false;
	}



    /**
     * Constructor of the class that creates a relationship with all the obtained information.
     * @param id Numeric id of the relationship.
     * @param n Name of the relationship.
     * @param o Originating endpoint.
     * @param e Destination endpoint.
     * @param t Type of the relationship.
     */
    public Relationship(int id, String n, RelationshipEndpoint o, RelationshipEndpoint e, RelationshipType t) {
	    this(String.valueOf(id));
	    name = n;
	    origin = o;
	    end = e;
	    type = t;
    }






    /**
     * Method that transforms the relationship to a string,
     * following the UML name convention.
     * @return The relationship as a string.
     */
	public String toString() {
		if(type.equals(RelationshipType.GENERALIZATION))
			return ("Specialization of: " + end.getClassOf().getName());
		else if(isAssocClass)
			return (origin.getClassOf().getName() + " related with: " + end.getClassOf().getName());
		else
			return ("(" + name + ") " + type.getName() + " with:" + end.getClassOf().getName());
	}



    /**
     * Getter to obtain the current ID of the relationship.
     * @return The id in string format.
     */
	public String getId() {
		return id;
	}


    /**
     * Setter to replace the current ID with the one
     * received as a parameter.
     * @param i The new id of the class.
     */
	public void setId(String i) {
		id = i;
	}


    /**
     * Getter to obtain the origin endpoint of the relationship.
     * @return The origin end as an object.
     */
	public RelationshipEndpoint getOrigin() {
		return origin;
	}


    /**
     * Method to replace the current origin end, with a new one
     * obtained as a parameter.
     * @param o The new origin of the relationship.
     */
	public void setOrigin(RelationshipEndpoint o) {
		origin = o;
	}


    /**
     * Getter to obtain the ending endpoint of the relationship.
     * @return The current ending endpoint as an object.
     */
	public RelationshipEndpoint getEnd() {
		return end;
	}


    /**
     * Method to replace the current ending end, with a new one
     * obtained as a parameter.
     * @param e The new ending of the relationship.
     */
	public void setEnd(RelationshipEndpoint e) {
		end = e;
	}


    /**
     * Getter to obtain the name of the relationship.
     * @return The name of the relationship.
     */
	public String getName() {
		return name;
	}


    /**
     * Setter to replace the current name of the relationship,
     * with the one received as a parameter.
     * @param n The new relationship name.
     */
	public void setName(String n) {
		name = n;
	}


    /**
     * Getter to obtain the relationship type.
     * @return The type as an enum instance.
     */
	public RelationshipType getType() {
		return type;
	}


    /**
     * Setter to replace the type of the relationship
     * for the one received as a parameter.
     * @param rt The type of relationship.
     */
	public void setType(RelationshipType rt) {
		type = rt;
	}




	/**
	 * Methods that adds parameters to this relationship, according to the
     * umldiagram.logical shape of a generalization.
	 * @param oClass Child class of the relationship.
	 * @param eClass Parent class of the relationship.
	 */
	public void newGeneralization(UmlClass oClass, UmlClass eClass) {
	    // TODO CHANGE NAMES ON THIS PART
		name = oClass.getName() + "Especializa" + eClass.getName();
		type = RelationshipType.GENERALIZATION;
		
		// Set the origin class
		origin = new RelationshipEndpoint( (oClass.getName() + "Hijo" ), true, false,
                false, "0..*", oClass, RelationshipType.ASSOCIATION );

		// Set the ending class
        end = new RelationshipEndpoint( (eClass.getName() + "Padre"), false, false,
                false, "0..1", eClass, RelationshipType.GENERALIZATION
        );
	}




    /**
     * Setter to change if the relationship is an association class or not.
     * @param isac The new value for this feature.
     */
	public void setAssociationClass(boolean isac) {
		isAssocClass = isac;
	}


	

}