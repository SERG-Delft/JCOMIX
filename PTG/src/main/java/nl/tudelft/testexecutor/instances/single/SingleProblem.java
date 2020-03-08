package nl.tudelft.testexecutor.instances.single;

import jga.individuals.Individual;
import jga.problems.Problem;
import nl.tudelft.io.ArgumentProcessor;
import nl.tudelft.testexecutor.Alphabet;
import nl.tudelft.testexecutor.AlphabetType;
import nl.tudelft.testexecutor.instances.distances.*;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestExecutor;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.util.List;
import java.util.Map;

/**
 * This class represents the problem for the single objective environment to solve.
 *
 *  @author Dimitri Stallenberg
 */
public class SingleProblem implements Problem<List<List<Character>>, TestObjective, String> {


    private StringDistance stringDistance;

    private TestExecutor executor;
    private Experiment experiment;

    /**
     * Constructor.
     *
     * @param executor          the TestExecutor object to run the tests on
     * @param experiment        the Experiment object
     */
    public SingleProblem(TestExecutor executor, Experiment experiment, Map<String, String> properties) {
        this.executor = executor;
        this.experiment = experiment;
        Alphabet alphabet = AlphabetType.getAlphabet(AlphabetType.UNRESTRICTED);

        if (properties.get("restricted").equals("true")) {
            alphabet = AlphabetType.getAlphabet(AlphabetType.RESTRICTED);
        }

        switch (properties.get("fitness-function")) {
            case "LINEAR_DISTANCE":
                this.stringDistance = new LinearDistance(alphabet);
                break;

            case "STRING_EDIT_DISTANCE":
                this.stringDistance = new StringEditDistance(alphabet);
                break;
            default:
                this.stringDistance = new RealCodedEditDistance(alphabet);
        }    }

    @Override
    public String process(Individual individual, List<List<Character>> strings, TestObjective object) {
        TestCase testCase = new TestCase(experiment.getProxyEntries());

        for (int j = 0; j < strings.size(); j++) {
            StringBuilder solution = new StringBuilder();
            for (Character ch : strings.get(j)) {
                solution.append(ch);
            }
            testCase.setInputField(j, solution.toString());
        }

        return executor.run(testCase);
    }

    @Override
    public double[] evaluate(Individual individual, String s, TestObjective object) {
        String t = object.getToText();

        double distance = stringDistance.distance(s, t);

        double lengthDiff = Math.abs(t.length() - s.length());

        return new double[]{1d / (distance + 1), 1d / (lengthDiff + 1)};
    }
}
