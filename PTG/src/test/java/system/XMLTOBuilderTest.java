package system;

import jga.solutions.Solution;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.io.readers.TestObjectiveReader;
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

public class XMLTOBuilderTest {

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

                StringBuilder result = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:lu=\"http://commonws/cetrel/lu\">\n");
                result.append("   <soapenv:Header/>\n");
                result.append("   <soapenv:Body>\n");
                result.append("      <lu:perform>\n");
                result.append("         <lu:resInput>\n");

                for (String s : order) {
                    for (NameValuePair pair : pairs) {
                        if (pair.getName().equals(s)) {
                            result.append("           <lu:");
                            result.append(s);
                            result.append(">");
                            result.append(pair.getValue());
                            result.append("</lu:");
                            result.append(s);
                            result.append(">\n");

                            break;
                        }
                    }
                }

                result.append("         </lu:resInput>\n");
                result.append("      </lu:perform>\n");
                result.append("   </soapenv:Body>\n");
                result.append("</soapenv:Envelope>\n");

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
        File proxyFile = new File("src/test/resources/proxyXML.json");

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
