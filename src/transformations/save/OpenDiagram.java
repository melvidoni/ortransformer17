package transformations.save;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import transformations.ort.enums.XMLLabels;
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


        // Get the first children
        Node child = document.getElementsByTagName(XMLLabels.ROOT.getName()).item(0).getFirstChild();

        // While there are children
        while(child != null) {
            if(child.getNodeType()==Node.ELEMENT_NODE) {
                // Convert to element
                Element eChild = (Element) child;
                String eLabel = eChild.getNodeName();

                // If it is a class
                if (eLabel.equals(XMLLabels.CLASS.getName())) {
                    // Prepare the basic values
                    double[] coords = new double[2];
                    UmlClass umlClass = readClass(eChild, false, coords);

                    // Update the information
                    diagram.addClass(umlClass);
                    drawing.addNewNode(coords[0], coords[1], umlClass);

                    // Once the classes are ready, make the layout
                    drawing.applyCss();
                    drawing.layout();
                }
                // If it is a relationship
                else if (eLabel.equals(XMLLabels.RELATIONSHIP.getName())) {
                    // Get the relationship
                    Relationship umlRel = readRelationship(eChild, false);

                    // Update the information
                    diagram.addRelationship(umlRel);
                    drawing.addNewRel(umlRel);
                }
                // If it is an association class
                else if (eLabel.equals(XMLLabels.ASSOCIATION_CLASS.getName())) {
                    // Create a basic association class
                    AssociationClass umlAC = new AssociationClass(

                            readClass((Element) eChild.getElementsByTagName(XMLLabels.CLASS.getName()).item(0),
                                    true, new double[2]),
                            readRelationship(
                                    (Element) eChild.getElementsByTagName(XMLLabels.RELATIONSHIP.getName()).item(0),
                                    true)
                    );

                    // Update the information
                    diagram.addAssociationClass(umlAC);
                    drawing.addNewAssociationClass(umlAC);
                }
            }

            // Go to the next
            child = child.getNextSibling();
        }
        // Reading completed
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
        Relationship umlRel = new Relationship( element.getAttribute(XMLLabels.REL_ID.getName()) );
        umlRel.setAssociationClass(isAssocClass);
        umlRel.setName( element.getAttribute(XMLLabels.REL_NAME.getName()) );
        umlRel.setType(RelationshipType.getRelType(
                element.getAttribute(XMLLabels.REL_TYPE.getName())
        ));

        // Get the diagram
        UMLDiagram diagram = UMLDiagram.getInstance(false);


        // Save the origin
        Element oElem = (Element) element.getElementsByTagName(XMLLabels.REL_START.getName()).item(0);
        umlRel.setOrigin(
            new RelationshipEndpoint(
                    oElem.getAttribute(XMLLabels.REL_EP_NAME.getName()),
                Boolean.valueOf( oElem.getAttribute(XMLLabels.REL_EP_BROWSABLE.getName()) ),
                Boolean.valueOf( oElem.getAttribute(XMLLabels.REL_EP_UNIQUE.getName()) ),
                Boolean.valueOf( oElem.getAttribute(XMLLabels.REL_EP_ORDERED.getName()) ),
                    oElem.getAttribute(XMLLabels.REL_EP_CARD.getName()),
                diagram.getClasses( oElem.getAttribute(XMLLabels.REL_EP_CLASS.getName()) ),
                RelationshipType.getRelEndpointType(
                        EndpointType.getEndpointType(oElem.getAttribute(XMLLabels.REL_EP_TYPE.getName()))
                )
            )
        );

        // Set the ending
        Element eElem = (Element) element.getElementsByTagName(XMLLabels.REL_DEST.getName()).item(0);
        umlRel.setEnd(
                new RelationshipEndpoint(
                        eElem.getAttribute(XMLLabels.REL_EP_NAME.getName()),
                        Boolean.valueOf( eElem.getAttribute(XMLLabels.REL_EP_BROWSABLE.getName()) ),
                        Boolean.valueOf( eElem.getAttribute(XMLLabels.REL_EP_UNIQUE.getName()) ),
                        Boolean.valueOf( eElem.getAttribute(XMLLabels.REL_EP_ORDERED.getName()) ),
                        eElem.getAttribute(XMLLabels.REL_EP_CARD.getName()),
                        diagram.getClasses( eElem.getAttribute(XMLLabels.REL_EP_CLASS.getName()) ),
                        RelationshipType.getRelEndpointType(
                                EndpointType.getEndpointType(eElem.getAttribute(XMLLabels.REL_EP_TYPE.getName()))
                        )
                )
        );

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
                element.getAttribute(XMLLabels.CLASS_ID.getName()),
                element.getAttribute(XMLLabels.CLASS_NAME.getName()),
                Boolean.valueOf( element.getAttribute(XMLLabels.CLASS_ABSTRACT.getName()) )
        );

        // If this is not an association class
        if(!isAssocClass) {
            // Save the coords
            coords[0] = Double.valueOf(element.getAttribute(XMLLabels.NODE_POINT_X.getName()));
            coords[1] = Double.valueOf(element.getAttribute(XMLLabels.NODE_POINT_Y.getName()));
        }

        // Now get the attributes
        Node child = element.getFirstChild();

        // While there are children
        while(child != null) {
            // If this is an element
            if(child.getNodeType() == Node.ELEMENT_NODE &&
                    child.getNodeName().equals(XMLLabels.ATTRIBUTE.getName())) {
                Element aElement = (Element) child;

                // Add the attribute
                umlClass.addAttribute(new Attribute(
                        aElement.getAttribute(XMLLabels.ATTRIBUTE_NAME.getName()),
                        AttributeType.getAttribute(aElement.getAttribute(XMLLabels.ATTRIBUTE_TYPE.getName())),
                        Integer.valueOf(aElement.getAttribute(XMLLabels.ATTRIBUTE_ID.getName())),
                        Boolean.valueOf(aElement.getAttribute(XMLLabels.ATTRIBUTE_ORDERED.getName())),
                        Boolean.valueOf(aElement.getAttribute(XMLLabels.ATTRIBUTE_UNIQUE.getName()))
                ));
            }

            // Go to the next one
            child = child.getNextSibling();
        }

        // Return the class
        return umlClass;
    }


}
