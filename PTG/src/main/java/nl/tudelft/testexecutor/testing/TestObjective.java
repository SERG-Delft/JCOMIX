package nl.tudelft.testexecutor.testing;

import nl.tudelft.util.FileUtil;

import java.io.File;

/**
 * This class represents a test objective.
 *
 * @author Dimitri Stallenberg
 */
public class TestObjective {

    private String fileName;
    private String toText;
    private int insertLocation;

    /**
     * Constructor.
     *
     * @param file the file of the objective.
     */
    public TestObjective(File file) {
        this.fileName = file.getName();
        this.toText = FileUtil.readAndNormalizeFile(file);
        this.insertLocation = findInsertLocation();

        assert this.toText != null;

        // TODO remove possibly
        this.toText = this.toText.replaceAll("\\s+", " ");
    }

    private int findInsertLocation() {
        int index = this.fileName.indexOf("-");

        String subString = this.fileName.substring(index - 1, index);

        // TODO specify format name
        if ("e".equals(subString)) {
            return 1;
        } else {
            return Integer.valueOf(subString) - 1;
        }
    }

    /**
     * Gets the file name of the test objective.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the content of the file.
     *
     * @return the content of the file
     */
    public String getToText() {
        return toText;
    }

    /**
     * Gets the most important insert location of this test objective.
     *
     * @return the most important insert location
     */
    public int getInsertLocation() {
        return insertLocation;
    }

    @Override
    public String toString() {
        return "TestObjective [toName=" + fileName + "]";
    }
}
