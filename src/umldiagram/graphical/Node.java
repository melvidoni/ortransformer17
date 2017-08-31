package umldiagram.graphical;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class Node extends Rectangle {
    private String name;


    public Node(double x, double y, int w, int h, Color color) {
        super(x, y, w, h);
        setFill(color);
    }


}
