package transformations.ort.enums;





/**
 * Enum class that contains the implementation types used during
 * the transformation process.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public enum ImplementationType {
	FLAT("Flat Model", "genP"),
	VERTICAL("Vertical Split", "genV"),
	HORIZONTAL("Horizontal Split", "genH");
	
	String name;
	String extension;


	/**
	 * Default constructor of the enum, in order to
	 * initialize it with the values received as parameters.
	 * @param n ImplementationType name.
	 * @param i ImplementationType extension.
	 */
	ImplementationType(String n, String i) {
		name = n;
		extension = i;
	}


	/**
	 * Getter to obtain the name of the enum.
	 * @return The name as a string.
	 */
	public String getName(){
		return name;
	}


	/**
	 * Getter to obtain the implementation instance extention.
	 * @return The extention as a string.
	 */
	public String getExtension() {
		return extension;
	}



	/**
	 * Method to obtain an instance of the ImplementationType, based on the
	 * name received as a parameter.
	 * @param n The name used to search for an implementation.
	 * @return The implementation, if the name matches, or otherwise null.
	 */
	public static ImplementationType getImplementacion(String n){
		if(FLAT.getName().equals(n))
			return FLAT;
		else if(VERTICAL.getName().equals(n))
			return VERTICAL;
		else if(HORIZONTAL.getName().equals(n))
			return HORIZONTAL;
		return null;
	}
}
