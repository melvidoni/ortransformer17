package gui.components;


import javafx.scene.control.TextFormatter;


/**
 * Class that generates field formatters for the TextFields on different
 * graphical interfaces of the system.
 * @see TextFormatter
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
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



    /**
     * Formatter for a textfield with only numbers, up to
     * the max length provided as a parameter.
     * @param maxLength Max number of allowed characters.
     * @return A formatter configured with the appropriate regex.
     */
    public static TextFormatter<String> getNumberFormatter(int maxLength) {
        // Return the formatter
        return new TextFormatter<>( change -> {
            String newText = change.getControlNewText() ;
            if (newText.matches("[0-9]{0," + maxLength + "}")) {
                return change ;
            }
            else return null ;
        });
    }



    /**
     * Formatter for a textfield with only numbers, up to
     * the max length provided as a parameter.
     * @param maxLength Max number of allowed characters.
     * @return A formatter configured with the appropriate regex.
     */
    public static TextFormatter<String> getStarFormatter(int maxLength) {
        // Return the formatter
        return new TextFormatter<>( change -> {
            String newText = change.getControlNewText() ;
            if (newText.matches("[*]{0,1}|[0-9]{0," + maxLength + "}")) {
                return change ;
            }
            else return null ;
        });
    }



}
