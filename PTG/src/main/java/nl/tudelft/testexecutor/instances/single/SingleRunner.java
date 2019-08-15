package nl.tudelft.testexecutor.instances.single;

import jga.datastructures.Alphabet;
import jga.individuals.GrammarIndividual;
import jga.individuals.Individual;
import jga.populations.Population;
import jga.solutions.Solution;
import jga.utils.ExecutorPool;
import nl.tudelft.factories.AlphabetFactory;
import nl.tudelft.factories.PopulationFactory;
import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.io.LogUtil;
import nl.tudelft.testexecutor.instances.GeneralRunner;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestExecutor;
import nl.tudelft.testexecutor.testing.TestObjective;
import nl.tudelft.util.Constants;

import java.util.*;

/**
 * This class is an extension of the general runner.
 * It will run one test objective at the time.
 *
 * @author Dimitri Stallenberg
 */
public class SingleRunner extends GeneralRunner {

    /**
     * Constructor.
     *
     * @param executor   the executor object
     * @param experiment the experiment object
     * @param properties the properties map
     */
    public SingleRunner(TestExecutor executor, Experiment experiment, Map<String, String> properties) {
        super(executor, experiment, properties);

        if (getProperties().get("use-stall-manager").matches("true")) {
            SingleStallManager stallManager = new SingleStallManager();
            getEnvironment().addActor(stallManager);
        }
    }

    @Override
    public Population createPopulation() {
        SingleProblem problem = new SingleProblem(getExecutor(), getExperiment());

        int configPopulation = Integer.parseInt(getProperties().get("population"));

        Alphabet<Character> alphabet = new Alphabet<>(new AlphabetFactory().getAlphabet(getProperties().get("restricted")));

        int amount = Integer.parseInt(getProperties().get("inputs"));

        int[] chromosomeSizes = new int[amount];

        for (int i = 0; i < amount; i++) {
            chromosomeSizes[i] = Integer.parseInt(getProperties().get("initial-chromosome-length"));
        }

        PopulationFactory factory = new PopulationFactory() {
            @Override
            public Individual createIndividual() {
                return new GrammarIndividual<>(problem, alphabet, chromosomeSizes, true);
            }
        };

        // TODO population type
        // This could be in the config however then the single Objective should be set to the appropriate index
        // AND the migration actor should be changed as it is now using islands

        if (getProperties().get("approach").contains("MULTI")) {
            return factory.generateMultiIndividualPopulation(configPopulation);
        } else if (getProperties().get("approach").contains("SIMPLE")) {
            return factory.generateIndividualPopulation(configPopulation);
        }

        return null;
    }

    @Override
    public Solution[] runGA() {
        long last = System.currentTimeMillis();
        double totalTime = ConfigReader.readTimeToMinutes(getProperties().get("time"));
        double timePerTo = 0;

        Solution[] resultSet = new Solution[getExperiment().getObjectives().size()];
        List<Integer> unsolved = new ArrayList<>();
        Population[] populations = new Population[getExperiment().getObjectives().size()];
        long[] timeTaken = new long[populations.length];

        for (int i = 0; i < getExperiment().getObjectives().size(); i++) {
            populations[i] = createPopulation();
        }

        while (System.currentTimeMillis() - last < totalTime * Constants.MILLIS_PER_MINUTE) {
            if (unsolved.isEmpty()) {
                reAddUnsolved(populations, unsolved);
                timePerTo = (totalTime - ((System.currentTimeMillis() - last) / Constants.MILLIS_PER_MINUTE));

                if (getProperties().get("budgeting").matches("true")) {
                    timePerTo = timePerTo / ((double) unsolved.size());
                }
            }

            // All TOs are solved
            if (unsolved.isEmpty()) {
                break;
            }

            int index = unsolved.remove(new Random().nextInt(unsolved.size()));
            executeOne(index, populations, timePerTo, resultSet, timeTaken);
        }

        ExecutorPool.getInstance().shutdown();
        return resultSet;
    }

    private void reAddUnsolved(Population[] populations, List<Integer> unsolved) {
        for (int i = 0; i < populations.length; i++) {
            if (populations[i] != null) {
                unsolved.add(i);
            }
        }
    }

    private void executeOne(int index, Population[] populations, double timePerTo, Solution[] resultSet, long[] timeTaken) {
        long startTimeTo = System.currentTimeMillis();

        TestObjective randomTO = getExperiment().getObjectives().get(index);

        getEnvironment().setSolutionFoundFlag(false);

        SingleReporter reporter = new SingleReporter(getEnvironment(), 1, randomTO.getFileName(), Integer.parseInt(getProperties().get("verbose-level")));
        getEnvironment().addActor(reporter);
        reporter.reportFunction();
        populations[index] = getEnvironment().executeTimeLimit(randomTO, populations[index], timePerTo);
        reporter.killThread();

        timeTaken[index] += System.currentTimeMillis() - startTimeTo;

        GrammarIndividual<Character, TestObjective, String> best =
                (GrammarIndividual<Character, TestObjective, String>) populations[index].toIndividualPopulation().get(0);

        List<List<Character>> dna = best.getDNA();

        if (getEnvironment().isSolutionFoundFlag()) {
            long totalTime = ((System.currentTimeMillis() - startTimeTo) + timeTaken[index]);
            resultSet[index] = new Solution<>(generateTestCase(dna), totalTime, best.getFitness()[0]);
            populations[index] = null;

            reportProgress(randomTO, dna, timeTaken[index], "Solved");
        } else {
            reportProgress(randomTO, dna, timeTaken[index], "Not Solved");
        }
    }

    private void reportProgress(TestObjective to, List<List<Character>> dna, long totalTime, String text) {
        if (Integer.parseInt(getProperties().get("verbose-level")) == 0) {
            return;
        }

        StringBuilder report = new StringBuilder(text);

        report.append("\n\t");
        report.append(to.getFileName());

        if (Integer.parseInt(getProperties().get("verbose-level")) == 2) {
            report.append("\nsolution:\n\t");

            for (List<Character> chromosome : dna) {
                for (Character gene : chromosome) {
                    report.append(gene);
                }
                report.append("\t");
            }

            report.append("\ntiming:\n\t");
            report.append((totalTime) / Constants.MILLIS_PER_SEC);
            report.append(" seconds\n");
        } else {
            report.append("\n");
        }

        LogUtil.getInstance().info(report.toString());

    }
}
