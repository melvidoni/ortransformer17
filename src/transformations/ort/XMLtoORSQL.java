package transformations.ort;



import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import transformations.ort.enums.ImplementationType;
import umldiagram.logical.enums.AttributeType;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Class that translates the XML types and tables, from the result
 * of the mapping, towards an object-relational or relational SQL
 * creation sentences.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
class XMLtoORSQL {
	private String path;
	private String number;

	private List<String> typesList;
	private List<String> alterTypesList;
	private HashMap<String, String> vArrangementsList;
	private HashMap<String, String> tabsList;

	private List<String> childrenList;
	private List<String> parentsList;
	private HashMap<String, List<String>> nestedScopesList;
	private HashMap<String, String> ntList;

	String typesScript;


    /**
     * Default constructor of the class, that initializes the
     * instance with the values received as parameters.
     * @param r The working directory.
     * @param n The number of transformation.
     */
	XMLtoORSQL(String r, String n){
		path = r;
		number = n;

		typesScript = "";

		// Initialize the elements
		typesList = new LinkedList<>();
		alterTypesList = new LinkedList<>();
		vArrangementsList = new HashMap<>();
		tabsList = new HashMap<>();
		childrenList = new LinkedList<>();
		parentsList = new LinkedList<>();
		nestedScopesList = new HashMap<>();
		ntList = new HashMap<>();
	}
	
	
	
	
	/**
	 * Method that specifically translates a *.tpo file of types,
     * towards the SQL types in object-relational mode.
	 * @return The SQL scripts as text.
	 */
	String translateORTypes() {
        try {
            // Read the tpo file
            Element tpo = read("-tpo.xml");

            // Prepare the script
            typesScript = "";

            // Get the structured childs
			Node child = tpo.getFirstChild();
			while(child != null) {
                // If this is usable
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("StructuredType")) {

                    // Get the element
                    Element e = (Element) child;

                    // Get the name
                    String typeName = e.getAttribute("uname");

                    // Start the definition
                    String definition = "CREATE OR REPLACE TYPE " + typeName + "_tip";

                    // Check if it has supertype
                    String superType = e.getAttribute("supertype");
                    if (superType.isEmpty()) {
                        // It does not have a parent
                        definition += " AS OBJECT (";
                    } else {
                        // If it has parent...
                        definition += " UNDER " + superType + "_tip (";

                        // Check if the child is not added on the children list
                        if (!childrenList.contains(typeName + "_tip"))
                            childrenList.add(typeName + "_tip");

                        // Check if the parent is not contained on the parent list
                        if (!parentsList.contains(superType + "_tip"))
                            parentsList.add(superType + "_tip");
                    }


                    // Now go through the attributes
                    NodeList attrsList = e.getElementsByTagName("Attribute");
                    boolean isFirst = true;
                    for (int j = 0; j < attrsList.getLength(); j++) {
                        // Get the element
                        Element a = (Element) attrsList.item(j);

                        String attrName = a.getAttribute("uname");

                        // Get the types
                        NodeList typesChilds = a.getElementsByTagName("Type").item(0).getChildNodes();
                        for (int k = 0; k < typesChilds.getLength(); k++) {

                            // If this is usable
                            if (typesChilds.item(k).getNodeType() == Node.ELEMENT_NODE) {

                                // Get the element
                                Element t = (Element) typesChilds.item(k);

                                // Predefined attributes
								switch (t.getNodeName()) {
									case "PredefinedType":
										// If it is not null...
										if (t.getAttributeNode("type") != null) {
											//...find it among the enums
											AttributeType ta = AttributeType.getAttribute(t.getAttribute("type"));

											// Define the type
											String attrDef = (ta != null) ? ta.getOracleName() : "";

											// If not, it is date
											if (ta != null && ta.getLength() > 0) {
												attrDef = attrDef + "(" + ta.getLength() + ")";
											}

											// Add the attribute to the definition
											if (isFirst) isFirst = false;
											else definition += ",";

											// Complete the definition
											definition += "\r\n" + attrName + " " + attrDef;
										}
										break;
									// Reference attributes
									case "ReferenceType": {
										// Call the translation method
										String attr = translateRefAttr(attrName, t, typeName, isFirst);
										definition += attr;

										break;
									}
									// Arrangement attributes
									case "Arrangement": {
										// Call the translation method
										String attr = translateArrangementAttr(attrName, t, typeName, isFirst);
										definition += attr;

										break;
									}
									// Multiset attributes
									case "Multiset": {
										// Call the translation method
										String attr = translateMultisetAttr(attrName, t, typeName, isFirst);
										definition += attr;
										break;
									}
								}
                            }
                        }

                    }
                    // CLOSE THE DEFINITION
                    definition += "\r\n) NOT FINAL;";

                    // Append the remaining information
                    typesScript += definition + "\r\n\r\n";

                    // Add the type as translated
                    typesList.add(typeName + "_tip");
                }

                child = child.getNextSibling();

            }

            // Add the arrangemenets
            for (String key : vArrangementsList.keySet()) {
                typesScript += vArrangementsList.get(key);
            }

            //Agregamos los multiset
            for (String key : tabsList.keySet()) {
                typesScript += tabsList.get(key);
            }

            // Add type modifications
            for (String alter : alterTypesList) {
                typesScript += alter;
            }

            return typesScript;
        }
        catch (Exception e) {
            // TODO FIX EXCEPTION
            e.printStackTrace();
        }

