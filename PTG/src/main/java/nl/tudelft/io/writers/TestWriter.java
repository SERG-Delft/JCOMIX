package nl.tudelft.io.writers;

import nl.tudelft.io.ImportsMapping;
import nl.tudelft.tobuilder.Pair;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This class can write test.java files that are ready for use.
 *
 * @author Dimitri Stallenberg
 */
public class TestWriter {

    private String savePath;
    private String chromeDriverPath;
    private String url;

    /**
     * Constructor.
     *
     * @param savePath          the path of the folder to save the tests in.
     * @param chromeDriverPath  the path to the chrome driver
     * @param url               the url to the form
     */
    public TestWriter(String savePath, String chromeDriverPath, String url) {
        this.savePath = savePath;
        this.chromeDriverPath = chromeDriverPath;
        this.url = url;
    }

    /**
     * This method will write a test for the given objective using the test case object.
     *
     * @param objective the objective
     * @param testCase  the test case object
     * @throws IOException this method can throw an IOException if the save path is invalid
     */
    public void writeTest(TestObjective objective, TestCase testCase) throws IOException {
        String fileName = objective.getFileName().replace("-", "").replace(".xml", "Test");
        String filePath = savePath + System.getProperty("file.separator") + fileName + ".java";

        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        String opening = writeOpening(fileName);
        String setUp = writeSetUp();
        String tearDown = writeTearDown();
        String test = getTest(testCase, objective.getToText());
        String footer = writeEnd();

        List<String> imports = ImportsMapping.getImports(opening + setUp + tearDown + test + footer);

        String importsString = writeImports(imports);

        writer.write(importsString);
        writer.write(opening);
        writer.write(setUp);
        writer.write(tearDown);
        writer.write(test);
        writer.write(footer);

        writer.close();
    }

    /**
     * Gets the string for the testcase.
     *
     * @param testCase the test case
     * @return the string version of the test case
     */
    private String getTest(TestCase testCase, String wantedText) {
        Map<String, Pair<String, Integer>> entries = testCase.getInputs();

        for (String key : entries.keySet()) {
            String preparedValue = entries.get(key).getFirst().replace("\"", "\\\"");
            int index = entries.get(key).getSecond();
            entries.put(key, new Pair<>(preparedValue, index));
        }

        String start = "driver.findElement(By.xpath(\"//input[@name=\'";
        String mid = "\']\")).sendKeys(\"";
        String end = "\");";

        StringBuilder test = new StringBuilder("\t@Test\n" + "\tpublic void aTest(){\n");

        test.append("\t\tString badXML = \"");
        test.append(wantedText.replace("\"", "\\\""));
        test.append("\";\n\n");

        for (String key : entries.keySet()) {
            test.append("\t\t");
            test.append(start);
            test.append(key);
            test.append(mid);

            test.append(entries.get(key).getFirst());

            test.append(end);
            test.append("\n");
        }

        test.append("\t\tdriver.findElement(By.xpath(\"//input[@name='submit']\")).submit();\n\n");

        test.append("\t\tString actualXML = driver.findElement(By.cssSelector(\"pre\")).getText().replaceAll(\"\\\\s+\", \" \");\n\n");

        test.append("\t\tAssertions.assertNotEquals(badXML, actualXML);\n");

        test.append("\t}\n");
        return test.toString();
    }

    /**
     * Writes the imports string according to the imports list.
     *
     * @param imports a list of imported packages
     * @return string of imports
     */
    private String writeImports(List<String> imports) {
        StringBuilder importsString = new StringBuilder();

        for (String anImport : imports) {
            importsString.append("import ");
            importsString.append(anImport);
            importsString.append(";\n");
        }

        importsString.append("\n");

        return importsString.toString();
    }

    /**
     * Writes the opening and variables string of the test file.
     *
     * @param fileName the name of the test file
     * @return string of the opening of the test file
     */
    private String writeOpening(String fileName) {
        String header = "class " + fileName + " {\n\n";
        String props = "\tprivate WebDriver driver;\n\n";

        return header + props;
    }

    /**
     * Writes the setup method for the test class.
     *
     * @return string of the setup method
     */
    private String writeSetUp() {
        return "\t@BeforeEach\n" +
                "\tvoid setup() throws Exception {\n" +
                "\t\tSystem.setProperty(\"webdriver.chrome.driver\", \"" + chromeDriverPath + "\");\n" +
                "\t\tdriver = new ChromeDriver();\n" +
                "\t\tdriver.manage().window().maximize();\n" +
                "\t\tdriver.get(\"" + url + "\");\n" +
                "\t}\n\n";
    }

    /**
     * Writes the tear down method for the test class.
     *
     * @return string of the tear down method
     */
    private String writeTearDown() {
        return "\t@AfterEach\n" +
                "\tvoid tearDown() {\n" +
                "\t\tdriver.quit();\n" +
                "\t}\n\n";
    }

    /**
     * Writes the end of the test file.
     *
     * @return string of the end of the file
     */
    private String writeEnd() {
        return "}";
    }
}
