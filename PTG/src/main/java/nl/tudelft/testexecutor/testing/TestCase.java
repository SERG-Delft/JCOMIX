package nl.tudelft.testexecutor.testing;

import nl.tudelft.tobuilder.Pair;

import java.util.*;

/**
 * This class holds a testcase to submit to the web server.
 *
 * @author Dimitri Stallenberg
 */
public class TestCase {

    private Map<String, Pair<String, Integer>> inputs;

    /**
     * Constructor.
     *
     * @param servletEntries the map of standard entries
     */
    public TestCase(Map<String, Pair<String, Integer>> servletEntries) {
        inputs = new HashMap<>(servletEntries);
    }

    /**
     * This method sets the value to a certain field.
     *
     * @param field the field to set the value to
     * @param value the new value
     */
    public void setInputField(String field, String value) {
        if (!inputs.containsKey(field)) {
            // TODO throw exception?
            return;
        }

        // TODO dimitri ~ i dont know about all these sanitatisations i dont like it
        String sanitized = value.replaceAll("\\s+", " ");

        int index = inputs.get(field).getSecond();
        inputs.put(field, new Pair<>(sanitized, index));
    }

    /**
     * This method sets a value to a certain index of the fields array.
     *
     * @param index the index of the field to set the value to
     * @param value the new value
     */
    public void setInputField(int index, String value) {
        for (String field : inputs.keySet()) {
            if (inputs.get(field).getSecond() == index) {
                setInputField(field, value);
                return;
            }
        }
    }

    /**
     * Gets the map of inputs of this test case.
     *
     * @return the map of inputs
     */
    public Map<String, Pair<String, Integer>> getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (String key : getInputs().keySet()) {
            string.append(key)
                    .append(" : ")
                    .append(getInputs().get(key).getFirst())
                    .append("\n");
        }
        return string.toString();
    }

}
