package umldiagram.logical;

import java.util.LinkedList;
import java.util.List;


/**
 * Representation of an UML umldiagram.logical class, in the umldiagram.logical model of the system.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class UmlClass {
	private String id;
	private String name;
	private List<Attribute> attrList;
	private boolean isAbstract;


    /**
     * Default constructor that initializes the instance
     * with empty default values.
     */
	public UmlClass(){
		id = "";
		name = "";
		attrList = new LinkedList<>();
		isAbstract = false;
	}


    /**
     * Constructor of the class that initializes the instance
     * with the values received as parameters, and the attribute
     * list as an empty list.
     * @param i Id of the logial class.
     * @param n Name of the class.
     * @param a true if it is abstract, false otherwise.
     */
	public UmlClass(String i, String n, boolean a){
		id = i;
		name = n;
		isAbstract = a;
		attrList = new LinkedList<>();
	}


    /**
     * Method that checks for the longest attribute on the class.
     * If there are none, it returns the class name.
     * @return The longest attribute as a string, or the class name.
     */
    public String getLongestAttribute() {
        // If there are no attributes return the name
        if(attrList.isEmpty()) return name;

        // Otherwise, check the attributes
        else {
            String longestAttr = "";
            for(Attribute a : attrList) {
                if(a.toString().length() >= longestAttr.length()) longestAttr = a.toString();
            }

            // Return a value
            return longestAttr;
        }
    }











    /**
     * Method to convert the class to string format.
     * @return The name of the class.
     */
	public String toString(){
		return name;
	}


    /**
     * Setter that replaces the ID of the class for
     * the one received as parameter.
     * @param i The new id for the class.
     */
	public void setId(String i) {
		id = i;
	}


    /**
     * Getter to obtain the current id of the class.
     * @return The current ID of the class, as a string.
     */
	public String getId() {
		return id;
	}


    /**
     * Setter that replaces the current name of the class
     * for the new one received as a parameter.
     * @param n The new name for the class.
     */
	public void setName(String n) {
		name = n;
	}


    /**
     * Getter to obtain the current name of the class.
     * @return The name of the class.
     */
	public String getName() {
		return name;
	}


    /**
     * Setter that replaces the feature 'abstract' of the class,
     * for the value received as a parameter.
     * @param a The new value of the feature.
     */
	public void setAbstract(boolean a) {
		isAbstract = a;
	}


    /**
     * Getter to obtain if the class is abstract or not.
     * @return true if it is abstract, false otherwise.
     */
	public boolean isAbstract() {
		return isAbstract;
	}


    /**
     * Method that adds a new attribute to the list, but keeps
     * all the existing attributes as well.
     * @param at The new attribute to be added.
     */
	public void addAttribute(Attribute at) {
		attrList.add(at);
	}


    /**
     * Getter to obtain the current list of attributes.
     * @return The list of attributes in object format.
     */
	public List<Attribute> getAttributes() {
		return attrList;
	}


    /**
     * Method that replaces the list of attributes for the
     * one that is received as a parameter.
     * @param l The new list of attributes.
     */
	public void setList(List<Attribute> l){
		attrList = l;
	}











	/**
	 * Method that returns the attributes in a list of object arrays, to be
     * used on the tables in the modifying windows.
	 * @return Attributes list as a list of object arrays.
	 */
	public List<Object[]> tablefyAttributes(){
		List<Object[]> rowsList = new LinkedList<>();

        for(Attribute a : attrList) {
            Object[] row = new Object[4];
            // Add the data
            row[0] = a.getName();
            row[1] = a.getType();
            row[2] = a.isOrdered();
            row[3] = a.isUnique();
            // Add the row to the list
            rowsList.add(row);
        }
		
		return rowsList;
	}



}
