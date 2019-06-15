package nl.tudelft.testexecutor.instances.multi;

import jga.environments.Environment;
import jga.individuals.Individual;
import jga.problems.Problem;
import nl.tudelft.io.LogUtil;
import nl.tudelft.testexecutor.Alphabet;
import nl.tudelft.testexecutor.AlphabetType;
import nl.tudelft.testexecutor.instances.distances.IStringDistance;
import nl.tudelft.testexecutor.instances.distances.RealCodedEditDistance;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestExecutor;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.util.List;

/**
 * This class represents the problem for the multi objective environment to solve.
 *
 *  @author Dimitri Stallenberg
 */
public class MultiProblem implements Problem<List<List<Character>>, List<TestObjective>, String> {

    private IStringDistance stringDistance;

    private TestExecutor executor;
    private Experiment experiment;

    private Environment environment;

    /**
     * Constructor.
     *
     * @param executor          the TestExecutor object to run the tests on
     * @param experiment        the Experiment object
     * @param environment       the Environment
     */
    public MultiProblem(TestExecutor executor, Experiment experiment, Environment environment) {
        this.executor = executor;
        this.experiment = experiment;
        this.environment = environment;

        // TODO make setting
        Alphabet alphabet = AlphabetType.getAlphabet(AlphabetType.RESTRICTED);

        this.stringDistance = new RealCodedEditDistance(alphabet);
    }

    @Override
    public String process(Individual individual, List<List<Character>> strings, List<TestObjective> objectives) {
        TestCase testCase = new TestCase(experiment.getProxyEntries());

        int mainTo = individual.getHierarchyPath()[0]; // TODO replace 0 by hierarchy index as argument for the constructor
        TestObjective to = objectives.get(mainTo);

        int mainInsert = to.getInsertLocation();

        if (mainInsert >= strings.size()) {
            for (int j = 0; j < strings.size() - 1; j++) {
                StringBuilder solution = new StringBuilder();
                for (Character ch : strings.get(j)) {
                    solution.append(ch);
                }
                testCase.setInputField(j, solution.toString());
            }
            StringBuilder solution = new StringBuilder();
            for (Character ch : strings.get(strings.size() - 1)) {
                solution.append(ch);
            }
            testCase.setInputField(mainInsert, solution.toString());
        } else {
            for (int j = 0; j < strings.size(); j++) {
                StringBuilder solution = new StringBuilder();
                for (Character ch : strings.get(j)) {
                    solution.append(ch);
                }
                testCase.setInputField(j, solution.toString());
            }
        }

        return executor.run(testCase);
    }

    @Override
    public double[] evaluate(Individual individual, String s, List<TestObjective> objectives) {
        int mainTo = individual.getHierarchyPath()[0];
        // TODO replace 0 by hierarchy index as argument for the constructor

        double[] fitnessArray = new double[objectives.size()];

        for (int i = 0; i < objectives.size(); i++) {
            if (i != mainTo) {
                fitnessArray[i] = 0;
                continue;
            }

            String t = objectives.get(i).getToText();
            fitnessArray[i] = 1d / (stringDistance.distance(s, t) + 1);

            if (fitnessArray[i] == 1) {
                if (environment.getSkipList().contains(i)) {
                    continue;
                    // TODO
//                    throw new IllegalStateException("Skip list already contains individual but still processed it!");
                }

                environment.getSkipList().add(i);
                LogUtil.getInstance().info("Added island: " + i + " to the skip list.");
            }
        }

        return fitnessArray;
    }
}
