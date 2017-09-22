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

    private boolean transformed;
    private String typesScript;
    private String tablesScript;




    /**
     * Private empty constructor of the class, that
     * restarts the status of transformation.
     */
    private TransformationStatus() {
        transform = false;
        implementation = null;
        database = null;

        transformed = false;
        typesScript = "";
        tablesScript = "";
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


    public boolean wasTransformed() {
        return transformed;
    }

    public void setTransformed(boolean t) {
        transformed = t;
    }

    public String getTypesScript() {
        return typesScript;
    }

    public void setTypesScript(String t) {
        typesScript = t;
    }

    public String getTablesScript() {
        return tablesScript;
    }

    public void setTablesScript(String t) {
        tablesScript = t;
    }
}
