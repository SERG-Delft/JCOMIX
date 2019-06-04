package nl.tudelft.testexecutor.instances.single;

import jga.actors.Actor;
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
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestExecutor;
import nl.tudelft.testexecutor.testing.TestObjective;

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

        if (getProperties().get("use-result-actor").matches("true")) {
            Actor resultActor = new SingleActor();
            getEnvironment().addActor(resultActor);
        }

        if (getProperties().get("use-migration-actor").matches("true")) {
            // TODO
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
            chromosomeSizes[i] = 10; // TODO 10 should also be a property
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

        double timePerTo;

        if (getProperties().get("budgeting").matches("true")) {
            timePerTo = totalTime / ((double) getExperiment().getObjectives().size());
        } else {
            timePerTo = totalTime;
        }

        Solution[] resultSet = new Solution[getExperiment().getObjectives().size()];

        List<Integer> unsolved = new ArrayList<>();

        Population[] populations = new Population[getExperiment().getObjectives().size()];
        long[] timeTaken = new long[populations.length];

        for (int i = 0; i < getExperiment().getObjectives().size(); i++) {
            populations[i] = createPopulation();
        }

        while (System.currentTimeMillis() - last < totalTime * 60 * 1000) {
            long startTimeTo = System.currentTimeMillis();

            if (unsolved.isEmpty()) {
                for (int i = 0; i < populations.length; i++) {
                    if (populations[i] != null) {
                        unsolved.add(i);
                    }
                }

                double timeLeft = (totalTime - (System.currentTimeMillis() - last) / 60000d);

                if (getProperties().get("budgeting").matches("true")) {
                    timePerTo = timeLeft / ((double) unsolved.size());
                } else {
                    timePerTo = timeLeft;
                }
            }

            if (unsolved.isEmpty()) {
                break;
            }

            int index = unsolved.remove(new Random().nextInt(unsolved.size()));
            TestObjective randomTO = getExperiment().getObjectives().get(index);

            getEnvironment().setSolutionFoundFlag(false);

            SingleReporter reporter = new SingleReporter(getEnvironment(), 1, randomTO.getFileName());
            getEnvironment().addActor(reporter);
            reporter.reportFunction();
            populations[index] = getEnvironment().executeTimeLimit(randomTO, populations[index], timePerTo);
            reporter.killThread();

            GrammarIndividual<Character, TestObjective, String> best = (GrammarIndividual<Character, TestObjective, String>) populations[index].toIndividualPopulation().get(0);

            List<List<Character>> chromosome = best.getDNA();

            if (getEnvironment().isSolutionFoundFlag()) {
                TestCase testCase = new TestCase(getExperiment().getServletEntries());

                for (int j = 0; j < chromosome.size(); j++) {
                    StringBuilder solution = new StringBuilder();
                    for (Character ch : chromosome.get(j)) {
                        solution.append(ch);
                    }
                    testCase.setInputField(j, solution.toString());
                }

                resultSet[index] = new Solution<>(testCase, ((System.currentTimeMillis() - startTimeTo) + timeTaken[index]), best.getFitness()[0]);
                populations[index] = null;

                LogUtil.getInstance().info(randomTO.getFileName());

                StringBuilder solution = new StringBuilder();

                for (List<Character> characters : chromosome) {
                    for (Character ch : characters) {
                        solution.append(ch);
                    }
                    solution.append("\t");
                }
                LogUtil.getInstance().info(solution.toString());

                LogUtil.getInstance().info("" + (System.currentTimeMillis() - startTimeTo + timeTaken[index]) / 1000d);
                LogUtil.getInstance().info("Generations taken: " + getEnvironment().getGen());
            } else {
                timeTaken[index] += System.currentTimeMillis() - startTimeTo;
                LogUtil.getInstance().info("Not solved!");
                LogUtil.getInstance().info(randomTO.getFileName());

                StringBuilder solution = new StringBuilder();

                for (List<Character> characters : chromosome) {
                    for (Character ch : characters) {
                        solution.append(ch);
                    }
                    solution.append("\t");
                }
                LogUtil.getInstance().info(solution.toString());

                LogUtil.getInstance().info("" + (timeTaken[index]) / 1000d);
            }
        }

        ExecutorPool.getInstance().shutdown();

        return resultSet;
    }
}
