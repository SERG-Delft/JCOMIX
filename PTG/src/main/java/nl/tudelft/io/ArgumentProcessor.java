package nl.tudelft.io;

import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.program.Flag;
import nl.tudelft.program.Program;
import nl.tudelft.program.cli.CLI;
import nl.tudelft.program.to_builder.ToBuilder;
import nl.tudelft.util.TextToEnum;

import java.util.*;

/**
 * This class process all the arguments given to the program and also reads the config.
 *
 * @author Dimitri Stallenberg
 */
public class ArgumentProcessor {

    private Map<String, Flag> flagMap;
    private Map<String, String> propertyArgumentMap;
    private Map<String, Flag> propertyFlagMap;

    private boolean called;

    /**
     * Constructor.
     */
    public ArgumentProcessor() {
        this.flagMap = buildArgumentMap();
        this.called = false;
    }

    /**
     * This method initializes the property maps.
     * If this method is not yet called the other functions won't work.
     */
    public void initializeMaps() {
        this.propertyArgumentMap = ConfigReader.readConfig(flagMap);

        this.propertyFlagMap = new HashMap<>();

        for (String key : flagMap.keySet()) {
            Flag flag = flagMap.get(key);
            propertyFlagMap.put(flag.getProperty(), flag);
        }

        this.called = true;
    }

    /**
     * This method checks for mistakes in the user input given to the program.
     */
    public void findConflictsInPropertyValues() {
        if (propertyArgumentMap.get("approach").equals("SIMPLE")) {
            if (propertyArgumentMap.get("use-migration-actor").equals("true")) {
                LogUtil.getInstance().warning("CONFIG FILE EXCEPTION: Cannot use the migration actor when using SIMPLE approach.");
                System.exit(1);
            }

        } else {
            if (propertyArgumentMap.get("objective").equals("ONE")) {
                LogUtil.getInstance().warning("CONFIG FILE EXCEPTION: Cannot use MULTI approach with ONE objective.");
                System.exit(1);
            }
        }
    }

