package transformations.save;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import umldiagram.logical.*;
import umldiagram.graphical.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;








/**
 * Class that saves the UML diagram in a special format of the system,
 * with a *.ort file extension. Though the extension is custom, the
 * file is an XML file, parsed with JavaX utilities.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class SaveDiagram {



	/**
	 * Main method to perform the file saving using the exclusive ORT format.
     * @param targetFile The target file where information is going to be stored.
     * @param nodes List of graphical nodes from the drawing diagram.
     */
	public static void export(File targetFile, LinkedList<Node> nodes)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {

		// Get the diagram information
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);

        // Prepare the DOM parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Get the root element
        Element rootElement = document.createElement(SaveLabels.DOC_ROOT.getName());
        document.appendChild(rootElement);

        // Store the classes
        Element classesElement = document.createElement(SaveLabels.DOC_CLASSES.getName());
        for(UmlClass c: umlDiagram.getClasses()) {
            // Find the corresponding node
            for(Node n: nodes) {
                // If this is it
                if(c.getName().equals(n.getName())) {
                    // Save the node
                    classesElement.appendChild(saveClass(document, c, n.getLayoutX(),
                            n.getLayoutY(), false));
                    break;
                }
            }
        }
        // Add the classes to the root
        rootElement.appendChild(classesElement);


        // Store the relationships
        Element relsElement = document.createElement(SaveLabels.DOC_RELS.getName());
        for(Relationship r: umlDiagram.getRelationships()) {
            // Add the relationship to the node
            relsElement.appendChild( saveRelationship(document, r) );
        }
        // Add the relationships to the root
        rootElement.appendChild(relsElement);


        // Store the association classes
        Element assocsElement = document.createElement(SaveLabels.DOC_ASSOCCLASS.getName());
        for(AssociationClass ac: umlDiagram.getAssociationClasses()) {
            // Create the basic element
            Element acElement = document.createElement(SaveLabels.ASSOC_CLASS.getName());
            acElement.setAttribute(SaveLabels.ASSOC_CLASS_ID.getName(), String.valueOf(ac.getId()));

            // Append the elements
            acElement.appendChild(saveClass(document, ac.getUmlClass(), 0, 0, true));
            acElement.appendChild(saveRelationship(document, ac.getRelationship()));

            // Now save this
            assocsElement.appendChild(acElement);
        }
        rootElement.appendChild(assocsElement);



        // Configure the transformer
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Now store the information on the file
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(targetFile);
        transformer.transform(source, result);
	}







    /**
     * Method that writes on the file the information about an UML class
     * received as a parameter.
     * @param document The document to create the elements.
     * @param c The UML Class with the information to be stored.
     * @param coordX X coordinate of the graphical node.
     * @param coordY Y coordinate of the graphical node.
     * @param isAssocClass true if this is an association class, false otherwise.
     *                     This param allows saving or ignoring the coordinates
     *                     values to be stored.
     */
    private static Element saveClass(Document document, UmlClass c, double coordX,
                                     double coordY, boolean isAssocClass) {

        // Create a new element
        Element cElement = document.createElement(SaveLabels.CLASS.getName());

        // Save the basic elements
        cElement.setAttribute(SaveLabels.CLASS_NAME.getName(), c.getName());
        cElement.setAttribute(SaveLabels.CLASS_ABSTRACT.getName(), String.valueOf(c.isAbstract()));
        cElement.setAttribute(SaveLabels.CLASS_ID.getName(), String.valueOf(c.getId()));

        // If this is not an assoc class...
        if(!isAssocClass) {
            // ...then save the node points
            cElement.setAttribute(SaveLabels.NODE_POINT_X.getName(), String.valueOf(coordX));
            cElement.setAttribute(SaveLabels.NODE_POINT_Y.getName(), String.valueOf(coordY));
        }


        // Now put the attributes
        Element attrsElement = document.createElement(SaveLabels.DOC_ATTRIBUTES.getName());
        for(Attribute a: c.getAttributes()) {
            // Create an element with the info
            Element aElement = document.createElement(SaveLabels.ATTRIBUTE.getName());

            // Add the values
            aElement.setAttribute(SaveLabels.ATTRIBUTE_NAME.getName(), a.getName());
            aElement.setAttribute(SaveLabels.ATTRIBUTE_TYPE.getName(), a.getType().getType());
            aElement.setAttribute(SaveLabels.ATTRIBUTE_ID.getName(), String.valueOf(a.getId()));
            aElement.setAttribute(SaveLabels.ATTRIBUTE_ORDERED.getName(), String.valueOf(a.isOrdered()));
            aElement.setAttribute(SaveLabels.ATTRIBUTE_UNIQUE.getName(), String.valueOf(a.isUnique()));

            // Put the attribute on the list
            attrsElement.appendChild(aElement);
        }
        // Now store the attributes
        cElement.appendChild(attrsElement);

        // And return the class
        return cElement;
    }







    /**
     * Method that writes on the file the information of the relationship
     * received as a parameter.
     * @param document The document to create the elements.
     * @param r The relationship with the information to be stored.
     */
    private static Element saveRelationship(Document document, Relationship r) {
        // Create an element
        Element rElement = document.createElement(SaveLabels.RELATIONSHIP.getName());

        // Add the basic information
        rElement.setAttribute(SaveLabels.REL_NAME.getName(), r.getName());
        rElement.setAttribute(SaveLabels.REL_TYPE.getName(), r.getType().getName());
        rElement.setAttribute(SaveLabels.REL_ID.getName(), String.valueOf(r.getId()));

        // Create the origin endpoint
        Element oepElement = document.createElement(SaveLabels.ENDPOINT_REL.getName());
        oepElement.setAttribute(SaveLabels.END_NAME.getName(), r.getOrigin().getName());
        oepElement.setAttribute(SaveLabels.END_BROWSABLE.getName(), String.valueOf(r.getOrigin().isBrowsable()));
        oepElement.setAttribute(SaveLabels.END_UNIQUE.getName(), String.valueOf(r.getOrigin().isUnique()));
        oepElement.setAttribute(SaveLabels.END_ORDERED.getName(), String.valueOf(r.getOrigin().isOrdered()));
        oepElement.setAttribute(SaveLabels.END_TYPE.getName(), r.getOrigin().getType().getName());
        oepElement.setAttribute(SaveLabels.END_ORIGIN.getName(), "true");
        oepElement.setAttribute(SaveLabels.END_CLASS.getName(), r.getOrigin().getClassOf().getName());
        oepElement.setAttribute(SaveLabels.END_CARDINALITY.getName(), r.getOrigin().getCardinality());
        rElement.appendChild(oepElement);

        // Create the destination endpoint
        Element depElement = document.createElement(SaveLabels.ENDPOINT_REL.getName());
        depElement.setAttribute(SaveLabels.END_NAME.getName(), r.getEnd().getName());
        depElement.setAttribute(SaveLabels.END_BROWSABLE.getName(), String.valueOf(r.getEnd().isBrowsable()));
        depElement.setAttribute(SaveLabels.END_UNIQUE.getName(), String.valueOf(r.getEnd().isUnique()));
        depElement.setAttribute(SaveLabels.END_ORDERED.getName(), String.valueOf(r.getEnd().isOrdered()));
        depElement.setAttribute(SaveLabels.END_TYPE.getName(), r.getEnd().getType().getName());
        depElement.setAttribute(SaveLabels.END_ORIGIN.getName(), "false");
        depElement.setAttribute(SaveLabels.END_CLASS.getName(), r.getEnd().getClassOf().getName());
        depElement.setAttribute(SaveLabels.END_CARDINALITY.getName(), r.getEnd().getCardinality());
        rElement.appendChild(depElement);

        // Now return the node
        return rElement;
    }


}
