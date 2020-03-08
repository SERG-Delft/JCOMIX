package system;

import jga.solutions.Solution;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.program.Flag;
import nl.tudelft.program.Program;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class JSONTOBuilderTest {

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

        File TOFile = new File("target/generated");
        File testFile = new File("target/generated");
        File configFile = new File("src/test/resources/config.json");
        File proxyFile = new File("src/test/resources/proxy.json");

        String[] args = new String[]{
                "--config", configFile.getAbsolutePath(),
                "--proxy", proxyFile.getAbsolutePath(),
                "-p", TOFile.getAbsolutePath(),
                "-f", testFile.getAbsolutePath(),
                "--build-tos",
        };

        Program program = processor.getProgram(args);

        Map<String, String> propertyArgumentsMap = ConfigReader.readConfig(flagMap);

        processor.initializeMaps(propertyArgumentsMap);
        processor.validateAndProcessArguments(args);
        processor.findConflictsInPropertyValues();

        List<TestObjective> testObjectives = new ArrayList<>();

        testObjectives.sort(Comparator.comparing(TestObjective::getFileName));

        Map<String, Pair<String, Integer>> proxyEntries = ProxyReader.readProxyEntries();

        Experiment experiment = new Experiment(testObjectives, proxyEntries);

        Solution<TestCase>[] result = program.start(proxy, experiment);

        Assertions.assertEquals(0, result.length);
    }
}
