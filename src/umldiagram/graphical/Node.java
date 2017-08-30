package umldiagram.graphical;


import java.util.LinkedList;

public class Node {
    private String name;
    private LinkedList<String> attributes;


    /**
     * Default constructor of the class, that initializes
     * the node with the values received.
     * @param n Name of the corresponding class.
     * @param attrs List of attributes as strings
     */
    public Node(String n, LinkedList<String> attrs) {
        name = n;
        attributes = attrs;
    }
}
