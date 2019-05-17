package nl.tudelft.program;

import java.util.Arrays;
import java.util.Set;

/**
 * This class implements the flag object.
 * It contains information about flags that can be given as arguments to the program.
 *
 * @author Dimitri Stallenberg
 */
public class Flag {

    private String description;
    private String property;

    private boolean hasArgument;
    private boolean hasOptions;
    private boolean hasFormat;

    private boolean setting;

    private Set<String> options;
    private String regex;

    /**
     * A constructor for this class.
     * Mainly used for settings with a boolean value and thus have no argument.
     *
     * @param property    the property this flag applies to
     * @param description the description of the flag
     */
    public Flag(String property, String description) {
        this.hasOptions = false;
        this.hasFormat = false;
        this.hasArgument = false;
        this.property = property;
        this.description = description;
        this.setting = true;
    }

    /**
     * A constructor for this class.
     * Mainly used for flags which are not settings.
     *
     * @param property    the property this flag applies to
     * @param description the description of the flag
     * @param setting     whether this flag is a setting
     */
    public Flag(String property, String description, boolean setting) {
        this.hasOptions = false;
        this.hasFormat = false;
        this.hasArgument = false;
        this.property = property;
        this.description = description;
        this.setting = setting;
    }

    /**
     * A constructor for this class.
     * Used for flags that have arguments that must be one out of a few options.
     *
     * @param property    the property this flag applies to
     * @param description the description of the flag
     * @param options     the options of which the argument of this flag can be
     */
    public Flag(String property, String description, Set<String> options) {
        this.hasArgument = true;
        this.hasOptions = true;
        this.hasFormat = false;
        this.options = options;
        this.property = property;
        this.description = description;
        this.setting = true;
    }

    /**
     * A constructor for this class.
     * Used for flags that have arguments that must conform to a certain format
     *
     * @param property    the property this flag applies to
     * @param description the description of the flag
     * @param regex       the format to which the argument of this flag must conform to
     */
    public Flag(String property, String description, String regex) {
        this.hasArgument = true;
        this.hasOptions = false;
        this.hasFormat = true;
        this.regex = regex;
        this.property = property;
        this.description = description;
        this.setting = true;
    }

    /**
     * This method generates a string that represents the flag object.
     *
     * @return the string representing the object
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(property + "\n");
        string.append("\t").append(description).append("\n");

        if (hasArgument()) {
            string.append("\tthis property requires an argument\n");

            if (hasOptions()) {
                string.append("\tOptions for this property are:\n");

                string.append("\t").append(Arrays.toString(options.toArray())).append("\n");
            }

            if (hasFormat()) {
                string.append("\tArgument must be of the form: ").append(regex).append("\n");
            }
        }
        return string.toString();
    }

    /**
     * Checks whether this flag needs an argument.
     *
     * @return boolean whether this flag needs an argument
     */
    public boolean hasArgument() {
        return hasArgument;
    }

    /**
     * Checks whether this flag has argument options.
     *
     * @return boolean whether this flag has argument options
     */
    public boolean hasOptions() {
        return hasOptions;
    }

    /**
     * Checks whether this flag has an argument format.
     *
     * @return boolean whether this flag has an argument format
     */
    public boolean hasFormat() {
        return hasFormat;
    }

    /**
     * Gets the options for this flag's argument.
     *
     * @return the argument options for this flag
     */
    public Set<String> getOptions() {
        return options;
    }

    /**
     * Gets the format for this flag's argument.
     *
     * @return the argument format for this flag
     */
    public String getFormat() {
        return regex;
    }

    /**
     * Gets the property name of this flag.
     *
     * @return the property name of this flag
     */
    public String getProperty() {
        return property;
    }

    /**
     * Checks whether this flag is a setting or a mode.
     *
     * @return boolean whether this flag is a setting
     */
    public boolean isSetting() {
        return setting;
    }
}
