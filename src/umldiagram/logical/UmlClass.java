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





}
