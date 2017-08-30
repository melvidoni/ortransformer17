package umldiagram.logical;


/**
 * Class that represents an association class in a relationship between two classes.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class AssociationClass {
	private String id;
	private UmlClass logicalClass;
	private Relationship relationship;


    /**
     * Default constructor that initializes an empty instance.
     */
	public AssociationClass(){
		id = "";
		logicalClass = new UmlClass();
		relationship = new Relationship("");
	}


    /**
     * Getter to obtain the umldiagram.logical class of the association.
     * @return the class in object format.
     */
	public UmlClass getUmlClass() {
		return logicalClass;
	}


    /**
     * Setter to change the existing association class (umldiagram.logical),
     * for the one received as a parameter.
     * @param c The new umldiagram.logical class.
     */
	public void setClass(UmlClass c) {
		logicalClass = c;
	}


    /**
     * Getter to obtain the umldiagram.logical relationship.
     * @return The relationship in object format.
     */
	public Relationship getRelationship() {
		return relationship;
	}


    /**
     * Setter to replace the current relationship with the
     * one received as a parameter.
     * @param r The new relationship.
     */
	public void setRelationship(Relationship r) {
		relationship = r;
	}


    /**
     * Getter to obtain the id of the current class.
     * @return The id as a string.
     */
	public String getId() {
		return id;
	}


    /**
     * Setter to replace the current id for a new one.
     * @param i The id of the current class.
     */
	public void setId(String i) {
		id = i;
	}


    /**
     * Method that returns the class converted into a string.
     * @return Converts the umldiagram.logical class to string, and returns it.
     */
	public String toString(){
		return logicalClass.toString();
	}

}