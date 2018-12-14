package gui.components;



import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import transformations.ort.TransformationStatus;
import transformations.ort.enums.DatabaseType;


/**
 * Class that controls the Tab included on the main TabPane, that has
 * the scripts information generated by the transformation.
 * @see Tab
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class ScriptTab extends Tab {
    private ScrollPane scrollTypes;
    private ScrollPane scrollTables;

    private DatabaseType databaseType;




    /**
     * Default constructor of the component.
     */
    @FXML
    public void initialize() {
        // Prepare grid pane
        GridPane gridPane = new GridPane();
        setContent(gridPane);

        // Prepare constraints
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setPercentWidth(50);
        gridPane.getColumnConstraints().add(cc1);

        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setPercentWidth(50);
        gridPane.getColumnConstraints().add(cc2);

        RowConstraints rc1 = new RowConstraints();
        rc1.setPercentHeight(100);
        gridPane.getRowConstraints().add(rc1);


        // Start the scrolltypes pane
        newModel();

        // Create title panes
        TitledPane titleTypes = new TitledPane("Types Scripts", scrollTypes);
        titleTypes.setCollapsible(false);
        titleTypes.setExpanded(true);
        titleTypes.setMaxHeight(Double.MAX_VALUE);

        TitledPane titleTables = new TitledPane("Tables Scripts", scrollTables);
        titleTables.setCollapsible(false);
        titleTables.setExpanded(true);
        titleTables.setMaxHeight(Double.MAX_VALUE);

        // Add the titled panes
        gridPane.add(titleTypes,0,0);
        gridPane.add(titleTables,1,0);

        // Allow for this to be closed and add the listeners
        this.setClosable(true);
    }




    /**
     * Method that clears the scrollpanes to remove their content
     * and allow a new model to be created.
     */
    private void newModel() {
        // Start the scrolltypes pane
        scrollTypes = new ScrollPane();
        scrollTypes.setFitToWidth(true);
        scrollTypes.setFitToHeight(true);
        scrollTypes.setMinWidth(200);
        scrollTypes.setPadding(new Insets(10,10,10,10));

        // Start the scrolltables pane
        scrollTables = new ScrollPane();
        scrollTables.setFitToHeight(true);
        scrollTables.setFitToWidth(true);
        scrollTables.setMinWidth(200);
        scrollTables.setPadding(new Insets(10,10,10,10));

        // Clean the type
        databaseType = null;
    }





    /**
     * Method that loads the scripts inside the
     * corresponding panes.
     */
    public void showGeneratedScripts() {
        // Get the transformation status
        TransformationStatus tStatus = TransformationStatus.getInstance(false);

        // Load the types on a label
        Text typesLabel = new Text(tStatus.getTypesScript());
        scrollTypes.setContent(typesLabel);

        // Load the tables on a Label
        Text tablesLabel = new Text(tStatus.getTablesScript());
        scrollTables.setContent(tablesLabel);

        // Store the type
        databaseType = tStatus.getDatabase();
    }




    /**
     * Method to obtain the scripts for the types displayed on
     * the script tabs.
     * @return The text contained on as a types SQL script.
     */
    public String getTypesScript() {
        return ((Text) scrollTypes.getContent()).getText();
    }




    /**
     * Method to obtain the scripts for the tables displayed on
     * the script tabs.
     * @return The text contained on as a tables SQL script.
     */
    public String getTablesScript() {
        return ((Text) scrollTables.getContent()).getText();
    }
}
