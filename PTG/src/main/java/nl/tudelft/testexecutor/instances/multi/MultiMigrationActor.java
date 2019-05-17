package nl.tudelft.testexecutor.instances.multi;

import jga.actors.MigrationActor;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.MultiIndividualPopulation;
import jga.populations.MultiPopulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents the migration actor for the multi objective environment.
 * It will copy the genes of the individuals with the most improved solutions,
 * to every single other island to make co-evolution possible.
 *
 * @author Dimitri Stallenberg
 */
public class MultiMigrationActor extends MigrationActor {

    private Environment environment;
    private MultiIndividualPopulation previous;

    /**
     * Constructor.
     *
     * @param environment the environment to migrate individuals in
     */
    public MultiMigrationActor(Environment environment) {
        super(0);
        this.environment = environment;
    }

    @Override
    public void migrate(MultiPopulation population) {
        if (population.size() < 2) {
            return;
        }

        MultiIndividualPopulation currentPopulation = (MultiIndividualPopulation) population;

        if (previous == null) {
            previous = currentPopulation.deepCopy();
            return;
        }

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < currentPopulation.size(); i++) {
            indices.add(i);
        }

        for (int i = 0; i < currentPopulation.size(); i++) {
            previous.get(i).sort(Comparator.comparingInt(Individual::getScore));
            currentPopulation.get(i).sort(Comparator.comparingInt(Individual::getScore));
        }

        sortJumpers(currentPopulation, indices);

        final double tenPercent = 0.1d;

        int amountToSelect = (int) (indices.size() * tenPercent);

        migrateAmount(amountToSelect, indices, currentPopulation);

        currentPopulation.setMigrations(amountToSelect);
        previous = currentPopulation.deepCopy();
    }

    /**
     * This method sorts the list of indices of the jumpers.
     * The sorting is based on the amount of change the populations had.
     *
     * @param population    the actual population
     * @param jumpers       the list of indices
     */
    private void sortJumpers(MultiIndividualPopulation population, List<Integer> jumpers) {
        jumpers.sort((o1, o2) -> {
            Individual actual1 = population.get(o1).get(0);
            Individual previous1 = previous.get(o1).get(0);

            Individual actual2 = population.get(o2).get(0);
            Individual previous2 = previous.get(o2).get(0);

            double dif1 = actual1.getFitness()[o1] - previous1.getFitness()[o1];

            double dif2 = actual2.getFitness()[o2] - previous2.getFitness()[o2];

            return Double.compare(dif2, dif1);
        });
    }

    /**
     * This method actually migrates the given amount of individuals.
     *
     * @param amountToSelect        the amount of individuals to migrate
     * @param indices               the indices of the populations
     * @param currentPopulation     the population
     */
    private void migrateAmount(int amountToSelect, List<Integer> indices, MultiIndividualPopulation currentPopulation) {
        for (int j = 0; j < amountToSelect; j++) {
            int index = indices.get(j);
            Individual actual1 = currentPopulation.get(index).get(0);
            Individual previous1 = previous.get(index).get(0);

            double diff = actual1.getFitness()[index] - previous1.getFitness()[index];

            if (diff == 0) {
                continue;
            }
            for (int i = 0; i < currentPopulation.size(); i++) {
                if (environment.getSkipList().contains(i)) {
                    continue; // Skip islands where the solution has been found
                }
                if (index == i) {
                    continue; // skip its original island
                }
                Individual individual = currentPopulation.get(index).get(0).copy();
                individual.setDnaChanged(true); // TODO
                currentPopulation.get(i).add(individual);
            }
        }
    }

}
