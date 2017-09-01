package umldiagram.graphical;


import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import umldiagram.logical.enums.RelationshipType;
import javafx.geometry.Point2D;

import java.awt.*;


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
    private Label nameLabel;
    private Label originLabel;
    private Label endLabel;




    /**
     * Constructor that creates the group depending on the type of relationship
     * required for this.
     * @param n The name of the relationship.
     * @param startPoint The starting point.
     * @param endPoint The ending point.
     * @param rt The type of relationship.
     * @param textOrigin The role name and cardinality of the origin.
     * @param textEnd The role name and cardinality of the ending.
     * @param fromSide A character indicating from which side the origin is located.
     * @param toSide A character indicating from which side the end is located.
     */
    Arrow(String n, Point2D startPoint, Point2D endPoint, RelationshipType rt,
          String textOrigin, String textEnd, char fromSide, char toSide) {

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
        switch (type) {
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

            case ASSOCIATION: // Simply draw the name and the cardinalities
                              drawNameAndCards(startPoint, endPoint, textOrigin, textEnd,
                                      fromSide, toSide);
                              break;

        }

    }







    private void drawNameAndCards(Point2D startPoint, Point2D endPoint, String textOrigin,
                                  String textEnd, char fromSide, char toSide) {

        // Set the relationship name
        nameLabel = new Label(name);
        nameLabel.setLayoutX(
                ((endPoint.getX() + startPoint.getX())/2)
        );
        nameLabel.setLayoutY((endPoint.getY() + startPoint.getY())/2);
        nameLabel.setStyle("-fx-text-fill: #4a4b4b;");
        getChildren().add(nameLabel);


        // Now the origin label
        originLabel = new Label(textOrigin);
        Point2D olPoint = drawEndpoints(fromSide, startPoint, originLabel);
        originLabel.setLayoutX(olPoint.getX());
        originLabel.setLayoutY(olPoint.getY());
        getChildren().add(originLabel);


        // The ending label
        endLabel = new Label(textEnd);
        Point2D elPoint = drawEndpoints(toSide, endPoint, endLabel);
        endLabel.setLayoutX(elPoint.getX());
        endLabel.setLayoutY(elPoint.getY());
        getChildren().add(endLabel);
    }






    /**
     * Method that writes the cardinality and name on each endpoint. It
     * evaluates the position according to the obtained character.
     * @param side Indicates the position.
     * @param p Starting drawing point.
     * @param label the label to be written
     */
    private Point2D drawEndpoints(char side, Point2D p, Label label){
        switch(side){
            case 'T': // Top
            case 'R': // Right
                      return new Point2D(p.getX() + 2, p.getY() - 2);

            case 'B': // Down
                      return new Point2D(p.getX(), p.getY() + 11);

            case 'L': // Left
                      return new Point2D(p.getX() - label.getWidth() * 7,
                              p.getY() - 15);
        }

        return new Point2D(0, 0);
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
