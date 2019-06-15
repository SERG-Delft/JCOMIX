package nl.tudelft.testexecutor.instances.multi;

import jga.datastructures.Alphabet;
import jga.individuals.GrammarIndividual;
import jga.individuals.Individual;
import jga.operators.mutation.AdaptiveFitnessBasedMutation;
import jga.operators.scoring.comparing.SingleObjectiveComparing;
import jga.populations.MultiIndividualPopulation;
import jga.populations.Population;
import jga.solutions.Solution;
import jga.utils.ExecutorPool;
import nl.tudelft.factories.AlphabetFactory;
import nl.tudelft.factories.PopulationFactory;
import nl.tudelft.io.readers.ConfigReader;
import nl.tudelft.testexecutor.instances.GeneralRunner;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.testexecutor.testing.TestCase;
import nl.tudelft.testexecutor.testing.TestExecutor;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.util.List;
import java.util.Map;

/**
 * This class is an extension of the general runner.
 * It will run multiple test objectives at the same time.
 *
 * @author Dimitri Stallenberg
 */
public class MultiRunner extends GeneralRunner {

    /**
     * Constructor.
     *
     * @param executor   the executor object
     * @param experiment the experiment object
     * @param properties the properties map
     */
    public MultiRunner(TestExecutor executor, Experiment experiment, Map<String, String> properties) {
        super(executor, experiment, properties);

        if (getEnvironment().getScoring().getComparing() instanceof SingleObjectiveComparing) {
            // Hierarchy index should be 0 because we use islandPopulation (just one layer deep)
            ((SingleObjectiveComparing) getEnvironment().getScoring().getComparing()).setHierarchyPathIndex(0);
        } else {

            // TODO we could also just override this by setting the compare here to singleobjective and give a heads up to the user
            throw new IllegalStateException("Does it make sense to use a multi runner with multi-objective compare? Explain why");
        }

        if (getEnvironment().getMutation() instanceof AdaptiveFitnessBasedMutation) {
            // Hierarchy index should be 0 because we use islandPopulation (just one layer deep)
            ((AdaptiveFitnessBasedMutation) getEnvironment().getMutation()).setHierarchyPathIndex(0);
        }

        if (getProperties().get("use-stall-manager").matches("true")) {
            MultiStallManager stallManager = new MultiStallManager(experiment.getObjectives(), getProperties());
            getEnvironment().addActor(stallManager);
        }

        if (getProperties().get("use-migration-actor").matches("true")) {
            MultiMigrationActor migrationActor = new MultiMigrationActor(getEnvironment());
            getEnvironment().addActor(migrationActor);
        }
    }

    @Override
    public Population createPopulation() {
        MultiProblem problem = new MultiProblem(getExecutor(), getExperiment(), getEnvironment());

        final int populationPerIsland = Integer.parseInt(getProperties().get("population")) / getExperiment().getObjectives().size();

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
        return factory.generateMultiIndividualPopulation(populationPerIsland, getExperiment().getObjectives().size());
    }

    @Override
    public Solution[] runGA() {
        double totalTime = ConfigReader.readTimeToMinutes(getProperties().get("time"));

        MultiIndividualPopulation population = (MultiIndividualPopulation) createPopulation();
        // TODO check  for population type

        MultiReporter multiReporter = new MultiReporter(getEnvironment(), 1, getExperiment());
        multiReporter.reportFunction();
        getEnvironment().addActor(multiReporter);

        population = (MultiIndividualPopulation) getEnvironment().executeTimeLimit(getExperiment().getObjectives(), population, totalTime);

        multiReporter.killThread();


        Solution[] resultSet = new Solution[getExperiment().getObjectives().size()];


        for (int i = 0; i < population.size(); i++) {
            GrammarIndividual<Character, List<TestObjective>, String> best =
                    (GrammarIndividual<Character, List<TestObjective>, String>) population.get(i).get(0);

            TestCase testCase = generateTestCase(best.getDNA());

            resultSet[i] = new Solution<>(testCase, 0, best.getFitness()[best.getHierarchyPath()[0]]);
        }

        ExecutorPool.getInstance().shutdown();
        return resultSet;
    }
}