        return "";
	}




	
	/**
	 * Method that translates a multiset attribute structure from XML
     * towards the corresponding object-relational script.
	 * @param attrName Name of the attribute to be translated.
	 * @param element Corresponding XML structure.
	 * @param typeName type name being modified.
	 * @param isFirst true if this is the first attribute of a class,
     *                false if it is not.
	 * @return String with the corresponding SQL script.
	 */
	private String translateMultisetAttr(String attrName, Element element, String typeName, boolean isFirst) {
		// Prepare the string for the script
		String attrScript = "";
		
		// Find the substructures
		Element e = (Element) element.getElementsByTagName("StructuredType").item(0);

		// If the name is not null
		if(e.getAttributeNode("uname") != null){
			String attrType = e.getAttribute("uname");
			
			// If the type has already been translated
			if(typesList.contains(attrType + "_tip")){
				// If the multiset has not been translated...
				if(!tabsList.containsKey(attrType + "_tab")){
					// Create the definition
					String tab = "CREATE TYPE " + attrType + "_tab AS TABLE OF "
						+ attrType + "_tip;\r\n\r\n";
					
					// And add it to the map
					tabsList.put(attrType + "_tab", tab);
				}
				// In any case, define the attribute
				if(!isFirst) attrScript += ",";
				else isFirst = false;

                attrScript += "\r\n" + attrName + "_mult " + attrType + "_tab";
			}
			// If it has not been processes
			else{
				// If is not added...
				if(!tabsList.containsKey(attrType + "_tab")){
					// Generate the tab creation
					String tab = "CREATE TYPE " + attrType + "_tab AS TABLE OF "
							+ attrType + "_tip;\r\n\r\n";
					
					// And add it to the list
					tabsList.put(attrType + "_tab", tab);
				}
				// Create the type alteration
				String alter = "ALTER TYPE " + typeName + "_tip ADD ATTRIBUTE ("
						+ attrName + "_mult " + attrType + "_tab) CASCADE;\r\n";
				
				// And add it to the corresponding list
				alterTypesList.add(alter);
			}
		}
		
		// And regardless of the name...
		Element tr = (Element) element.getElementsByTagName("ReferenceType").item(0);
		NodeList trList = tr.getElementsByTagName("StructuredType");

        // Go through the structures
        for(int m=0; m<trList.getLength(); m++) {
            // Get the element
            Element tetr = (Element) trList.item(m);

            // If the name is not null
            if(tetr.getAttributeNode("uname") != null) {
                String attrType = tetr.getAttribute("uname");

                // If it has already been added...
                if (typesList.contains(attrType + "_tip")) {
                    // ..but not as a tab
                    if (!tabsList.containsKey(attrType + "_tab")) {
                        // Create the type
                        String tab = "CREATE TYPE " + attrType + "_tab AS TABLE OF REF "
                                + attrType + "_tip;\r\n\r\n";

                        // And add it to the list
                        tabsList.put(attrType + "_tip", tab);
                    }
                    // Add the attribute
                    if (!isFirst) attrScript += ",";
                    else  isFirst = false;
                    attrScript += "\r\n" + attrName + "_mult " + attrType + "_tab";
                }
                // If it has not been added
                else {
                    // But not as a tab
                    if (!tabsList.containsKey(attrType + "_tab")) {
                        // Generate the definition
                        String tab = "CREATE TYPE " + attrType + "_tab AS TABLE OF REF "
                                + attrType + "_tip;\r\n\r\n";

                        // And add it to the list
                        tabsList.put(attrType + "_tab", tab);
                    }
                    // Create the alteration, regardless
                    String alter = "ALTER TYPE " + typeName + "_tip ADD ATTRIBUTE ("
                            + attrName + "_mult " + attrType + "_tab) CASCADE;\r\n\r\n";

                    // And add it to the list
                    alterTypesList.add(alter);
                }

				/*
				 * Add the nested tables
				 */
                String nested = "NESTED TABLE " + attrName + "_mult STORE AS "
                        + attrType + "_nt";
                // If the key is already on the list
                if (nestedScopesList.containsKey(typeName + "_t")) {
                    nestedScopesList.get(typeName + "_t").add(nested);
                }
                else {
                    List<String> list = new ArrayList<>();
                    list.add(nested);
                    nestedScopesList.put(typeName + "_t", list);
                }
				
				/*
				 * And the remaining scopes
				 */
                // Generate the definition
                String alter = "ALTER TABLE " + attrType
                        + "_nt ADD (SCOPE FOR (column_value) IS "
                        + attrType + "_t);\r\n\r\n";
                // And add it to the list
                ntList.put(attrType + "_nt", alter);
            }
        }
		
		// Return the script
		return attrScript;
	}




	/**
	 * Method that translates an arrangement attribute on the OR transformation.
	 * @param attrName Name of the chosen attribute.
	 * @param element Corresponding XML structure.
	 * @param typeName Type name being modified.
	 * @param isFirst true if this is the first attribute of a class,
     *                false if it is not.
	 * @return String with the corresponding SQL script.
	 */
	private String translateArrangementAttr(String attrName, Element element, String typeName, boolean isFirst) {
		// Prepare the data
		Element e = (Element) element.getElementsByTagName("StructuredType").item(0);
		String maxNumber = element.getAttribute("max_element_number");
		
		// And then the attribute
		String attr = "";
		
		// If the name is not null
		if(e.getAttributeNode("uname") != null){
			String attrType = e.getAttribute("uname");
			
			// If the type has already been translated...
			if(typesList.contains(attrType + "_tip")){
				// If the arrangement hasn't been translated
				if(!vArrangementsList.containsKey(attrType + "_va")){
					// Create the definition
					String arrangement = "CREATE TYPE " + attrType + "_va AS VARRAY("
								+ maxNumber + ") OF " + attrType + "_tip;\r\n\r\n";
					// And add it to the list
					vArrangementsList.put(attrType + "_va", arrangement);
				}
				// Create the attribute definition
				if(!isFirst) attr += ",";
				else isFirst = false;

				attr += "\r\n" + attrName + "_arr " + attrType + "_va";

			}
			// If the attribute has not been added...
			else{
				// ...and also not declared
				if(!vArrangementsList.containsKey(attrType + "_va")){
					// Create the definition
					String arrangement = "CREATE TYPE " + attrType + "_va AS VARRAY ("
								+ maxNumber + ") OF " + attrType + "_tip;\r\n\r\n";
					// Add the definition to the list
					vArrangementsList.put(attrType + "_va", arrangement);
				}
				// And create the modification
				String alterType = "ALTER TYPE " + typeName + "_tip ADD ATRIBUTTE ("
							+ attrName + "_arr " + attrType + "_va) CASCADE;\r\n\r\n";
				
				//Y la sumamos a la lista
				alterTypesList.add(alterType);
			}
		}
		
		
		// Now get the element reference
		Element tr = (Element) element.getElementsByTagName("ReferenceType").item(0);
		NodeList trList = tr.getElementsByTagName("StructuredType");

        // Go through the child nodes
        for(int l=0; l<trList.getLength(); l++) {
            // Get the element
            Element trte = (Element) trList.item(l);

            // If it has a name, it is not empty
            if (trte.getAttributeNode("uname") != null) {
                String attrType = trte.getAttribute("uname");

                // If the type has already been translated
                if (typesList.contains(attrType + "_tip")) {
                    // ...but the type has not been created
                    if (!vArrangementsList.containsKey(attrType + "_va")) {
                        // Generate the creation
                        String arrDefinition = "CREATE TYPE " + attrType + "_va AS VARRAY ("
                                + maxNumber + ") OF REF " + attrType + "_tip;\r\n\r\n";

                        // Add it to the list
                        vArrangementsList.put(attrType + "_va", arrDefinition);
                    }

                    // In both cases, prepare the definition
                    if (!isFirst) attr += ",";
                    else isFirst = false;

                    attr += "\r\n" + attrName + "_arr " + attrType + "_va";
                }
                // If it has not been added
                else {
                    // If it hasn't been created
                    if (!vArrangementsList.containsKey(attrType + "_va")) {
                        // Generate the definition
                        String arr = "CREATE TYPE " + attrType + "_va AS VARRAY ("
                                + maxNumber + ") OF REF " + attrType + "_tip;\r\n\r\n";

                        // And add it to the list
                        vArrangementsList.put(attrType + "_tip", arr);
                    }
                    // Prepare the alter and add it
                    String alterType = "ALTER TYPE " + typeName + "_tip ADD ATTRIBUTE ("
                            + attrName + "_arr " + attrType + "_va) CASCADE;\r\n\r\n";
                    alterTypesList.add(alterType);
                }
            }
        }
		
        // Return the script
		return attr;
	}




	/**
	 * Method that translates only an attribute that is a reference type,
     * during the type transformation.
	 * @param attrName The name of the chosen attribute.
	 * @param element Corresponding XML structure.
	 * @param typeName Type name being modified.
	 * @param isFirst true if this is the first attribute of a class,
     *                false if it is not.
     * @return String with the corresponding SQL script.
	 */
	private String translateRefAttr(String attrName, Element element, String typeName, boolean isFirst) {
		Element te = (Element) element.getElementsByTagName("StructuredType").item(0);
		
		// Declare the type
		String attr = "";

		// If the name is not null
		if(te.getAttributeNode("uname") != null){
			// If the type has already been added
			if(typesList.contains(te.getAttribute("uname") + "_tip")){
				if(!isFirst) attr += ",";
				else isFirst = false;

				attr += "\r\n" + attrName + "_ref REF " + te.getAttribute("uname") + "_tip";
			}
			// If the type has not been processed
			else{
				// Create the modification
				String alterType = "ALTER TYPE " + typeName + "_tip ADD ATTRIBUTE ("
							+ attrName + "_ref REF " + te.getAttribute("uname")
							+ "_tip) CASCADE;\r\n\r\n";
				
				// Add it to the list
				alterTypesList.add(alterType);
			}
			
			/*
			 * PREPARE THE TABLES SCOPES
			 */
			// Create the scope definition
			String scope = "ADD (SCOPE FOR (" + attrName
				+ "_ref) IS " + te.getAttribute("uname") +"_t)";

			// If it is not contained...
			if(nestedScopesList.containsKey(typeName + "_t")){
				nestedScopesList.get(typeName + "_t").add(scope);
			}
			else {
                LinkedList<String> list = new LinkedList<>();
				list.add(scope);
				nestedScopesList.put(typeName + "_t", list);
			}
		}
						
		// Return the attribute
		return attr;
	}


    /**
     * Method that manages the transformation into tables, depending on the
     * type of implementation. Vertical and Flat remain straightforward.
     * @param imp Implementation type to work with.
     * @return The type of scripts to be used.
     */
	String manageTablesTranslation(ImplementationType imp) {
		String sql = "";

		// If this is not horizontal, go ahead
		if(!imp.getExtension().equals(ImplementationType.HORIZONTAL.getExtension())) {
			sql = translateORTables(imp, "");
		}
		// Otherwise, let's check
		else {
			// Get both transformations
			String vertical = translateORTables(ImplementationType.VERTICAL, "-V");

			String[] tablesGenV = vertical.split("\r\n\r\n");
			String[] tablesGenH = translateORTables(imp, "-H").split("\r\n\r\n");

			// Create an empty list for the parents
			LinkedList<String> parents = new LinkedList<>();

			// Add everything from horizontal
			for(String tgv : tablesGenV) {
				// Get the name
				String tname = tgv.split("\r\n")[0].split(" ")[2];

				// If the name is not a parent or a children
				if(!parentsList.contains(tname + "ip") && !childrenList.contains(tname + "ip")) {
					// Add it to the list
					if(!sql.equals("")) sql += "\r\n\r\n";
					sql += tgv;
				}
				// Otherwise, add it to the parent list
				else if(parentsList.contains(tname + "ip")) parents.add(tgv);
			}

			// Now split the types
            List<String> splitTypes = Arrays.asList(typesScript.split("\r\n\r\n"));

			// For each horizontal table
            for(String tgh : tablesGenH) {
                // Get the name
                String t2name = tgh.split("\r\n")[0].split(" ")[2];
                String parentName = "";

                // Get the parent
                for(String st: splitTypes) {
                    if(st.contains(t2name + "ip UNDER" )) {
                        parentName = st.split("\r\n")[0].split(" UNDER ")[1].replace(" (", "");
                        break;
                    }
                }

                // Get the new table
                String newChildTable = "";

                // If we have a parent
                if(!parentName.equals("")) {
                    // Split the children
                    String[] splitChildren = tgh.split("\r\n");

                    // Get parent attributes
                    LinkedList<String> parentTable = new LinkedList<>();

                    for(String p : parents) {
                        // Split at get the name
                        String[] psplit = p.split("\r\n");
                        String pname = psplit[0].split(" ")[2];
                        //pname = pname.substring(0, pname.length() - 2);

                        // Check if this is the one
                        if(parentName.startsWith(pname)) {
                            // Add the origin
                            newChildTable = splitChildren[0] + "\r\n";

                            // Loop and add
                            for(int k = 1; k < psplit.length; k++) {
                                if(!psplit[k].startsWith("NOT "))
                                    newChildTable += psplit[k] + "\r\n";
                            }

                            // Add the remaining bits
                            newChildTable += String.join("\r\n",
                                    Arrays.copyOfRange(splitChildren, 1, splitChildren.length));

                            // Add to the sql
                            sql = newChildTable + "\r\n\r\n" + sql;
                            break;
                        }
                    }
                }


            }

		}







		// Return the result
		return sql;
	}






	/**
	 * Method that effectively translates the last *.tbl file from the
     * OR modelling, towards the SQL script.
	 * @param imp Type of implementation.
	 * @return String with the SQL script
	 */
	private String translateORTables(ImplementationType imp, String additional) {
		// Read the file
		Element tbl = read(additional + "-tbl.xml");

        // Set the script
        String tableScripts = "";

        // Go through each element
        NodeList nodeList = tbl.getElementsByTagName("TypedTable");
        for(int i=0; i<nodeList.getLength(); i++) {

            // If this is usable
            if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                // Get the element
                Element tt = (Element) nodeList.item(i);

                // Prepare the definition
                String definition = "";

                if (tt.getAttributeNode("uname") != null) {
                    String name = tt.getAttribute("uname");

                    // Mark the header
                    definition = "CREATE TABLE " + name + "_t OF " + name + "_tip";

                    // Evaluate hierarchies
                    if (childrenList.contains(name + "_tip"))
                        definition = definition + "\r\nNOT SUBSTITUTABLE AT ALL LEVELS";
                    else if (parentsList.contains(name + "_tip") && !imp.equals(ImplementationType.FLAT))
                        definition = definition + "\r\nNOT SUBSTITUTABLE AT ALL LEVELS";

                    // Check the scopes
                    if (nestedScopesList.containsKey(name + "_t")) {
                        //Recorremos de a uno y vamos agregando
                        for(String s : nestedScopesList.get(name + "_t")) {
                            definition += "\r\n" + s;
                        }
                    }

                    // Add the closure
                    definition += ";\r\n\r\n";
                }

                tableScripts += definition;
            }
        }


        // Add the alterations to nested tables
        if(!imp.equals(ImplementationType.HORIZONTAL)) {
            for (String key : ntList.keySet()) {
                tableScripts += ntList.get(key);
            }
        }

		return tableScripts;
	}
















	
	/**
	 * Method that reads the corresponding file received as a parameter
     * and the specified number, with the adequate extension.
	 * @param extension Final chars for the file.
	 * @return Loaded and completed XML file.
	 */
	private Element read(String extension) {
		try {
			// Read the document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new File(path
                    + File.separator + number + extension) );

			// Return the root
			return (Element) document.getElementsByTagName("SQLSchema").item(0);
		}
		catch (Exception e) {
			// TODO EVALUATE THIS CATCH
			e.printStackTrace();
		}

		return null;
	}






    String getTypesScript() {
	    return typesScript;
    }
}
