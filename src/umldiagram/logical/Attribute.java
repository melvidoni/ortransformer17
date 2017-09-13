package umldiagram.logical;


import umldiagram.logical.enums.AttributeType;

/**
 * Class that represents an attribute in the model's logic.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 *
 */
public class Attribute {
	private String name;
	private AttributeType type;
	private int id;
	private boolean ordered;
	private boolean unique;



	/**
	 * Default constructor that initializes an empty attribute.
	 */
	public Attribute(){
		name = "";
		id = -1;
		ordered = false;
		unique = false;
	}



    /**
     * Constructor that creates an attribute with the values received as
     * parameters, stored in the instance.
     * @param n Attribute name.
     * @param t Type of the attribute.
     * @param i Id of the attribute.
     * @param o true if it i ordered, false if it is not.
     * @param u true if it is unique, false if it is not.
     */
	public Attribute(String n, AttributeType t, int i, boolean o, boolean u){
		name = n;
		type = t;
		id = i;
		ordered = o;
		unique = u;
	}

    /**
     * Method that returns the attribute as a string.
     * @return The type and name of the attribute, as a string,
     * following the UML convention.
     */
	public String toString(){
		return (type + " : " + name);
	}


    /**
     * Setter that replaces the existing name with the
     * new one received as a parameter.
     * @param n The new name of the attribute.
     */
	public void setName(String n) {
		name = n;
	}

    /**
     * Getter to obtain the current name of the attribute.
     * @return The name in object format.
     */
	public String getName() {
		return name;
	}


    /**
     * Setter to replace the existing attribute type with the
     * new type received as a parameter.
     * @param t The new type of the attribute
     */
	public void setType(AttributeType t) {
		type = t;
	}


    /**
     * Getter to obtain the current type of the attribute.
     * @return The current type in object format.
     */
	public AttributeType getType() {
		return type;
	}


    /**
     * Setter to replace the current id for a new one.
     * @param i The new id of the attribute.
     */
	public void setId(int i) {
		id = i;
	}


    /**
     * Getter to obtain the current id of the attribute.
     * @return The current id as an integer.
     */
	public int getId() {
		return id;
	}


    /**
     * Method to replace the value of the 'ordered' feature
     * of the attribute, for the one received as parameter.
     * @param isOrdered The new feature value.
     */
	public void setOrdered(boolean isOrdered){
		ordered = isOrdered;
	}


    /**
     * Method that returns if the attribute is ordered or not.
     * @return true if it is ordered, false otherwise.
     */
	public boolean isOrdered(){
		return ordered;
	}


    /**
     * Method that returns if the attribute is unique or not.
     * @return true if it is unique, false otherwise.
     */
	public boolean isUnique(){
		return unique;
	}


    /**
     * Method to replace the value of the 'unique' feature
     * of the attribute, for the one received as parameter.
     * @param isUnique The new feature value.
     */
	public void setUnique(boolean isUnique){
		unique = isUnique;
	}
	

}
