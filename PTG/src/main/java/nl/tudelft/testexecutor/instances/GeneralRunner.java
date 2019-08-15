package nl.tudelft.testexecutor.instances;

import jga.environments.Environment;
import jga.factory.Approach;
import jga.factory.EnvironmentFactory;
import jga.factory.Operator;
import jga.populations.Population;
import jga.random.UniformRandom;
import jga.solutions.Solution;
import jga.utils.ExecutorPool;
import jga.utils.RandomUtil;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestExecutor;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.util.List;
import java.util.Map;

import static nl.tudelft.util.TextToEnum.*;

/**
 * Abstract version of a runner object.
 *
 * @author Dimitri Stallenberg
 */
public abstract class GeneralRunner {

    private TestExecutor executor;
    private Experiment experiment;

    private Map<String, String> properties;

    private Environment environment;

    /**
     * Constructor.
     *
     * @param executor   the executor object
     * @param experiment the experiment object
     * @param properties the properties map
     */
    public GeneralRunner(TestExecutor executor, Experiment experiment, Map<String, String> properties) {
        this.executor = executor;
        this.experiment = experiment;
        this.properties = properties;

        ExecutorPool.setNumThreads(Integer.parseInt(properties.get("threads")));

        final int seed = Integer.parseInt(properties.get("random-seed"));

        if (seed != 0) {
            RandomUtil.useRandom(new UniformRandom(seed));
        }

        environment = buildEnvironment(properties);
    }

    /**
     * This method creates a population appropriate for the environment.
     *
     * @return a newly created population
     */
    public abstract Population createPopulation();

    /**
     * This method builds the environment based on the properties gathered from the config file and arguments given to the program.
     *
     * @param properties the properties map
     * @return the created environment
     */
    protected Environment buildEnvironment(Map<String, String> properties) {
        EnvironmentFactory<TestObjective> factory = new EnvironmentFactory<>();

        Approach approach = getApproach(properties.get("approach"));
        Operator.Compare compare = getComparing(properties.get("compare"));
        Operator.Scoring score = getScoring(properties.get("score"));
        Operator.Crossover crossover = getCrossOver(properties.get("crossover"));
        Operator.Selection select = getSelection(properties.get("select"));
        Operator.Mutation mutate = getMutation(properties.get("mutate"));

        return factory.createEnvironment(approach, compare, score, crossover, select, mutate);
    }

    /**
     * This method must start the Genetic Algorithm to find and return solutions to the objectives.
     *
     * @return the solutions the GA found
     */
    public abstract Solution[] runGA();

    /**
     * This method will generate a test case based on the given dna strain.
     *
     * @param dna the dna strain
     * @return the generated test case
     */
    public TestCase generateTestCase(List<List<Character>> dna) {
        TestCase testCase = new TestCase(getExperiment().getProxyEntries());

        for (int j = 0; j < dna.size(); j++) {
            StringBuilder solution = new StringBuilder();
            for (Character gene : dna.get(j)) {
                solution.append(gene);
            }
            testCase.setInputField(j, solution.toString());
        }

        return testCase;
    }

    /**
     * Gets the executor object.
     *
     * @return the executor object
     */
    protected TestExecutor getExecutor() {
        return executor;
    }

    /**
     * Gets the experiment object.
     *
     * @return the experiment object
     */
    protected Experiment getExperiment() {
        return experiment;
    }

    /**
     * Gets the properties map.
     *
     * @return the properties map
     */
    protected Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Gets the environment object.
     *
     * @return the environment object
     */
    protected Environment getEnvironment() {
        return environment;
    }
}
