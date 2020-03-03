package nl.tudelft.program;

import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.io.readers.TestObjectiveReader;
import nl.tudelft.proxy.HttpProcessor;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestObjective;
import nl.tudelft.tobuilder.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
        Map<String, Flag> flagMap = ArgumentProcessor.buildArgumentMap();
        ArgumentProcessor processor = new ArgumentProcessor(flagMap);

        Program program = processor.getProgram(args);

        Map<String, String> propertyArgumentsMap = ConfigReader.readConfig(flagMap);

        processor.initializeMaps(propertyArgumentsMap);
        processor.validateAndProcessArguments(args);
        processor.findConflictsInPropertyValues();

        TestObjectiveReader reader = new TestObjectiveReader(processor.getPropertyValue("to-read-path"));
        List<TestObjective> testObjectives = reader.readTestObjectives(ProxyReader.getLanguage());
        testObjectives.sort(Comparator.comparing(TestObjective::getFileName));

        Map<String, Pair<String, Integer>> proxyEntries = ProxyReader.readProxyEntries();

        HttpProcessor proxy = new HttpProcessor(
                processor.getPropertyValue("proxy-url"),
                processor.getPropertyValue("connection"));

        Experiment experiment = new Experiment(testObjectives, proxyEntries);

        program.start(proxy, experiment);
    }
}
