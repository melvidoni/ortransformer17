package transformations.ort;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import transformations.ort.enums.XMLLabels;
import umldiagram.graphical.Node;
import umldiagram.logical.*;

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
 * Class that performs the main conversion from UML to XML, in order
 * to start with the subsequent XML transformations.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class UMLtoXML {



	/**
	 * Method that effectively transforms the diagram into XML.
     */
	public static void transformToXML(File file, boolean forTransformation, LinkedList<Node> nodes)
            throws ParserConfigurationException, TransformerException {

	    System.out.println(file.getAbsolutePath());

        // Get the diagram information
        UMLDiagram umlDiagram = UMLDiagram.getInstance(false);

        // Prepare the DOM parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

	    // Get the root label
		Element root = document.createElement(XMLLabels.ROOT.getName());
		document.appendChild(root);


		// For each class, add it to the node
        for(UmlClass c : umlDiagram.getClasses()) {
            // Get the node
            Node node = null;

            // If this is not for transformation, find the equivalent node
            if(!forTransformation)
                for(Node n: nodes)
                    if(n.getName().equals(c.getName())) {
                        // Save the information and break the loop
                        node = n;
                        break;
                    }

            // Now add the node
            root.appendChild( getXmlClass(document, c, forTransformation, node, false) );
        }


		// Add the relationships
        for(Relationship r : umlDiagram.getRelationships()) {
            // Add the element
            root.appendChild( getXmlRelationship(document, r) );
         }


		// Add the association classes
        for(AssociationClass ac : umlDiagram.getAssociationClasses()) {
              // Create the basic element
            Element acElement = document.createElement(XMLLabels.ASSOCIATION_CLASS.getName());
            acElement.setAttribute(XMLLabels.ASSOC_CLASS_ID.getName(), String.valueOf(ac.getId()));

            // Append the elements
            acElement.appendChild(getXmlClass(document, ac.getUmlClass(),
                    forTransformation, null, true));
            acElement.appendChild(getXmlRelationship(document, ac.getRelationship()));

            // Now save this
            root.appendChild(acElement);
        }


        // Configure the transformer
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Now store the information on the file
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
	}









	/**
	 * Method that transforms a relationship in UML format (as object)
     * towards an XML element.
     * @param document The XML document where the information is stored.
     * @param r Relationship in object UML format.
     * @return Relationship transformed as an XML element.
	 */
	private static Element getXmlRelationship(Document document, Relationship r) {
        // Create an element
        Element rElement = document.createElement(XMLLabels.RELATIONSHIP.getName());

        // Add the basic information
        rElement.setAttribute(XMLLabels.REL_NAME.getName(), r.getName());
        rElement.setAttribute(XMLLabels.REL_TYPE.getName(), r.getType().getName());
        rElement.setAttribute(XMLLabels.REL_ID.getName(), String.valueOf(r.getId()));

        // Create the origin endpoint
        Element oepElement = document.createElement(XMLLabels.REL_START.getName());
        oepElement.setAttribute(XMLLabels.REL_EP_NAME.getName(), r.getOrigin().getName());
        oepElement.setAttribute(XMLLabels.REL_EP_BROWSABLE.getName(), String.valueOf(r.getOrigin().isBrowsable()));
        oepElement.setAttribute(XMLLabels.REL_EP_UNIQUE.getName(), String.valueOf(r.getOrigin().isUnique()));
        oepElement.setAttribute(XMLLabels.REL_EP_ORDERED.getName(), String.valueOf(r.getOrigin().isOrdered()));
        oepElement.setAttribute(XMLLabels.REL_EP_TYPE.getName(), r.getOrigin().getType().getName());
        oepElement.setAttribute(XMLLabels.REL_EP_CLASS.getName(), r.getOrigin().getClassOf().getName());
        oepElement.setAttribute(XMLLabels.REL_EP_CARD.getName(), r.getOrigin().getCardinality());
        rElement.appendChild(oepElement);

        // Create the destination endpoint
        Element depElement = document.createElement(XMLLabels.REL_DEST.getName());
        depElement.setAttribute(XMLLabels.REL_EP_NAME.getName(), r.getEnd().getName());
        depElement.setAttribute(XMLLabels.REL_EP_BROWSABLE.getName(), String.valueOf(r.getEnd().isBrowsable()));
        depElement.setAttribute(XMLLabels.REL_EP_UNIQUE.getName(), String.valueOf(r.getEnd().isUnique()));
        depElement.setAttribute(XMLLabels.REL_EP_ORDERED.getName(), String.valueOf(r.getEnd().isOrdered()));
        depElement.setAttribute(XMLLabels.REL_EP_TYPE.getName(), r.getEnd().getType().getName());
        depElement.setAttribute(XMLLabels.REL_EP_CLASS.getName(), r.getEnd().getClassOf().getName());
        depElement.setAttribute(XMLLabels.REL_EP_CARD.getName(), r.getEnd().getCardinality());
        rElement.appendChild(depElement);

        // Now return the node
        return rElement;
	}









	/**
	 * Method that transforms a class in UML object format, towards
     * an XML element already transformed. Includes the attributes.
     * @param document The XML document where the information is stored.
	 * @param c Class in object UML format.
	 * @param forTransformation true if this file will be used for transformation
     *                          false otherwise. It limits recording the graphical
     *                          information.
     * @param node The graphical node with information for storing the points,
     *             in the case this is not for transformation.
     * @param isAssocClass true if this is association class, false otherwise.
     *                     It also impacts on whether the coordinates are written
     *                     or avoided in the process.
     * @return Class transformed as an XML element.
	 */
	private static Element getXmlClass(Document document, UmlClass c,
                                       boolean forTransformation, Node node, boolean isAssocClass) {

	    // Prepare the element
        Element xmlClass = document.createElement(XMLLabels.CLASS.getName());

	    // Set basic attributes
        xmlClass.setAttribute(XMLLabels.CLASS_NAME.getName(), c.getName());
        xmlClass.setAttribute(XMLLabels.CLASS_ID.getName(), c.getId());
        xmlClass.setAttribute(XMLLabels.CLASS_ABSTRACT.getName(), Boolean.toString(c.isAbstract()));

        // If this is not for transformation and it is not an association class
        if(!forTransformation && !isAssocClass) {
            // Then record the points
            xmlClass.setAttribute(XMLLabels.NODE_POINT_X.getName(), String.valueOf(node.getTranslateX()));
            xmlClass.setAttribute(XMLLabels.NODE_POINT_Y.getName(), String.valueOf(node.getTranslateY()));
        }

		// Add the attributes
        for(Attribute a : c.getAttributes()) {
            // Create a new element
            Element xmlAttribute = document.createElement(XMLLabels.ATTRIBUTE.getName());
            xmlAttribute.setAttribute(XMLLabels.ATTRIBUTE_ID.getName(), String.valueOf(a.getId()));
            xmlAttribute.setAttribute(XMLLabels.ATTRIBUTE_NAME.getName(), a.getName());
            xmlAttribute.setAttribute(XMLLabels.ATTRIBUTE_TYPE.getName(), a.getType().getType());
            xmlAttribute.setAttribute(XMLLabels.ATTRIBUTE_ORDERED.getName(), Boolean.toString(a.isOrdered()));
            xmlAttribute.setAttribute(XMLLabels.ATTRIBUTE_UNIQUE.getName(), Boolean.toString(a.isUnique()));

            // Add it to the class
            xmlClass.appendChild(xmlAttribute);
        }

        // Return the transformed value
		return xmlClass;
	}


	
}
