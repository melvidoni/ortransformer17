package transformations.save;


import javafx.geometry.Point2D;
import umldiagram.graphical.DrawingDiagram;
import umldiagram.logical.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collectors;






/**
 * Method that opens the file added on the constructor for reading, and
 * to reload the data in the new diagram (logic and graphic). It later
 * updates the parent window. Only works with *.ort files, stored
 * using this system.
 * @see SaveDiagram
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class OpenDiagram {




    /**
     * Main method to open a file, using the custom format from OR-Transformer.
     * @param targetFile The file from which to read the diagram.
     * @param drawing The canvas where the drawing needs to be generated.
     */
	public static void open(File targetFile, DrawingDiagram drawing) throws IOException {

	    // Get the stream with the lines
        LinkedList<String> stream = new LinkedList<>(
                Files.lines(Paths.get(targetFile.getAbsolutePath()))
                        .collect(Collectors.toList())
        );


        // Get the diagram
        UMLDiagram diagram = UMLDiagram.getInstance(false);


        // Go through the lines
        for(int i=0; i<stream.size(); i++) {
            // Get the line
            String line = stream.get(i);

            // If this line contains a class definition
            if(line.contains(SaveLabels.CLASS.getName())) {
                // Prepare the information
                // TODO THIS POINT IS NOT UPDATING ITS VALUE!!!
                Point2D point = new Point2D(0,0);
                UmlClass c = new UmlClass();

                // Read the class
                i += readClass(stream, i, point, c);

                // Add the information to the diagram
                diagram.addClass(c);
                drawing.addNewNode(point.getX(), point.getY(), c);

            }
        }















        /*






	    // Prepare the basics
		FileReader reader = new FileReader(targetFile);
		BufferedReader translator = new BufferedReader(reader);;




        // Read the lines of the file
        String line;
        while( (line = translator.readLine()) != null){
            // If this is a class
            if(line.contains(SaveLabels.CLASS.getName())) {
                // Create the new logical class
                UmlClass c = new UmlClass();
                Point2D point = new Point2D(0, 0);

                // Until we reach the class closure
                while (!(line = translator.readLine()).contains(SaveLabels.CLASS_CLOSURE.getName())) {

                    // Split the line
                    String[] splitCL = line.split(" ");

                    // Store the name if this is it
                    if (line.contains(SaveLabels.CLASS_NAME.getName())) c.setName(splitCL[1]);

                        // Store the abstract status
                    else if (line.contains(SaveLabels.ABSTRACT_CLASS.getName()))
                        c.setAbstract(Boolean.valueOf(splitCL[1]));

                        // Store the id of the class
                    else if (line.contains(SaveLabels.ID_CLASS.getName()))
                        c.setId(splitCL[1]);

                        // If this is the point
                    else if (line.contains(SaveLabels.NODE_POINT.getName())) {
                        // Split the line content
                        String[] coord = splitCL[1].split(",");

                        // Update the point
                        point = new Point2D(Double.valueOf(coord[0]), Double.valueOf(coord[1]));
                    }

                    // Now, we reach the attributes
                    else if (line.contains(SaveLabels.ATTRIBUTE.getName())) {
                        // Create a new attribute
                        Attribute a = new Attribute();

                        // Until we reach the closure
                        while (!(line = translator.readLine()).contains(SaveLabels.ATTRIBUTE_CLOSURE.getName())) {
                            // Split the new line
                            String[] splitAL = line.split(" ");

                            // If this is the name, record it
                            if (line.contains(SaveLabels.ATTRIBUTE_NAME.getName())) a.setName(splitAL[1]);

                                // If this is an attribute id, record it
                            else if (line.contains(SaveLabels.ATTRIBUTE_ID.getName()))
                                a.setId(Integer.valueOf(splitAL[1]));

                                // If this is the type, get the enum and compare it
                            else if (line.contains(SaveLabels.ATTRIBUTE_TYPE.getName()))
                                a.setType(AttributeType.getAttributeName(splitAL[1]));

                                // If it is ordered, save the information
                            else if (line.contains(SaveLabels.ORDERED_ATTRIBUTE.getName()))
                                a.setOrdered(Boolean.valueOf(splitAL[1]));

                                // Update if it is a unique attribute
                            else if (line.contains(SaveLabels.UNIQUE_ATTRIBUTE.getName())) {
                                a.setUnique(Boolean.valueOf(splitAL[1]));

                            }

                            // Add the attribute to the class
                            c.addAttribute(a);
                        }
                    }
                }

                // Update the information on the diagram
                diagram.addClass(c);
                drawing.addNewNode(point.getX(), point.getY(), c);
            }





            // If this is a relationship
            else if(line.contains(SaveLabels.RELATIONSHIP.getName())){
                // Create the logical relationship
                Relationship r = new Relationship("0");
                boolean originStored = false;

                // Loop until we find a closure
                while(! (line = translator.readLine()).contains(SaveLabels.RELATIONSHIP_CLOSURE.getName()) ){
                    // Split the line
                    String[] splitRL = line.split(" ");

                    // Store if this is the name
                    if(line.contains(SaveLabels.REL_NAME.getName())) r.setName(splitRL[1]);

                    // Store the id of the relationship
                    else if(line.contains(SaveLabels.REL_ID.getName())) r.setId(splitRL[1]);

                    // If this is the type, compare it and store it
                    else if(line.contains(SaveLabels.REL_TYPE.getName()))
                        r.setType(RelationshipType.getRelType(splitRL[1]));

                    // Now evaluate the endings
                    else if(line.contains(SaveLabels.REL_END.getName())){
                        // Prepare an endpoint
                        RelationshipEndpoint relEnd = new RelationshipEndpoint();

                        // Loop until we reach the closure
                        while(! (line = translator.readLine())
                                .contains(SaveLabels.RELATIONSHIP_END_CLOSURE.getName())){

                            // Split the file line
                            String[] splitEPL = line.split(" ");

                            // If this is the role name, store it
                            if(line.contains(SaveLabels.END_NAME.getName())) relEnd.setName(splitEPL[1]);

                            // If the end is browsable or not, save it
                            else if(line.contains(SaveLabels.BROWSABLE_END.getName()))
                                relEnd.setBrowsable(Boolean.valueOf(splitEPL[1]));

                            // Save information regarding the unique characteristic
                            else if(line.contains(SaveLabels.UNIQUE_END.getName()))
                                relEnd.setUnique(Boolean.valueOf(splitEPL[1]));

                            // If this is ordered or not, save the information
                            else if(line.contains(SaveLabels.ORDERED_END.getName()))
                                relEnd.setOrdered(Boolean.valueOf(splitEPL[1]));

                            // Store the cardinality
                            else if(line.contains(SaveLabels.END_CARDINALITY.getName()))
                                relEnd.setCardinality(splitEPL[1]);

                            // Save the endpoint type
                            else if(line.contains(SaveLabels.END_TYPE.getName()))
                                relEnd.setType(EndpointType.getEndpointType(splitEPL[1]));

                            // Now, because the classes are loaded first, get the class
                            else if(line.contains(SaveLabels.END_CLASS.getName())){
                                relEnd.setClass(diagram.getClasses(splitEPL[1]));
                            }
                        }

                        // If origin was already stored, this is the endpoint
                        if(originStored) r.setEnd(relEnd);
                        // In the other case
                        else {
                            r.setOrigin(relEnd);
                            originStored = true;
                        }
                    }
                }

                // Now store the relationship
                diagram.addRelationship(r);
                drawing.addNewRel(r);
            }





            // Now, it might be an association class
            else if(line.contains(SaveLabels.ASSOC_CLASS.getName())){
                // Create the empty class
                AssociationClass ca = new AssociationClass();

                // Loop until we reach the closure
                while(! (line = translator.readLine()).contains(SaveLabels.ASSOC_CLASS_CLOSURE.getName()) ){
                    // Split the line
                    /*String[] splitACL = line.split(" ");

                    // If this is the ID, store it
                    if(line.contains(SaveLabels.ASSOC_CLASS_ID.getName())) ca.setId(splitACL[1]);

                    // Loop on the inner class
                    else if(line.contains(SaveLabels.CLASS.getName())){
                        LClass c1 = new LClass();
                        Node n1 = new Node();
                        while(! (line = translator.readLine()).contains(SaveLabels.CLASS_CLOSURE.getName()) ){
                            String[] split4 = line.split(" ");
                            if(line.contains(SaveLabels.CLASS_NAME.getName())){
                                c1.setName(split4[1]);
                                n1.setClassName(split4[1]);
                            }
                            else if(line.contains(SaveLabels.ABSTRACT_CLASS.getName())){
                                c1.setAbstract(Boolean.valueOf(split4[1]));
                            }
                            else if(line.contains(SaveLabels.ID_CLASS.getName())){
                                c1.setId(split4[1]);
                                n1.setClassId(split4[1]);
                            }
                            else if(line.contains(SaveLabels.ATTRIBUTE.getName())){
                                Attribute a = new Attribute();
                                while(! (line = translator.readLine()).contains(SaveLabels.ATTRIBUTE_CLOSURE.getName()) ){
                                    String[] split2 = line.split(" ");
                                    if(line.contains(SaveLabels.ATTRIBUTE_NAME.getName())){
                                        a.setName(split2[1]);
                                    }
                                    else if(line.contains(SaveLabels.ATTRIBUTE_ID.getName())){
                                        a.setId(Integer.valueOf(split2[1]));
                                    }
                                    else if(line.contains(SaveLabels.ATTRIBUTE_TYPE.getName())){
                                        a.setType(AttributeType.getAttributeName(split2[1]));
                                    }
                                    else if(line.contains(SaveLabels.ORDERED_ATTRIBUTE.getName())){
                                        a.setOrdered(Boolean.valueOf(split2[1]));
                                    }
                                    else if(line.contains(SaveLabels.UNIQUE_ATTRIBUTE.getName())){
                                        a.setUnique(Boolean.valueOf(split2[1]));
                                    }
                                }
                                c1.addAttribute(a);
                                n1.addAttribute(a);
                            }
                        }
                        ca.setClass(c1);
                        rc.setNode(n1);


                    }
                    else if(line.contains(SaveLabels.RELATIONSHIP.getName())){
                        Relationship r1 = new Relationship();
                        int endpoint = 0;
                        while(! (line = translator.readLine()).contains(SaveLabels.RELATIONSHIP_CLOSURE.getName()) ){
                            String[] split5 = line.split(" ");
                            if(line.contains(SaveLabels.REL_NAME.getName())){
                                r1.setName(split5[1]);
                            }
                            else if(line.contains(SaveLabels.REL_ID.getName())){
                                r1.setId(split5[1]);
                            }
                            else if(line.contains(SaveLabels.REL_TYPE.getName())){
                                r1.setType(RelationshipType.getRelType(split5[1]));
                            }
                            else if(line.contains(SaveLabels.REL_END.getName())){
                                RelationshipEnd relEnd = new RelationshipEnd();
                                while(! (line = translator.readLine()).contains(SaveLabels.RELATIONSHIP_END_CLOSURE.getName())){
                                    String[] split3 = line.split(" ");
                                    if(line.contains(SaveLabels.END_NAME.getName())){
                                        relEnd.setName(split3[1]);
                                    }
                                    else if(line.contains(SaveLabels.BROWSABLE_END.getName())){
                                        relEnd.setBrowsable(Boolean.valueOf(split3[1]));
                                    }
                                    else if(line.contains(SaveLabels.UNIQUE_END.getName())){
                                        relEnd.setUnique(Boolean.valueOf(split3[1]));
                                    }
                                    else if(line.contains(SaveLabels.ORDERED_END.getName())){
                                        relEnd.setOrdered(Boolean.valueOf(split3[1]));
                                    }
                                    else if(line.contains(SaveLabels.END_CARDINALITY.getName())){
                                        relEnd.setCardinality(split3[1]);
                                    }
                                    else if(line.contains(SaveLabels.END_TYPE.getName())){
                                        relEnd.setType(EndType.getEndpointType(split3[1]));
                                    }
                                    else if(line.contains(SaveLabels.END_CLASS.getName())){
                                        relEnd.setClass(logicDiagram.getClasses(split3[1]));
                                    }
                                }
                                if(endpoint == 0 ){
                                    r1.setOrigin(relEnd);
                                    endpoint = 1;
                                }
                                else
                                    r1.setEnd(relEnd);
                            }
                        }
                        ca.setRelationship(r1);
                        rc.setRelationship(r1);
                    }
                }
                logicDiagram.addAssociationClass(ca);
                drawingDiagram.addMixedLine(rc);
            } }

        }*/

	}







    private static int readClass(LinkedList<String> lines, int i, Point2D point, UmlClass c) {
	    // Start on the given line
        int j = i;
        while(!lines.get(j).contains(SaveLabels.CLASS_CLOSURE.getName())) {
            // Get the line and split it
            String[] splitLine = lines.get(j).split(" ");

            // Store the name if this is it
            if (splitLine[0].contains(SaveLabels.CLASS_NAME.getName())) c.setName(splitLine[1]);

            // Store the abstract status
            else if (splitLine[0].contains(SaveLabels.ABSTRACT_CLASS.getName()))
                c.setAbstract(Boolean.valueOf(splitLine[1]));

            // Store the id of the class
            else if (splitLine[0].contains(SaveLabels.ID_CLASS.getName()))
                c.setId(splitLine[1]);

             // If this is the point
            else if (splitLine[0].contains(SaveLabels.NODE_POINT.getName())) {
                // Split the line content
                String[] coord = splitLine[1].split(",");

                // Update the point
                point = new Point2D(Double.valueOf(coord[0]), Double.valueOf(coord[1]));
            }


            // Increment the counter
            j++;
        }

        // Return the number of advanced lines
        return j;
    }


}
