package umldiagram.graphical.status;




public class EditingStatus {
    private static EditingStatus instance = new EditingStatus();

    private String className;
    private String editedClassName;
    private boolean associationClass;
    private boolean edited;


    /**
     * Private default constructor of the class.
     */
    private EditingStatus() {
        className = "";
        editedClassName = "";
        associationClass = false;
        edited = false;
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

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean e) {
        edited = e;
    }

    public String getEditedClassName() {
        return editedClassName;
    }

    public void setEditedClassName(String e) {
        editedClassName = e;
    }
}
