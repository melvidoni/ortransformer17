package transformations.ort.enums;



/**
 * Enum class that represents the available types of convertions towards
 * a database structure, that can be performed using OR-Transformer.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public enum DatabaseType {
	OBJECT_RELATIONAL("Object-Relational (Oracle)"),
	RELATIONAL_MSQL("Relational (MySQL)");
	
	private String type;


	/**
	 * Default constructor of the enum, to assign a type
	 * to the current instance of the class.
	 * @param t The type name as a string.
	 */
	DatabaseType(String t){
		type = t;
	}



	/**
	 * Method that returns the type of the DatabaseType instance.
	 * @return The type name as a String.
	 */
	public String getStringType() {
		return type;
	}



	/**
	 * Method to obtain an instance of the type, based on the
	 * name received as a parameter.
	 * @param t The name used to search for a type.
	 * @return The type, if the name matches, or otherwise null.
	 */
	public static DatabaseType getType(String t){
		if(OBJECT_RELATIONAL.getStringType().equals(t))
			return OBJECT_RELATIONAL;
		else if(RELATIONAL_MSQL.getStringType().equals(t))
			return RELATIONAL_MSQL;
		return null;
	}
	

}
