package nl.tudelft.testexecutor.testing;

import nl.tudelft.proxy.Proxy;
import nl.tudelft.tobuilder.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class describes the abstract form of a TestExecutor.
 *
 * @author Dimitri Stallenberg
 */
public class TestExecutor {

    private Proxy proxy;

    /**
     * Constructor.
     *
     * @param proxy the proxy to submit the testcase to.
     */
    public TestExecutor(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * This method executes a test case and returns the observed output string (XML) for fitness measurement.
     *
     * @param test the test case to be tested
     * @return the resulting string
     */
    public String run(TestCase test) {
        List<NameValuePair> pairs = generatePairs(test);
        return submit(pairs);
    }

    /**
     * This method generates the name value pairs based on the test case object.
     *
     * @param testCase the test case to generate name value pairs for
     * @return a list of name value pairs
     */
    private List<NameValuePair> generatePairs(TestCase testCase) {
        Map<String, Pair<String, Integer>> map = testCase.getInputs();

        List<NameValuePair> pairs = new ArrayList<>();

        for (String field : map.keySet()) {
            pairs.add(new BasicNameValuePair(field, map.get(field).getFirst()));
        }

        return pairs;
    }

    /**
     * This method submits the name value pairs to the processor and returns the result.
     *
     * @param pairs the name value pairs
     * @return the result of the web server
     */
    private String submit(List<NameValuePair> pairs) {
        String output = proxy.submit(pairs);

        return sanitise(output);
    }

    /**
     * This method sanitizes strings.
     * It removes all whitespaces.
     *
     * @param dirtyString the string to sanitise
     * @return the sanitized string
     */
    private String sanitise(String dirtyString) {
        // TODO remove possibly
        return dirtyString.replaceAll("\\s+", " ");
    }
}
