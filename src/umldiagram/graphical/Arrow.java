package umldiagram.graphical;


import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import umldiagram.logical.enums.RelationshipType;
import javafx.geometry.Point2D;




/**
 * Group class that represents a UML relationship, of type generalization,
 * association, aggregation or composition. It is composed of the different
 * graphical parts.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
class Arrow extends Group {
    private String name;
    private RelationshipType type;

    private Line mainLine;
    private Polygon arrowHead;




    /**
     * Constructor that creates the group depending on the type of relationship
     * required for this.
     * @param n The name of the relationship.
     * @param startPoint The starting point.
     * @param endPoint The ending point.
     * @param rt The type of relationship.
     */
    Arrow(String n, Point2D startPoint, Point2D endPoint, RelationshipType rt) {
        // Initialize the basics
        super();
        name = n;
        type = rt;

        // Create the basic line
        mainLine = new Line(startPoint.getX(), startPoint.getY(),
                endPoint.getX(), endPoint.getY());
        mainLine.setStyle("-fx-stroke: #565656;");
        getChildren().add(mainLine);

        // Now, depending on the type...
        switch(type) {
            case GENERALIZATION: // Prepare the direction
                                 double aDir = Math.atan2(startPoint.getX() - endPoint.getX(),
                                         startPoint.getY() - endPoint.getY());
                                 int i1 = 10;

                                 // Create an empty arrow head with the points
                                 arrowHead = new Polygon(
                                         // End point coords
                                         endPoint.getX(), endPoint.getY(),
                                         // Left point coords
                                         endPoint.getX() + getCoordX(i1, aDir + 0.5),
                                         endPoint.getY() + getCoordY(i1, aDir + 0.5),
                                         // Right point coords
                                         endPoint.getX() + getCoordX(i1, aDir - 0.5),
                                         endPoint.getY() + getCoordY(i1, aDir - 0.5)
                                 );
                                 arrowHead.setStyle("-fx-stroke: #565656; -fx-fill: #FFFFFF;");

                                 // Configure and add
                                 getChildren().add(arrowHead);
                                 break;
        }



    }













    /**
     * Method that returns a coordinate on the y axis,
     * depending the direction and length received.
     * @param len The length received.
     * @param dir The direction of the line.
     * @return The calculated y coordinate.
     */
    private int getCoordY(int len, double dir) {
        return (int)(len * Math.cos(dir));
    }




    /**
     * Method that returns a coordinate on the x axis,
     * depending the direction and length received.
     * @param len The length received.
     * @param dir The direction of the line.
     * @return The calculated x coordinate.
     */
    private double getCoordX(int len, double dir) {
        return len * Math.sin(dir);
    }


}
