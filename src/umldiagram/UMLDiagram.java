package umldiagram;

import umldiagram.enums.RelationshipType;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class that represents a UML diagram in the logical model.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class UMLDiagram {
	private String name;
	private List<UmlClass> classes;
	private List<Relationship> relationships;
	private List<AssociationClass> associationClasses;


    /**
     * Constructor that initializes the instance with the diagram
     * name and the obtained browser panel.
     * @param n The name of the diagram.
     */
	public UMLDiagram(String n) {
		classes = new LinkedList<>();
		relationships = new LinkedList<>();
		associationClasses = new LinkedList<>();
		name = n;
	}


    /**
     * Method that adds a new class to the diagram,
     * while keeping the existing ones.
     * @param c The new class of the diagram.
     */
	public void addClass(UmlClass c) {
		classes.add(c);
	}


    /**
     * Method that adds a new relationship to the list in
     * the diagram, preserving the existing ones.
     * @param r The new relationship to be added.
     */
	public void addRelationship(Relationship r) {
		relationships.add(r);
	}


    /**
     * Method that adds a new association class relationship
     * to the diagram, while preserving the existing ones.
     * @param ac The new association class to be added.
     */
	public void addAssociationClass(AssociationClass ac) {
		associationClasses.add(ac);
	}

	
	/**
	 * Method that evaluates if there are generalziation relationships
     * on this UML diagram.
	 * @return true if there is at least one, false if there is none.
	 */
	public boolean existGeneralizationRels() {
        for(Relationship r : relationships) {
            if (r.getType().equals(RelationshipType.GENERALIZATION))
                return true;
        }
		return false;
	}


    /**
     * Getter to obtain the list of classes.
     * @return The list of classes in object format
     */
	public List<UmlClass> getClasses() {
		return classes;
	}

    /**
     * Getter to obtain the list of association classes.
     * @return The list of association classes in object format.
     */
	public List<AssociationClass> getAssociationClasses() {
		return associationClasses;
	}


    /**
     * Getter to obtain the list of relationships in the diagram.
     * @return The list of relationships as objects.
     */
	public List<Relationship> getRelationships(){
		return relationships;
	}


	/**
	 * Method that obtains a list populated only with the names of the
     * classes belonging to the logic diagram.
	 * @return a list with the names of the classes. It can be empty if
     * there are no classes on the diagram.
	 */
	public LinkedList<String> getClassesNames() {
	    LinkedList<String> namesList = new LinkedList<>();

	    namesList.addAll(
	            classes.stream().map(c -> c.getName().toUpperCase())
                        .collect(Collectors.toCollection(LinkedList::new)));
	    namesList.addAll(
	            associationClasses.stream().map(AssociationClass::getUmlClass)
                        .map(c -> c.getName().toUpperCase()).collect(Collectors.toCollection(LinkedList::new))
        );


		return namesList;
	}


    /**
     * Getter to replace the name of the diagram for a new one.
     * @param n The new name of the diagram.
     */
	public void setName(String n) {
		name = n;
	}


    /**
     * Getter to obtain the current name of the diagram.
     * @return The current name of the diagram.
     */
	public String getName() {
		return name;
	}


	
	/**
	 * Method that deletes the class which name was received as a
     * parameter. It also deletes the relationships and association
     * classes linked to that class.
	 * @param className Name of the class to be deleted
	 */
	public void deleteClass(String className) {
		// Delete the association classes
		Iterator<AssociationClass> itca = associationClasses.iterator();
		while(itca.hasNext()){
			AssociationClass ca = itca.next();
			Relationship rca = ca.getRelationship();
			if(rca.getOrigin().getClassOf().getName().equals(className)
					|| rca.getEnd().getClassOf().getName().equals(className))
				itca.remove();
		}
		
		// Delete the relationships
        relationships.removeIf(r -> r.getOrigin().getClassOf().getName().equals(className)
                || r.getEnd().getClassOf().getName().equals(className));

		// Delete the class
        classes.removeIf(c -> c.getName().equals(className));
	}

	
	/**
	 * Returns the class which names matches the one received
     * as a parameter.
	 * @param className The class name as a string.
	 * @return The object of the class, if the name matches.
     * If no class is found, then returns null.
	 */
	public UmlClass getClasses(String className) {
        for (UmlClass c : classes) {
            if (c.getName().equals(className)) return c;
        }
		return null;
	}



	
	/**
	 * Evaluates if there is a relationship, regardless of its type, between
     * the two classes which names are received as parameters. It does not check
     * if the classes exists, it believes they do.
	 * @param oneClass Name of one of the classes.
	 * @param anotherClass Name of the other class.
	 * @return true if there is at least one relationship between these classes,
     * false otherwise.
	 */
	public boolean existsRelationshipBetween(String oneClass, String anotherClass) {
        for(Relationship r : relationships) {
            String co = r.getOrigin().getClassOf().getName();
            String cd = r.getEnd().getClassOf().getName();
            if ((co.equals(oneClass) && cd.equals(anotherClass))
                    || (co.equals(anotherClass) && cd.equals(oneClass))) {
                return true;
            }
        }
		return false;
	}


	
	/**
	 * Evaluates if there is a generalization between the two classes
     * which names are received as parameters. It dos not consider which
     * class is parent and which one is child.
     * @param oneClass Name of one of the classes.
     * @param anotherClass Name of the other class.
     * @return true if there is at least one relationship between these classes,
     * false otherwise.
	 */
	public boolean existsGeneralizationBetween(String oneClass, String anotherClass) {
        for(Relationship r : relationships) {
            if (r.getType().equals(RelationshipType.GENERALIZATION)) {
                String co = r.getOrigin().getClassOf().getName();
                String cd = r.getEnd().getClassOf().getName();
                if ((co.equals(oneClass) && cd.equals(anotherClass))
                        || (co.equals(anotherClass) && cd.equals(oneClass))) {
                    return true;
                }
            }
        }
		return false;
	}



	/**
	 * Returns the new id for the new relationship.
	 * @return The new id is given by the amounf of relationships plus one.
	 */
	public String newRelId() {
        IntSummaryStatistics summaryStatistics =
                relationships.stream().map(Relationship::getId).mapToInt(Integer::parseInt).summaryStatistics();

        return String.valueOf(summaryStatistics.getMax()+1);
	}



	/**
	 * Evaluates if the class which name is received as a parameter
     * already belongs as a child node in a generalization.
	 * @param className The class name to search for.
	 * @return true if it already is a child node, false otherwise.
	 */
	public boolean isChildNode(String className) {
        for(Relationship r : relationships) {
            if (r.getType().equals(RelationshipType.GENERALIZATION)) {
                if (r.getOrigin().getClassOf().getName().equals(className))
                    return true;
            }
        }
		return false;
	}


	
	/**
	 * Evaluates if a relationship already exists with the name received
     * as a parameter. Returns a boolean depending on the result.
	 * @param relName The name of the relationship.
	 * @return true if the name exists, false otherwise.
	 */
	public boolean existsRelationship(String relName) {
		// Check for association, aggregation or composition
        for(Relationship r : relationships) {
            if (r.getName().toUpperCase().equals(relName.toUpperCase()))
                return true;
        }
		// Check for association classes
        for(AssociationClass ca : associationClasses) {
            Relationship r = ca.getRelationship();
            if (r.getName().toUpperCase().equals(relName.toUpperCase()))
                return true;
        }
		return false;
	}


    /**
     * Getter to obtain the next id of an association class.
     * @return The next id of an association class.
     */
	public String getAssocClassId() {
        IntSummaryStatistics summaryStatistics =
                associationClasses.stream().map(AssociationClass::getId)
                        .mapToInt(Integer::parseInt).summaryStatistics();

        return String.valueOf(summaryStatistics.getMax()+1);
	}


    /**
     * Getter to obtain a list of names of the association classes.
     * @return A list of names as strings.
     */
	public List<String> getAssocClassName() {
		LinkedList<String> names = new LinkedList<>();
        for(AssociationClass ca : associationClasses) {
            names.add(ca.getUmlClass().getName().toUpperCase());
        }
		return names;
	}


	/**
	 * Method that searches all the relationships of the logical diagram,
     * that link with the class which name matches with the ones received
     * as a parameter.
	 * @param className Name of the class to be searched.
	 * @return List of objects with the name and relationship.
	 */
	public List<Object[]> getRelationshipsOf(String className) {
		List<Object[]> list = new LinkedList<>();
		// Check the relationships
        for(Relationship r : relationships) {
            if (r.getOrigin().getClassOf().getName().equals(className)
                    || r.getEnd().getClassOf().getName().equals(className)) {
                Object[] row = new Object[2];
                row[0] = r.getName();
                row[1] = r.getType().getName();
                list.add(row);
            }
        }
		// Now check the association classes
        for(AssociationClass ca : associationClasses) {
            Relationship r = ca.getRelationship();
            if (r.getOrigin().getClassOf().getName().equals(className)
                    || r.getEnd().getClassOf().getName().equals(className)) {
                Object[] row = new Object[2];
                row[0] = r.getName();
                row[1] = "Association Class";
                list.add(row);
            }
        }
		return list;
	}



	/**
	 * Method that deletes an association class which relationship has
     * the same name as the one received as parameter.
	 * @param relName Relationship name to be deleted.
	 */
	public void deleteAssociationClass(String relName) {
        associationClasses.removeIf(ca -> ca.getRelationship().getName()
                .toUpperCase().equals(relName.toUpperCase()));
	}



	/**
	 * Method that removes the relationship (of type association, aggregation
     * or generaliztion), which names matches the parameter.
	 * @param relName Relationship name to be deleted.
	 */
	public void deleteRelationship(String relName) {
        relationships.removeIf(r -> r.getName().toUpperCase().equals(relName.toUpperCase()));
	}


	/**
	 * Method that returns the relationship which name matches
     * the one received as a parameter.
	 * @param relName Relationship name to be matched.
	 * @return Relationship object that matches the name received.
	 */
	public Relationship getRelationship(String relName) {
        for (Relationship r : relationships) {
            if (r.getName().toUpperCase().equals(relName.toUpperCase()))
                return r;
        }
		return null;
	}


	/**
	 * Method that returns the association class which logical class has
     * the same name as the one received as parameter.
	 * @param cName Class name to be matched.
	 * @return AssociationClass if found, the object of the association class.
     * If not found, returns null.
	 */
	public AssociationClass getAssociationClasses(String cName) {
        for(AssociationClass ca : associationClasses) {
            if (ca.getUmlClass().getName().toUpperCase().equals(cName.toUpperCase()))
                return ca;
        }
		return null;
	}

	
	/**
	 * Method that returns the association class which relationship has a
     * name that matches with the one received as parameter.
	 * @param relName Relationship name to be matched.
	 * @return AssociationClass if found, the object of the association class.
     * If not found, returns null.
	 */
	public AssociationClass getRelAssociationClass(String relName) {
        for(AssociationClass ca : associationClasses) {
            if (ca.getRelationship().getName().toUpperCase().equals(relName.toUpperCase()))
                return ca;
        }
		return null;
	}


	
	/**
	 * Method that obtains the names of all existing relationships.
	 * @return A list with the names of all the relationships, regardless
     * of the type, and including association classes.
	 */
	public List<String> getRelationshipsNames() {
        LinkedList<String> names =
                relationships.stream().map(r -> r.getName().toUpperCase())
                        .collect(Collectors.toCollection(LinkedList::new));

        for (AssociationClass ca : associationClasses) {
            names.add(ca.getRelationship().getName().toUpperCase());
        }
		
		return names;
	}


    /**
     * Evaluates if a received name for an association class is already used
     * either as a regular class name, or as an association class name.
     * @param className The class name to be compared.
     * @return true if the name already exists, false if it does not.
     */
    public boolean existsAssociationClass(String className) {
	    // If a normal class exists, or if an association class exists
	    return getClassesNames().contains(className.toUpperCase());
    }


}
