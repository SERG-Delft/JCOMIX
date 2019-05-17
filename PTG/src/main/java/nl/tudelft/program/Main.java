package nl.tudelft.program;

import nl.tudelft.io.ArgumentProcessor;

/**
 * The main class of the tool.
 * This is where the program starts.
 *
 * @author Dimitri Stallenberg
 */
public final class Main {

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private Main() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }


    /**
     * The main method of the CLI project.
     *
     * @param args arguments given to the nl.tudelft.program
     */
    public static void main(String[] args) {

        ArgumentProcessor processor = new ArgumentProcessor();

        Program program = processor.getProgram(args);

        processor.initializeMaps();
        processor.validateAndProcessArguments(args);
        processor.findConflictsInPropertyValues();


        program.start();
    }

    // TODO remove these (are reminders for me)
//        String url = "http://localhost:8080/sbank/Affilier3DsecureSevlet";
//        String testObjectivePath = "TestObjectives/sbank3";
}
