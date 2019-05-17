package nl.tudelft.testexecutor.testing;

import nl.tudelft.tobuilder.Pair;

import java.util.List;
import java.util.Map;

/**
 * This class represents the experiment object.
 * It holds certain knowledge about the experiment.
 *
 * @author Dimitri Stallenberg
 */
public class Experiment {

    private List<TestObjective> objectives;
    private Map<String, Pair<String, Integer>> servletEntries;

    private long startTime;

    /**
     * Constructor.
     *
     * @param objectives      the list of objectives to run in this experiment
     * @param servletEntries the Map of standard entries for the objectives
     */
    public Experiment(List<TestObjective> objectives, Map<String, Pair<String, Integer>> servletEntries) {
        this.objectives = objectives;
        this.servletEntries = servletEntries;

        this.startTime = System.currentTimeMillis();
    }

    /**
     * Gets the objectives to test.
     *
     * @return the list of objectives
     */
    public List<TestObjective> getObjectives() {
        return objectives;
    }

    /**
     * Gets the start time of the experiment.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the map of standard entries.
     *
     * @return the map of standard entries
     */
    public Map<String, Pair<String, Integer>> getServletEntries() {
        return servletEntries;
    }
}
