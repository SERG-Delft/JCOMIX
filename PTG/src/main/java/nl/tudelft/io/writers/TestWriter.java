package nl.tudelft.io.writers;

import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestObjective;
import nl.tudelft.util.FileUtil;

import java.io.File;
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
     */
    public void writeTest(TestObjective objective, TestCase testCase) {
        String fileExt = ProxyReader.getLanguage();
        String fileName = objective.getFileName().replace("-", "").replace("." + fileExt, "Test");
        String filePath = getProperties().get("test-save-path") + System.getProperty("file.separator") + fileName + "." + getOutputExtension();

        File file = new File(filePath);

        String test = generateTest(objective, testCase);

        FileUtil.writeFile(file, test, false);
    }

    public abstract String getOutputExtension();

    /**
     * This method will write a test for the given objective using the test case object.
     * @param objective the objective
     * @param testCase  the test case object
     * @return the generated test as a string
     */
    public abstract String generateTest(TestObjective objective, TestCase testCase);

    /**
     * Gets the properties map.
     *
     * @return the map of properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }
}
