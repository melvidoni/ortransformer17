package gui.components;


import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


/**
 * Class that generates field formatters for the TextFields on different
 * graphical interfaces of the system.
 * @see TextFormatter
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class FieldFormatter {

    // PATTERNS
    private static Pattern PATTERN_MIX = Pattern.compile("[A-Za-z0-9_]*");
    private static Pattern PATTERN_NUMERIC = Pattern.compile("[0-9]*");
    private static Pattern PATTERN_STAR = Pattern.compile("[*]{0,1}|[0-9]*");


    /**
     * Formatter for a textfield with alphabetic and numeric characters.
     */
    public static UnaryOperator<TextFormatter.Change> FILTER_MIX = c -> {
        if (PATTERN_MIX.matcher(c.getControlNewText()).matches()) {
            return c ;
        } else {
            return null ;
        }
    };


    /**
     * Formatter for a textfield with only numbers.
     */
    public static UnaryOperator<TextFormatter.Change> FILTER_NUMERIC = c -> {
        if (PATTERN_NUMERIC.matcher(c.getControlNewText()).matches()) {
            return c ;
        } else {
            return null ;
        }
    };



    /**
     * Formatter for a textfield with only star and numbers.
     */
    public static UnaryOperator<TextFormatter.Change> FILTER_STAR = c -> {
        if (PATTERN_STAR.matcher(c.getControlNewText()).matches()) {
            return c ;
        } else {
            return null ;
        }
    };





    /**
     * Formatter for a textfield with only numbers, up to
     * the max length provided as a parameter.
     * @param maxLength Max number of allowed characters.
     * @return A formatter configured with the appropriate regex.
     */
    /*
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
*/


}
