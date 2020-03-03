package system;

import jga.solutions.Solution;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.io.DirectoryUtil;
import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.io.readers.TestObjectiveReader;
import nl.tudelft.io.writers.JUnitWriter;
import nl.tudelft.program.Flag;
import nl.tudelft.program.Program;
import nl.tudelft.proxy.HttpProcessor;
import nl.tudelft.proxy.Proxy;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestObjective;
import nl.tudelft.tobuilder.Pair;
import org.apache.http.NameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

public class XMLSBankTest {

    Proxy proxy;

    @BeforeEach
    void setUp() {
        proxy = new Proxy() {
            @Override
            public String submit(List<NameValuePair> pairs) {
                String[] order = new String[]{
                        "UserName",
                        "BankCode",
                        "RequestId",
                        "CardNumber"
                };

                StringBuilder result = new StringBuilder("{");

                for (int i = 0; i < order.length; i++) {
                    for (NameValuePair pair : pairs) {
                        if (pair.getName().equals(order[i])) {
                            result.append("\"").append(order[i]).append("\":\"").append(pair.getValue()).append("\"");

                            if (i != order.length - 1) {
                                result.append(",");
                            }
                            break;
                        }
                    }
                }

                result.append("}");

                return result.toString();
            }
        };
    }

    @Test
    public void ProgramTest () {
        Map<String, Flag> flagMap = ArgumentProcessor.buildArgumentMap();
        ArgumentProcessor processor = new ArgumentProcessor(flagMap);

        File TOFile = new File("src/test/resources/tos");
        File testFile = new File("target/generated");

        String args[] = new String[]{
                "-p", TOFile.getAbsolutePath(),
                "-f", testFile.getAbsolutePath(),
                "-t", "60s"
        };

        Program program = processor.getProgram(args);

        Map<String, String> propertyArgumentsMap = ConfigReader.readConfig(flagMap);

        processor.initializeMaps(propertyArgumentsMap);
        processor.validateAndProcessArguments(args);
        processor.findConflictsInPropertyValues();

        TestObjectiveReader reader = new TestObjectiveReader(processor.getPropertyValue("to-read-path"));
        List<TestObjective> testObjectives = reader.readTestObjectives(ProxyReader.getLanguage());

        testObjectives.sort(Comparator.comparing(TestObjective::getFileName));

        Map<String, Pair<String, Integer>> proxyEntries = ProxyReader.readProxyEntries();

        Experiment experiment = new Experiment(testObjectives, proxyEntries);

        Solution<TestCase>[] result = program.start(proxy, experiment);

        Assertions.assertEquals(1, result.length);
        Assertions.assertEquals(1.0, result[0].getFitness());
        TestCase testCase = result[0].getSolution();
        Map<String, Pair<String, Integer>> inputs = testCase.getInputs();

        Assertions.assertEquals("</test>", inputs.get("UserName").getFirst());
        Assertions.assertEquals("0111", inputs.get("BankCode").getFirst());
        Assertions.assertEquals("0001User", inputs.get("RequestId").getFirst());
        Assertions.assertEquals("123456789123456", inputs.get("CardNumber").getFirst());
    }
}
