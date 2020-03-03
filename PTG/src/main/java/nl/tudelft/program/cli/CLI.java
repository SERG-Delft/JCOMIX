package nl.tudelft.program.cli;

import jga.solutions.Solution;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.program.Flag;
import nl.tudelft.program.Program;
import nl.tudelft.proxy.Proxy;
import nl.tudelft.testexecutor.testing.Experiment;

/**
 * The CLI version of the program class.
 *
 * @author Dimitri Stallenberg
 */
public class CLI extends Program {

    private InputAsker inputAsker;

    /**
     * Constructor.
     *
     * @param argumentProcessor the argument processor
     */
    public CLI(ArgumentProcessor argumentProcessor) {
        super(argumentProcessor);
    }

    @Override
    public Solution[] start(Proxy proxy, Experiment experiment) {
        startCLI();
        return super.start(proxy, experiment);
    }

    /**
     *This method will run the cli in the command line and ask the user about settings.
     */
    public void startCLI() {
        this.inputAsker = new InputAsker();

        askSetting("proxy-url", "Please enter the url to the servlet to connect to:");
        askSetting("html-url", "Please enter the url to the html form to connect to:");
        askSetting("to-read-path", "Please specify the path to the folder with testObjectives:");

        boolean useStandard = inputAsker.getInput("Do you want to use config settings?", "(y|n){1}").contains("y");

        if (!useStandard) {
            boolean useStandardProgram = inputAsker.getInput("Do you want to use config program settings?", "(y|n){1}").contains("y");
            // Program
            if (!useStandardProgram) {
                setProgramSettings();
            }

            boolean useStandardTO = inputAsker.getInput("Do you want to use config test objective settings?", "(y|n){1}").contains("y");
            // Test Objective
            if (!useStandardTO) {
                setTOSettings();
            }

            boolean useStandardGA = inputAsker.getInput("Do you want to use config genetic algorithm settings?", "(y|n){1}").contains("y");
            // Genetic algorithm
            if (!useStandardGA) {
                setGASettings();
            }

            boolean useStandardAdvanced = inputAsker.getInput("Do you want to use config advanced settings?", "(y|n){1}").contains("y");
            // Advanced
            if (!useStandardAdvanced) {
                setAdvancedSettings();
            }
        }

        this.inputAsker.close();
    }

    private void askSetting(String property, String question) {
        Flag flag = getArgumentProcessor().getPropertyFlag(property);
        if (!flag.isSetting()) {
            throw new IllegalStateException("Asked property " + property + " has no argument!");
        }
        String result;
        if (flag.hasOptions()) {
            result = inputAsker.getInput(question, flag.getOptions());
        } else if (flag.hasFormat()) {
            result = inputAsker.getInput(question, flag.getFormat());
        } else if (!flag.hasArgument()) {
            result = String.valueOf(inputAsker.getInput(question, "(y|n){1}").equals("y"));
        } else {
            result = inputAsker.getInput(question);
        }

        getArgumentProcessor().setProperty(property, result);
    }

    private void setAdvancedSettings() {
        askSetting("connection", "Please specify the connectionType: ");
    }

    private void setProgramSettings() {
        askSetting("time", "Please specify the amount of time to spend: ");
    }

    private void setTOSettings() {
        askSetting("inputs", "Please specify the number of inputs to try: ");
    }

    private void setGASettings() {
        askSetting("restricted", "Do you want to use a restricted alphabet?");
        askSetting("population", "Please specify the population size: ");

        askSetting("budgeting", "Do you want to use budgeting? ");

        askSetting("objective", "Please specify how many objectives you want to run at the same time: ");

        askSetting("approach", "Please specify the approach to use: ");

        askSetting("compare", "Please specify the compare operator to use: ");
        askSetting("score", "Please specify the score operator to use: ");
        askSetting("crossover", "Please specify the crossover operator to use: ");
        askSetting("select", "Please specify the select operator to use: ");
        askSetting("mutate", "Please specify the mutate operator to use: ");

        askSetting("fitness-function", "Please specify the fitness function to use: ");


        askSetting("use-stall-manager", "Do you want to use the stall manager? ");
        askSetting("use-migration-actor", "Do you want to use the migration actor? ");
    }
}
