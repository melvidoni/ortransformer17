package transformations.save;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import umldiagram.graphical.DrawingDiagram;
import umldiagram.logical.*;
import umldiagram.logical.enums.AttributeType;
import umldiagram.logical.enums.EndpointType;
import umldiagram.logical.enums.RelationshipType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;




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
	public static void open(File targetFile, DrawingDiagram drawing)
            throws ParserConfigurationException, IOException, SAXException {
	    // Get the diagram instance
        UMLDiagram diagram = UMLDiagram.getInstance(false);

        // Read the document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(targetFile);


        // For each class
        NodeList classNodeList =
                ((Element) document.getElementsByTagName(SaveLabels.DOC_CLASSES.getName()).item(0))
                        .getElementsByTagName(SaveLabels.CLASS.getName());
        for(int i=0; i<classNodeList.getLength(); i++) {
            // Prepare the basic values
            double[] coords = new double[2];
            UmlClass umlClass = readClass((Element) classNodeList.item(i), false, coords);

            // Update the information
            diagram.addClass( umlClass );
            drawing.addNewNode(coords[0], coords[1], umlClass);
        }
        // Once the classes are ready, make the layout
        drawing.applyCss();
        drawing.layout();


        // For each relationship
        NodeList relsNodeList =
                ((Element) document.getElementsByTagName(SaveLabels.DOC_RELS.getName()).item(0))
                        .getElementsByTagName(SaveLabels.RELATIONSHIP.getName());
        for(int j=0; j<relsNodeList.getLength(); j++) {
            // Get the relationship
            Relationship umlRel = readRelationship((Element) relsNodeList.item(j), false);

            // Update the information
            diagram.addRelationship(umlRel);
            drawing.addNewRel(umlRel);
        }


        // For each association class
        NodeList assocsNodeList =
                ((Element) document.getElementsByTagName(SaveLabels.DOC_ASSOCCLASS.getName()).item(0))
                        .getElementsByTagName(SaveLabels.ASSOC_CLASS.getName());
        for(int k=0; k<assocsNodeList.getLength(); k++) {
            // Get the element
            Element aElement = (Element) assocsNodeList.item(k);

            // Create a basic association class
            AssociationClass umlAC = new AssociationClass(

                    readClass( (Element) aElement.getElementsByTagName(SaveLabels.CLASS.getName()).item(0),
                            true, new double[2]),
                    readRelationship(
                            (Element) aElement.getElementsByTagName(SaveLabels.RELATIONSHIP.getName()).item(0),
                            true)
            );

            // Update the information
            diagram.addAssociationClass(umlAC);
            drawing.addNewAssociationClass(umlAC);
        }

    }







    /**
     * Method that reads an XML relationship and transforms it towards object format.
     * @param element The XML element to be read.
     * @param isAssocClass true if this is an association class, false otherwise.
     *                     For association classes, the consequence is that it
     *                     changes the related value on the relationship.
     * @return The UML relationship in object format.
     */
    private static Relationship readRelationship(Element element, boolean isAssocClass) {
	    // Create the basic relationship
        Relationship umlRel = new Relationship( element.getAttribute(SaveLabels.REL_ID.getName()) );
        umlRel.setAssociationClass(isAssocClass);
        umlRel.setName( element.getAttribute(SaveLabels.REL_NAME.getName()) );
        umlRel.setType(RelationshipType.getRelType(
                element.getAttribute(SaveLabels.REL_TYPE.getName())
        ));

        // Get the diagram
        UMLDiagram diagram = UMLDiagram.getInstance(false);

        // Get the elements
        NodeList endsList = element.getElementsByTagName(SaveLabels.ENDPOINT_REL.getName());
        for(int j=0; j<endsList.getLength(); j++) {
            // Get the node
            Node epNode = endsList.item(j);

            // If this is usable
            if (epNode.getNodeType() == Node.ELEMENT_NODE) {
                // Then convert it
                Element epElement = (Element) epNode;

                // Create the endpoint
                RelationshipEndpoint umlEndpoint = new RelationshipEndpoint(
                      epElement.getAttribute(SaveLabels.END_NAME.getName()),
                      Boolean.valueOf( epElement.getAttribute(SaveLabels.END_BROWSABLE.getName()) ),
                      Boolean.valueOf( epElement.getAttribute(SaveLabels.END_UNIQUE.getName()) ),
                      Boolean.valueOf( epElement.getAttribute(SaveLabels.END_ORDERED.getName()) ),
                      epElement.getAttribute(SaveLabels.END_CARDINALITY.getName()),
                      diagram.getClasses( epElement.getAttribute(SaveLabels.END_CLASS.getName()) ),
                      RelationshipType.getRelEndpointType(
                              EndpointType.getEndpointType(epElement.getAttribute(SaveLabels.END_TYPE.getName()))
                      )
                );

                // If this is the origin
                if(Boolean.valueOf(epElement.getAttribute(SaveLabels.END_ORIGIN.getName()))) {
                    // Save it as the origin
                    umlRel.setOrigin(umlEndpoint);
                }
                // Otherwise, it is the destination
                else umlRel.setEnd(umlEndpoint);
            }
        }

        // Return the relationship
        return umlRel;
    }








    /**
     * Method that reads an XML class and transforms it towards object format.
     * @param element The XML element to be read.
     * @param isAssocClass true if this is an association class, false otherwise.
     *                     For association classes, the consequence is that it
     *                     ignores the reading of the coordinates.
     * @param coords An array to obtain the graphical coordinates.
     * @return The UML class in object format.
     */
    private static UmlClass readClass(Element element, boolean isAssocClass, double[] coords) {
	    // Create the UML class
        UmlClass umlClass = new UmlClass(
                element.getAttribute(SaveLabels.CLASS_ID.getName()),
                element.getAttribute(SaveLabels.CLASS_NAME.getName()),
                Boolean.valueOf( element.getAttribute(SaveLabels.CLASS_ABSTRACT.getName()) )
        );

        // If this is not an association class
        if(!isAssocClass) {
            // Save the coords
            coords[0] = Double.valueOf(element.getAttribute(SaveLabels.NODE_POINT_X.getName()));
            coords[1] = Double.valueOf(element.getAttribute(SaveLabels.NODE_POINT_Y.getName()));
        }

        // Now get the attributes
        NodeList attrsList =
                (element.getElementsByTagName(SaveLabels.DOC_ATTRIBUTES.getName()).item(0))
                .getChildNodes();

        // For each attribute
        for(int i=0; i<attrsList.getLength(); i++) {
            // Get the element
            Node aNode = attrsList.item(i);

            if (aNode.getNodeType() == Node.ELEMENT_NODE) {
                Element aElement = (Element) aNode;

                // Add the attribute
                umlClass.addAttribute(new Attribute(
                        aElement.getAttribute(SaveLabels.ATTRIBUTE_NAME.getName()),
                        AttributeType.getAttribute(aElement.getAttribute(SaveLabels.ATTRIBUTE_TYPE.getName())),
                        Integer.valueOf(aElement.getAttribute(SaveLabels.ATTRIBUTE_ID.getName())),
                        Boolean.valueOf(aElement.getAttribute(SaveLabels.ATTRIBUTE_ORDERED.getName())),
                        Boolean.valueOf(aElement.getAttribute(SaveLabels.ATTRIBUTE_UNIQUE.getName()))
                ));
            }
        }

        // Return the class
        return umlClass;
    }


}
