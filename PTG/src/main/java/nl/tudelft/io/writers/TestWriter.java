package nl.tudelft.io.writers;

import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.io.IOException;
import java.util.Map;

/**
 * This abstract class describes a test writer which writes tests for the webapp.
 *
 * @author Dimitri Stallenberg
 */
public abstract class TestWriter {

    private Map<String, String> properties;

    /**
     * Constructor.
     *
     * @param properties the properties map
     */
    public TestWriter(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * This method will write a test for the given objective using the test case object.
     *
     * @param objective the objective
     * @param testCase  the test case object
     * @throws IOException this method can throw an IOException if the save path is invalid
     */
    public abstract void writeTest(TestObjective objective, TestCase testCase) throws IOException;

    /**
     * Gets the properties map.
     *
     * @return the map of properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }
}