    /**
     * This method creates the program based on the arguments.
     *
     * @param args  the arguments supplied by the user to the program
     * @return      the created program
     */
    public Program getProgram(String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "--build-tos":
                    return new ToBuilder(this);
                case "--gui":
//                    return new GUI(this);
                    break;
                case "--cli":
                    return new CLI(this);
                case "-h":
                case "--help":
                    printCommands();
                    System.exit(0);
                    break;
                case "--init":
                    Initialize.init();
                    System.exit(0);
                    break;
                default:
                    return new Program(this);
            }

        } else {
            return new Program(this);
        }

        LogUtil.getInstance().warning("Something went wrong!");
        System.exit(1);
        return null;
    }

    /**
     * This method validates the arguments given to the program.
     * If an argument is valid it will be put in the propertyArgumentMap map.
     *
     * @param args the arguments given to the program
     */
    public void validateAndProcessArguments(String[] args) {
        if (!called) {
            throw new IllegalStateException("The initializeMaps method has not been called yet! Make sure your program does this before it starts!");
        }
        for (int i = 0; i < args.length; i++) {
            if (args.length != 1 && (args[i].equals("--cli") || args[i].equals("--gui") || args[i].equals("-h") || args[i].equals("--help"))) {
                LogUtil.getInstance().warning("You can't give more arguments when using "
                        + args[0]
                        + " flag.");
                System.exit(1);
            }

            if (!flagMap.containsKey(args[i])) {
                LogUtil.getInstance().warning(args[i]
                        + " is not a valid argument.");
                System.exit(1);
            }

            Flag flag = flagMap.get(args[i]);

            if (flag.hasArgument()) {
                String argument = getArgument(args, i, flag);
                setProperty(flag.getProperty(), argument);
                i++;
            } else {
                setProperty(flag.getProperty(), "true");
            }
        }
    }

    private String getArgument(String[] args, int index, Flag flag) {
        if (index + 1 >= args.length || flagMap.keySet().contains(args[index + 1])) {
            LogUtil.getInstance().warning(args[index]
                    + " misses an argument. Please consult the help menu using -h.");
            System.exit(1);
        }

        if (flag.hasOptions() && !flag.getOptions().contains(args[index + 1])) {
            LogUtil.getInstance().warning(args[index]
                    + " Can't have "
                    + args[index + 1]
                    + " as an argument. Please no main manifest attribute, in comix-1.0-SNAPSHOT.jarconsult the help menu using -h.");
            System.exit(1);
        }

        if (flag.hasFormat() && !args[index + 1].matches(flag.getFormat())) {
            LogUtil.getInstance().warning(args[index]
                    + " Can't have "
                    + args[index + 1]
                    + " as an argument. Please consult the help menu using -h.");
            System.exit(1);
        }

        return args[index + 1];
    }

    /**
     * Creates a mapping of flag strings to Flags.
     *
     * @return the mapping
     */
    @SuppressWarnings("checkstyle:methodlength")
    private Map<String, Flag> buildArgumentMap() {
        Map<String, Flag> flagMap = new HashMap<>();

        flagMap.put("-h", new Flag("Manual", "Displays this help menu.", false));
        flagMap.put("--help", new Flag("Manual", "Displays this help menu.", false));

        flagMap.put("--cli", new Flag("Command Line Interface", "Starts a command line interface to set the settings.", false));
        flagMap.put("--gui", new Flag("Graphical User Interface", "Starts a graphical interface to set the settings.", false));

        flagMap.put("--init", new Flag("Initializes config files", "Creates a template proxy and config file.", false));

        flagMap.put("--build-tos", new Flag("Generates test objectives", "Generates test objectives for a given proxy.", false));

        flagMap.put("-u", new Flag("proxy-url", "Set the url to the servlet to use.", "((https)|(http))://.{0,}"));
        flagMap.put("-k", new Flag("html-url", "Set the url to the html form to use.", "((https)|(http))://.{0,}"));

        flagMap.put("--cd", new Flag("chrome-driver-path", "Set the path to the chrome driver.", ".+"));

        flagMap.put("-p", new Flag("to-read-path", "Set the path to the test objectives.", ".+"));
        flagMap.put("-f", new Flag("test-save-path", "Set the save path for the results.", ".+"));
        flagMap.put("-t", new Flag("time", "Set the max amount of time to run.", "[1-9][0-9]*([dhms])"));
        flagMap.put("-n", new Flag("inputs", "Set the amount of inputs to use.", "[1-9][0-9]*"));
        flagMap.put("-s", new Flag("population", "Set the population size.", "[1-9][0-9]*"));
        flagMap.put("-r", new Flag("restricted", "Set the restricted alphabet option."));

        Set<String> connectionOptions = new HashSet<>();
        connectionOptions.add("HttpClient");

        flagMap.put("-c", new Flag("connection", "Set the connection type.", connectionOptions));

        Set<String> objectiveOptions = new HashSet<>();
        objectiveOptions.add("ONE");
        objectiveOptions.add("ALL");

        flagMap.put("-o", new Flag("objective", "Set how many objectives to run at the time.", objectiveOptions));

        flagMap.put("-a", new Flag("approach", "Set the approach.", TextToEnum.getApproachOptions()));
        flagMap.put("-i", new Flag("compare", "Set the comparing.", TextToEnum.getCompareOptions()));
        flagMap.put("-g", new Flag("score", "Set the scoring.", TextToEnum.getScoringOptions()));
        flagMap.put("-x", new Flag("crossover", "Set the crossover.", TextToEnum.getCrossOverOptions()));
        flagMap.put("-e", new Flag("select", "Set the selection.", TextToEnum.getSelectionOptions()));
        flagMap.put("-m", new Flag("mutate", "Set the mutation.", TextToEnum.getMutationOptions()));

        Set<String> fitnessFunctionOptions = new HashSet<>();
        fitnessFunctionOptions.add("LINEAR_DISTANCE");
        fitnessFunctionOptions.add("REAL_CODED_EDIT_DISTANCE");
        fitnessFunctionOptions.add("SEQUENCE_ALIGNMENT_DISTANCE");
        fitnessFunctionOptions.add("STRING_EDIT_DISTANCE");

        flagMap.put("-j", new Flag("fitness-function", "Set the fitness function.", fitnessFunctionOptions));


        Set<String> verboseLevels = new HashSet<>();
        verboseLevels.add("0");
        verboseLevels.add("1");
        verboseLevels.add("2");
        verboseLevels.add("3");

        flagMap.put("-d", new Flag("verbose-level", "Set the verbose level.", verboseLevels));
        flagMap.put("-b", new Flag("budgeting", "Set the budgeting option."));

        flagMap.put("-sm", new Flag("use-stall-manager", "Set the stall manager option."));
        flagMap.put("-ra", new Flag("use-result-actor", "Set the result actor option."));
        flagMap.put("-ma", new Flag("use-migration-actor", "Set the migration actor option."));


        return flagMap;
    }

    /**
     * Adds new flags to the tool.
     *
     * @param flag       the flag
     * @param flagObject the flag object
     */
    public void addFlag(String flag, Flag flagObject) {
        if (flagMap.containsKey(flag)) {
            throw new IllegalStateException("Flag: " + flag + " is already in use by the " + flagMap.get(flag).getProperty() + " property!");
        }
        flagMap.put(flag, flagObject);
    }

    /**
     * Prints all the information about the flag arguments.
     */
    public void printCommands() {
        StringBuilder x = new StringBuilder();

        List<String> keys = new ArrayList<>(flagMap.keySet());

        keys.sort(Comparator.comparingInt(o -> o.toCharArray()[1]));

        for (String s : keys) {
            Flag flag = flagMap.get(s);
            x.append(s).append(" : ").append(flag.toString());
        }

        System.out.println(x.toString());
    }

    /**
     * Sets a property given a field and a value.
     *
     * @param field the field name
     * @param value the new value
     */
    public void setProperty(String field, String value) {
        if (!called) {
            throw new IllegalStateException("The initializeMaps method has not been called yet! Make sure your program does this before it starts!");
        }
        propertyArgumentMap.put(field, value);
    }

    /**
     * Gets the propertyArgumentMap map.
     *
     * @return the map containing the propertyArgumentMap
     */
    public Map<String, String> getPropertyArgumentMap() {
        if (!called) {
            throw new IllegalStateException("The initializeMaps method has not been called yet! Make sure your program does this before it starts!");
        }
        return propertyArgumentMap;
    }

    /**
     * Gets the value of a property.
     *
     * @param property the wanted property
     * @return the value of the property
     */
    public String getPropertyValue(String property) {
        if (!called) {
            throw new IllegalStateException("The initializeMaps method has not been called yet! Make sure your program does this before it starts!");
        }
        if (!getPropertyArgumentMap().containsKey(property)) {
            throw new IllegalStateException("Property is missing!");
        }

        return getPropertyArgumentMap().get(property);
    }

    /**
     * Gets the flag object of a property.
     *
     * @param property the wanted property
     * @return the flag object of the property
     */
    public Flag getPropertyFlag(String property) {
        if (!called) {
            throw new IllegalStateException("The initializeMaps method has not been called yet! Make sure your program does this before it starts!");
        }
        if (!propertyFlagMap.containsKey(property)) {
            throw new IllegalStateException("Property is missing!");
        }

        return propertyFlagMap.get(property);
    }


}
