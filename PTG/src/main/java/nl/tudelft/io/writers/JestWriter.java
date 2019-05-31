package nl.tudelft.io.writers;

import nl.tudelft.io.ImportsMapping;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestObjective;
import nl.tudelft.tobuilder.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JestWriter extends TestWriter {

    /**
     * Constructor.
     *
     * @param properties the properties map
     */
    public JestWriter(Map<String, String> properties) {
        super(properties);
    }

    @Override
    public void writeTest(TestObjective objective, TestCase testCase) throws IOException {
        String fileName = objective.getFileName().replace("-", "").replace(".xml", ".test");
        String filePath = getProperties().get("test-save-path") + System.getProperty("file.separator") + fileName + ".js";

        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        String opening = writeOpening();
        String setUp = writeSetUp();
        String test = getTest(testCase, objective.getToText());
        String tearDown = writeTearDown();

        writer.write(opening);
        writer.write(setUp);
        writer.write(test);
        writer.write(tearDown);

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

        StringBuilder test = new StringBuilder("describe('Software under test', () => {\n");

        test.append("\ttest('does not return test objective', async () => {\n");

        test.append("\t\tlet testObjective = '");
        test.append(wantedText);
        test.append("'\n\n");

        test.append("\t\tawait page.goto(url)\n\n");

        for (String key : entries.keySet()) {
            test.append("\t\tawait page.click('[name=\"");
            test.append(key);
            test.append("\"]')\n");
            test.append("\t\tawait page.keyboard.type('");
            test.append(entries.get(key).getFirst());
            test.append("')\n");
        }

        test.append("\t\tawait page.click('[name=\"submit\"]')\n");
        test.append("\t\tawait page.waitForSelector('pre')\n\n");

        test.append("\t\tlet result = await page.$eval('pre', e => e.innerText)\n");
        test.append("\t\tresult = result.replace(/\\s\\s+/g, ' ')\n\n");

        test.append("\t\texpect(result).not.toBe(testObjective)\n\t})\n})\n\n");

        return test.toString();
    }

    /**
     * Writes the opening and variables string of the test file.
     *
     * @return string of the opening of the test file
     */
    private String writeOpening() {
        return "const puppeteer = require('puppeteer')\n" +
                "\n" +
                "const url = '" +
                getProperties().get("html-url") +
                "'\n" +
                "\n" +
                "let browser\n" +
                "let page\n\n";
    }

    /**
     * Writes the setup method for the test class.
     *
     * @return string of the setup method
     */
    private String writeSetUp() {
        return "beforeAll(async () => {\n" +
                "\tbrowser = await puppeteer.launch({})\n" +
                "\tpage = await browser.newPage()\n" +
                "})\n\n";
    }

    /**
     * Writes the tear down method for the test class.
     *
     * @return string of the tear down method
     */
    private String writeTearDown() {
        return "afterAll(() => {\n" +
                "\tbrowser.close()\n" +
                "})\n";
    }
}
