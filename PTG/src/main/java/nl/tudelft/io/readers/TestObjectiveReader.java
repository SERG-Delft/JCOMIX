package nl.tudelft.io.readers;

import nl.tudelft.io.LogUtil;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will read in the test objectives.
 *
 * @author Dimitri Stallenberg
 */
public class TestObjectiveReader {

    private String filesPath;

    /**
     * Constructor.
     *
     * @param filesPath the path of the test objectives folder
     */
    public TestObjectiveReader(String filesPath) {
        this.filesPath = filesPath;
    }

    /**
     * This function reads all the test objectives from the folder.
     *
     * @return a list of all the testobjectives in the folder
     */
    public List<TestObjective> readTestObjectives() {
        File dir = new File(filesPath);
        File[] allFiles = dir.listFiles((dir1, name) -> name.endsWith(".xml"));

        if (allFiles != null) {
            List<TestObjective> testObjectives = new ArrayList<>();

            for (File child : allFiles) {
                TestObjective to = new TestObjective(child);

                testObjectives.add(to);
            }
            return testObjectives;
        } else {
            LogUtil.getInstance().warning("The TO directory does not exist, you can generate one using the --build-tos flag");
            System.exit(1);
        }
        return null;
    }
}
