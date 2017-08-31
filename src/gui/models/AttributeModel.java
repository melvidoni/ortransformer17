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
