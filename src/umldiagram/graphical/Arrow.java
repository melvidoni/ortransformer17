package umldiagram.graphical;



import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import umldiagram.logical.Relationship;
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
    private Polyline loopMainLine;

    private Polygon arrowHead;
    private Label nameLabel;
    private Label originLabel;
    private Label endLabel;




    /**
     * Constructor that creates the group depending on the type of relationship
     * required for this. It works only for relationships that start and end on
     * different classes.
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
                              drawName(startPoint, endPoint);
                              drawCards(startPoint, endPoint, textOrigin, textEnd, fromSide, toSide);
                              break;

            case AGGREGATION: // Draw the polygon
                              drawAggCompPolygon(startPoint, endPoint, false);
                              drawName(startPoint, endPoint);
                              drawCards(startPoint, endPoint, textOrigin, textEnd, fromSide, toSide);
                              break;

            case COMPOSITION: // Draw the polygon filled
                              drawAggCompPolygon(startPoint, endPoint, true);
                              drawName(startPoint, endPoint);
                              drawCards(startPoint, endPoint, textOrigin, textEnd, fromSide, toSide);
                              break;
        }

    }




    /**
     * Constructor that creates an association, aggregation or composition
     * relationship, when they are loops (start and end on the same class).
     * @param fromPoint The starting point.
     * @param toPoint The ending point.
     * @param rel The relationship to be drawn.
     */
    Arrow(Point2D fromPoint, Point2D toPoint, Relationship rel) {
        // Initialize the basics
        super();
        name = rel.getName();
        type = rel.getType();

        // Prepare the stops
        Point2D firstStop = new Point2D(fromPoint.getX() + 30, fromPoint.getY());
        Point2D secondStop = new Point2D(fromPoint.getX() + 30, toPoint.getY() - 30);
        Point2D thirdStop = new Point2D(toPoint.getX(), toPoint.getY() - 30);

        // Create the loop line
        loopMainLine = new Polyline(
                // Starting point
                fromPoint.getX(), fromPoint.getY(),
                // First stop
                firstStop.getX(), firstStop.getY(),
                // Second stop
                secondStop.getX(), secondStop.getY(),
                // Third stop
                thirdStop.getX(), thirdStop.getY(),
                // Destination stop
                toPoint.getX(), toPoint.getY()
        );
        loopMainLine.setStyle("-fx-stroke: #565656;");
        getChildren().add(loopMainLine);

        // If it is is aggregation or composision...
        if(rel.getType().equals(RelationshipType.AGGREGATION)
                || rel.getType().equals(RelationshipType.COMPOSITION)) {
            // ...then draw the polygon on the point
            drawAggCompPolygon(thirdStop, toPoint,
                    rel.getType().equals(RelationshipType.COMPOSITION)
            );
        }

        // Now draw the name and cards
        drawName(firstStop, secondStop);
        drawCards(fromPoint, toPoint,
                rel.getOrigin().getName() + "\n" + rel.getOrigin().getCardinality(),
                rel.getEnd().getName() + "\n" + rel.getEnd().getCardinality(),
                'R', 'T'
         );

    }


    /**
     * Method that draws the polygon for aggregation or composition classes.
     * @param startPoint The starting point of the line.
     * @param endPoint The ending point of the line.
     * @param isFilled true if it must be filled, false otherwise.
     */
    private void drawAggCompPolygon(Point2D startPoint, Point2D endPoint, boolean isFilled) {
        // Prepare the direction
        double aDir = Math.atan2(startPoint.getX() - endPoint.getX(),
                startPoint.getY() - endPoint.getY());
        int i1 = 10;

        // Get the left point
        Point2D leftPoint = new Point2D(
                endPoint.getX() + getCoordX(i1, aDir + 0.5),
                endPoint.getY() + getCoordY(i1, aDir + 0.5)
        );

        // Now load the polygon
        arrowHead = new Polygon(
                // Ending points
                endPoint.getX(), endPoint.getY(),
                // Left point
                leftPoint.getX(), leftPoint.getY(),
                // Bottom point
                leftPoint.getX() + getCoordX(i1, aDir - 0.5),
                leftPoint.getY() + getCoordY(i1, aDir - 0.5),
                // Right point
                endPoint.getX() + getCoordX(i1, aDir - 0.5),
                endPoint.getY() + getCoordY(i1, aDir - 0.5)
        );

        // Now set the style
        arrowHead.setStyle("-fx-stroke: #565656; -fx-fill: #"
                + ((isFilled) ? "565656;" : "FFFFFF;"));

        // And load the polygon
        getChildren().add(arrowHead);
    }



    /**
     * Method that draws the name for a relationship.
     * @param startPoint The starting point.
     * @param endPoint The ending point.
     */
    private void drawName(Point2D startPoint, Point2D endPoint) {
        // Set the relationship name
        nameLabel = new Label(name);
        nameLabel.setLayoutX(
                ((endPoint.getX() + startPoint.getX())/2)
        );
        nameLabel.setLayoutY((endPoint.getY() + startPoint.getY())/2);
        nameLabel.setStyle("-fx-text-fill: #4a4b4b;");
        getChildren().add(nameLabel);
    }



    /**
     * Method that draws the cardinalities for a relationship.
     * @param startPoint The starting point.
     * @param endPoint The ending point.
     * @param textOrigin The text for the originating role.
     * @param textEnd The text for the ending role.
     * @param fromSide Side where it starts.
     * @param toSide Side where it ends.
     */
    private void drawCards(Point2D startPoint, Point2D endPoint, String textOrigin,
                                  String textEnd, char fromSide, char toSide) {
        // Now the origin label
        originLabel = new Label(textOrigin);
        originLabel.setStyle("-fx-text-fill: #7F7F7F; -fx-font-size: 0.9em; -fx-font-style: italic;");
        getChildren().add(originLabel);
        Point2D olPoint = drawEndpoints(fromSide, startPoint, originLabel);
        originLabel.relocate(olPoint.getX(), olPoint.getY());

        // The ending label
        endLabel = new Label(textEnd);
        endLabel.setStyle("-fx-text-fill: #7F7F7F; -fx-font-size: 0.9em; -fx-font-style: italic;");
        getChildren().add(endLabel);
        Point2D elPoint = drawEndpoints(toSide, endPoint, endLabel);
        endLabel.relocate(elPoint.getX(), elPoint.getY());
    }






    /**
     * Method that writes the cardinality and name on each endpoint. It
     * evaluates the position according to the obtained character.
     * @param side Indicates the position.
     * @param p Starting drawing point.
     * @param label the label to be written
     */
    private Point2D drawEndpoints(char side, Point2D p, Label label){
        // Depending on the side
        switch(side){
            case 'T': // Top
                      return new Point2D(p.getX() + 2, p.getY() - 30);

            case 'R': // Right
                      return new Point2D(p.getX() + 2, p.getY() - 2);

            case 'B': // Down
                      return new Point2D(p.getX(), p.getY() + 11);

            case 'L': // Left
                     // Create a text to obtain the width
                     Text trashLabel = new Text(label.getText());
                     new Scene(new Group(trashLabel));
                     trashLabel.applyCss();

                     // Now return the corresponding point
                     return new Point2D(p.getX() - trashLabel.getLayoutBounds().getWidth() - 3,
                              p.getY() - 2);
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
