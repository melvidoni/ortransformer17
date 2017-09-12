package umldiagram.graphical.status;


import gui.models.RelationshipModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;




/**
 * Singleton class used to keep the editing status of relationships
 * and classes, and obtain the values throughout the controllers.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class EditingStatus {
    private static EditingStatus instance = new EditingStatus();

    private String className;
    private String editedClassName;
    private boolean associationClass;
    private boolean editedClass;

    private boolean openEditing;
    private boolean editedRel;
    private String relName;
    private String editedRelName;

    private boolean deletedRels;
    private ObservableList<RelationshipModel> delRelationships;







    /**
     * Private default constructor of the class.
     */
    private EditingStatus() {
        // Class values
        className = "";
        editedClassName = "";
        associationClass = false;
        editedClass = false;

        // Relationship values
        openEditing = false;
        editedRel = false;
        deletedRels = false;
        relName = "";
        editedRelName = "";
        delRelationships = FXCollections.observableArrayList();
    }




    /**
     * Method that returns the corresponding instance, or a new one.
     * @return the corresponding instance.
     */
    public static EditingStatus getInstance(boolean newInstance) {
        if(newInstance) instance = new EditingStatus();
        return instance;
    }





    public String getClassName() {
        return className;
    }

    public void setClassName(String cn) {
        className = cn;
    }

    public boolean isAssociationClass() {
        return associationClass;
    }

    public void setAssociationClass(boolean ac) {
        associationClass = ac;
    }

    public boolean isEditedClass() {
        return editedClass;
    }

    public void setEditedClass(boolean e) {
        editedClass = e;
    }

    public String getEditedClassName() {
        return editedClassName;
    }

    public void setEditedClassName(String e) {
        editedClassName = e;
    }


    public boolean hasEditedRel() {
        return editedRel;
    }

    public void setEditedRel(boolean e) {
        editedRel = e;
    }

    public String getRelName() {
        return relName;
    }

    public void setRelName(String r) {
        relName = r;
    }

    public String getEditedRelName() {
        return editedRelName;
    }

    public void setEditedRelName(String e) {
        editedRelName = e;
    }

    public boolean hasDeletedRels() {
        return deletedRels;
    }

    public void setDeletedRels(boolean d) {
        deletedRels = d;
    }

    public ObservableList<RelationshipModel> getDelRelationships() {
        return delRelationships;
    }

    public void setDelRelationships(ObservableList d) {
        delRelationships = d;
    }

    public void setOpenEditing(boolean o) {
        openEditing = o;
    }

    public boolean needsOpenEditing() {
        return openEditing;
    }
}
