package transformations.ort;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import transformations.ort.enums.DatabaseType;
import transformations.ort.enums.ImplementationType;





/**
 * Singleton class used to keep the transformation status of the
 * diagram, and obtain the values throughout the controllers.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class TransformationStatus {
    private static TransformationStatus instance;

    private boolean transform;
    private ImplementationType implementation;
    private DatabaseType database;

    private SimpleDoubleProperty progressProperty;
    private SimpleStringProperty progressLabel;





    /**
     * Private empty constructor of the class, that
     * restarts the status of transformation.
     */
    private TransformationStatus() {
        progressProperty = new SimpleDoubleProperty(0);
        progressLabel = new SimpleStringProperty("Working...");

        transform = false;
        implementation = null;
        database = null;
    }



    /**
     * Method that returns the corresponding instance, or a new one.
     * @return the corresponding instance.
     */
    public static TransformationStatus getInstance(boolean newInstance) {
        if(newInstance) instance = new TransformationStatus();
        return instance;
    }





    public boolean needsTransformation() {
        return transform;
    }

    public void setTransform(boolean t) {
        transform = t;
    }

    public ImplementationType getImplementation() {
        return implementation;
    }

    public void setImplementation(ImplementationType i) {
        implementation = i;
    }

    public DatabaseType getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseType d) {
        database = d;
    }


    public double getProgress() {
        return progressProperty.get();
    }

    public SimpleDoubleProperty getProgressProperty() {
        return progressProperty;
    }


    void setProgressProperty(double pp) {
        progressProperty.set(pp);
        progressLabel.set(pp + "% Completed");
    }

    public SimpleStringProperty getLabelProperty() {
        return progressLabel;
    }

}
