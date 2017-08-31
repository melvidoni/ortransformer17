package umldiagram.graphical;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import umldiagram.logical.Attribute;
import umldiagram.logical.UmlClass;



class Node extends Label {
    private String name;



    /**
     * Initializes the node with the elements received as parameters.
     * @param x The coordinate x.
     * @param y The coordinate y.
     * @param umlClass The UML class in object format.
     */
    Node(double x, double y, UmlClass umlClass) {
        super();
        name = umlClass.getName();

        // Get the attribute
        String attr = umlClass.getLongestAttribute();


        // Prepare the text
        String text = name.toUpperCase();
        for(Attribute a: umlClass.getAttributes()) {
            text += "\n" + a.toString();
        }


        // Get a label for the text
        setText(text);
        setWrapText(true);
        setStyle("-fx-background-color: #bbe1ff; " +
                "-fx-border-color: #76B6FF; " +
                "-fx-text-fill: #20283d");


        setPadding(new Insets(10,10,10,10));

        setLayoutX(x);
        setLayoutY(y);
    }




    /**
     * Method to override the parent one, and check if a point
     * is contained inside this label.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if it is contained, false otherwise.
     */
    @Override
    public boolean contains(double x, double y) {
        return getLayoutX() <= x && getLayoutY()<=y
            && (getLayoutX() + getWidth())>=x && (getLayoutY()+getHeight())>=y;
    }


    /**
     * Returns the name of the node.
     * @return the name of the node.
     */
    public String getName() {
        return name;
    }
}
