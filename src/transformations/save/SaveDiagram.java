package transformations.save;



import umldiagram.logical.*;
import umldiagram.graphical.Node;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;








/**
 * Class that saves the UML diagram in a special format of the system,
 * with a *.ort file extension.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class SaveDiagram {



	/**
	 * Main method to perform the file saving using the exclusive ORT format.
     * @param targetFile The target file where information is going to be stored.
     * @param nodes List of graphical nodes from the drawing diagram.
     * @throws IOException Related to issues with the file.
     */
	public static void export(File targetFile, LinkedList<Node> nodes) throws IOException {
	    // Get the diagram
        UMLDiagram diagram = UMLDiagram.getInstance(false);

	    // Initialize the elements
		FileWriter writer = new FileWriter(targetFile);
		PrintWriter printWriter = new PrintWriter(writer);

        // Save the classes
        for(UmlClass c: diagram.getClasses()) {
            // Get the corresponding node
            for(Node n: nodes) {
                // If this is the correct one
                if(n.getName().equals(c.getName())) {
                    // Save the class information
                    saveClass(printWriter, c, n);

                     // Break the inner loop
                    break;
                }
            }
        }
        // Write an empty line
        printWriter.println("\n");



        // Save relationships
        for(Relationship r: diagram.getRelationships()) {
            // Save the relationship
            saveRelationship(printWriter, r);
        }
        // Add a blank line
        printWriter.println("\n");


        // Save association classes
        for(AssociationClass ca : diagram.getAssociationClasses()) {
            // Save the assoc class data
            printWriter.println(SaveLabels.ASSOC_CLASS.getName());
            printWriter.println(SaveLabels.ASSOC_CLASS_ID.getName() + " " + ca.getId());

            // Store the internal information
            saveClass(printWriter, ca.getUmlClass(), null);
            saveRelationship(printWriter, ca.getRelationship());

            // Close the association class
            printWriter.println(SaveLabels.ASSOC_CLASS_CLOSURE.getName());
        }

        // Close the file
        writer.close();
		
	}




    /**
     * Method that writes on the file the information about an UML class
     * received as a parameter.
     * @param printWriter A print writer already configured to write on a
     *                    given file. This must not be empty.
     * @param c The UML Class with the information to be stored.
     * @param n The graphical node corresponding to the class. If the UML
     *          class corresponds to an association class, this parameter
     *          must be null, to avoid storing the node information.
     */
    private static void saveClass(PrintWriter printWriter, UmlClass c, Node n) {

        // Save the node information
        printWriter.println(SaveLabels.CLASS.getName());
        printWriter.println(SaveLabels.CLASS_NAME.getName() + " " + c.getName());
        printWriter.println(SaveLabels.ID_CLASS.getName() + " " + c.getId());
        printWriter.println(SaveLabels.ABSTRACT_CLASS.getName() + " " + c.isAbstract() );

        // If the node is not empty
        if(n != null) {
            printWriter.println(SaveLabels.NODE_POINT.getName() + " "
                    + n.getLayoutX() + "," + n.getLayoutY());
        }

        // Now get the attributes of the class
        for (Attribute a : c.getAttributes()) {
            printWriter.println(SaveLabels.ATTRIBUTE.getName());
            printWriter.println(SaveLabels.ATTRIBUTE_NAME.getName() + " " + a.getName());
            printWriter.println(SaveLabels.ATTRIBUTE_TYPE.getName() + " " + a.getType().getName());
            printWriter.println(SaveLabels.ATTRIBUTE_ID.getName() + " " + a.getId());
            printWriter.println(SaveLabels.ORDERED_ATTRIBUTE.getName() + " " + a.isOrdered());
            printWriter.println(SaveLabels.UNIQUE_ATTRIBUTE.getName() + " " + a.isUnique());
            printWriter.println(SaveLabels.ATTRIBUTE_CLOSURE.getName());
        }

        // Close the class
        printWriter.println(SaveLabels.CLASS_CLOSURE.getName());
    }







    /**
     * Method that writes on the file the information of the relationship
     * received as a parameter.
     * @param printWriter A print writer already configured to write on a
     *                    given file. This must not be empty.
     * @param r The relationship with the information to be stored.
     */
    private static void saveRelationship(PrintWriter printWriter, Relationship r) {
        // Store the relationship information
        printWriter.println(SaveLabels.RELATIONSHIP.getName());
        printWriter.println(SaveLabels.REL_NAME.getName() + " " + r.getName());
        printWriter.println(SaveLabels.REL_ID.getName() + " " + r.getId());
        printWriter.println(SaveLabels.REL_TYPE.getName() + " " + r.getType().getName());

        // Origin endpoint information
        printWriter.println(SaveLabels.REL_END.getName());
        printWriter.println(SaveLabels.END_NAME.getName() + " " + r.getOrigin().getName());
        printWriter.println(SaveLabels.BROWSABLE_END.getName() + " " + r.getOrigin().isBrowsable());
        printWriter.println(SaveLabels.UNIQUE_END.getName() + " " + r.getOrigin().isUnique());
        printWriter.println(SaveLabels.ORDERED_END.getName() + " " + r.getOrigin().isOrdered());
        printWriter.println(SaveLabels.END_CARDINALITY.getName() + " " + r.getOrigin().getCardinality());
        printWriter.println(SaveLabels.END_TYPE.getName() + " " + r.getOrigin().getType().getName());
        printWriter.println(SaveLabels.END_CLASS.getName() + " " + r.getOrigin().getClassOf().getName());
        printWriter.println(SaveLabels.RELATIONSHIP_END_CLOSURE.getName());

        // Destination endpoint information
        printWriter.println(SaveLabels.REL_END.getName());
        printWriter.println(SaveLabels.END_NAME.getName() + " " + r.getEnd().getName());
        printWriter.println(SaveLabels.BROWSABLE_END.getName() + " " + r.getEnd().isBrowsable());
        printWriter.println(SaveLabels.UNIQUE_END.getName() + " " + r.getEnd().isUnique());
        printWriter.println(SaveLabels.ORDERED_END.getName() + " " + r.getEnd().isOrdered());
        printWriter.println(SaveLabels.END_CARDINALITY.getName() + " " + r.getEnd().getCardinality());
        printWriter.println(SaveLabels.END_TYPE.getName() + " " + r.getEnd().getType().getName());
        printWriter.println(SaveLabels.END_CLASS.getName() + " " + r.getEnd().getClassOf().getName());
        printWriter.println(SaveLabels.RELATIONSHIP_END_CLOSURE.getName());

        // Close the relationship
        printWriter.println(SaveLabels.RELATIONSHIP_CLOSURE.getName());
    }


}
