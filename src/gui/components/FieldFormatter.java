package gui.components;


import javafx.scene.control.TextFormatter;




public class FieldFormatter {


    /**
     * Formatter for a textfield with only letters and numbers, up to
     * the max length provided as a parameter.
     * @param maxLength Max number of allowed characters.
     * @return A formatter configured with the appropriate regex.
     */
    public static TextFormatter<String> getMixedFormatter(int maxLength) {
        // Return the formatter
        return new TextFormatter<>( change -> {
                    String newText = change.getControlNewText() ;
                    if (newText.matches("[0-9a-zA-Z]{0," + maxLength + "}")) {
                        return change ;
                    }
                    else return null ;
        });
    }


}