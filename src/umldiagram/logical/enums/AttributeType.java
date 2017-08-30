package umldiagram.logical.enums;

/**
 * Enum that controls the possible attribute types, and their nomenclature.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 *
 */
public enum AttributeType {
	CHARACTER("Character", "character", "VARCHAR2", 256), 
	INTEGER("Integer", "integer", "NUMBER", 15), 
	DATE("Date", "date", "DATE", 0);

	String name;
	String type;
	String oracleName;
	int length;


	/**
	 * Default constructor of the enum, that initializes the instance
	 * with the values received as a parameter.
	 * @param n New attribute name.
	 * @param t New attribute type.
	 * @param no New attribute Oracle name.
	 * @param l New attribute length.
	 */
	AttributeType(String n, String t, String no, int l) {
		name = n;
		type = t;
		oracleName = no;
		length = l;
	}


	/**
	 * Getter to obtain the AttributeType that matches the
	 * name that was received as a parameter.
	 * @param n The name to check for a type.
	 * @return The type if found, null otherwise.
	 */
	public static AttributeType getAttributeName(String n) {
		if(CHARACTER.getName().equals(n))
			return AttributeType.CHARACTER;
		else if(INTEGER.getName().equals(n))
			return INTEGER;
		else if(DATE.getName().equals(n))
			return DATE;
		return null;		
	}


	/**
	 * Getter to obtain the name of an attribute.
	 * @return The attribute name as a string.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Getter to obtain the attribute's type.
	 * @return The type as a string.
	 */
	public String getType() {
		return type;
	}


	/**
	 * Method to convert the current instance in a string.
	 * @return Returns the element as a string. In this case, it
	 * simply returns the name of the instance.
	 */
	public String toString() {
		return name;
	}


	/**
	 * Getter to obtain the type name used in Oracle format.
	 * @return The type name used in Oracle databases.
	 */
	public String getOracleName() {
		return oracleName;
	}


	/**
	 * Getter to obtain the length of the attribute.
	 * @return The length of the attribute as an integer,
	 */
	public int getLength() {
		return length;
	}

    

    /**
     * Method to obtain an instance of the type, based on the
     * name received as a parameter.
     * @param t The name used to search for a type.
     * @return The type, if the name matches, or otherwise null.
     */
	public static AttributeType getAtributo(String t) {
		if(CHARACTER.getType().equals(t))
			return AttributeType.CHARACTER;
		else if(INTEGER.getType().equals(t))
			return INTEGER;
		else if(DATE.getType().equals(t))
			return DATE;
		return null;	
	}
}
