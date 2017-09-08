package gui.models;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;





public class AttributeModel {
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleBooleanProperty ordered;
    private final SimpleBooleanProperty unique;


    /**
     * Default constructor that initializes the model
     * with empty values.
     */
    public AttributeModel() {
        name = new SimpleStringProperty("");
        type = new SimpleStringProperty("");
        ordered = new SimpleBooleanProperty(false);
        unique = new SimpleBooleanProperty(false);
    }


    /**
     * Default constructor that initializes the model
     * with the values received as parameters
     * @param n Name of the attribute.
     * @param t Type of the attribute.
     * @param o If it is ordered or not.
     * @param u If is is unique or not.
     */
    public AttributeModel(String n, String t, Boolean o, Boolean u) {
        name = new SimpleStringProperty(n);
        type = new SimpleStringProperty(t);
        ordered = new SimpleBooleanProperty(o);
        unique = new SimpleBooleanProperty(u);
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

    public boolean isOrdered() {
        return ordered.get();
    }

    public void setOrdered(boolean o) {
        ordered.set(o);
    }

    public boolean isUnique() {
        return unique.get();
    }

    public void setUnique(boolean u) {
        unique.set(u);
    }

}
