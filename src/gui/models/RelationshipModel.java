package gui.models;


import javafx.beans.property.SimpleStringProperty;




public class RelationshipModel {
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;




    /**
     * Default constructor that initializes the model
     * with empty values.
     */
    public RelationshipModel() {
        name = new SimpleStringProperty("");
        type = new SimpleStringProperty("");
    }




    /**
     * Default constructor that initializes the model
     * with the values received as parameters
     * @param n Name of the attribute.
     * @param t Type of the attribute.
     */
    public RelationshipModel(String n, String t) {
        name = new SimpleStringProperty(n);
        type = new SimpleStringProperty(t);
    }


    public String getName() {
        return name.get();
    }

    public void setName(String n) {
        name.set(n);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String t) {
        type.set(t);
    }
}
