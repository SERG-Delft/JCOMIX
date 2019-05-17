package nl.tudelft.factories;

import jga.individuals.Individual;
import jga.populations.IndividualPopulation;

import jga.populations.MultiIndividualPopulation;
import nl.tudelft.io.LogUtil;

/**
 * This class describes an abstract population factory.
 * The class generates different kinds of populations.
 *
 * @author Dimitri Stallenberg
 */
public abstract class PopulationFactory {

    /**
     * This method creates the individuals that belong to the population.
     *
     * @return an individual
     */
    public abstract Individual createIndividual();

    /**
     * This method calculates the optimal amount of sub populations in a multi population.
     *
     * @param totalSize     the total size of the population
     * @return              the optimal divider for the population
     */
    private int calculateOptimalPopulationDistribution(int totalSize) {
        int best = Integer.MAX_VALUE;
        int bestDivider = -1;
        for (int i = 2; i < totalSize / 2; i++) {
            if ((totalSize / i) * i != totalSize) {
                continue;
            }
            if (Math.abs(totalSize / i - i) < best) {
                bestDivider = i;
                best = Math.abs(totalSize / i - i);
            } else {
                break;
            }
        }

        if (bestDivider == -1) {
            LogUtil.getInstance().warning("Prime numbers don't work as population size!");
            System.exit(0);
        }

        return bestDivider;
    }

    /**
     * This method generates a multi individual population.
     * It calculates its own optimal amount of sub populations.
     *
     * @param totalSize     the total size of the population
     * @return              the generated multi population
     */
    public MultiIndividualPopulation generateMultiIndividualPopulation(int totalSize) {
        int populationPerIsland = calculateOptimalPopulationDistribution(totalSize);
        int islands = totalSize / populationPerIsland;
        return generateMultiIndividualPopulation(populationPerIsland, islands);
    }

    /**
     * This method generates a multi individual population.
     *
     * @param populationPerIsland   the size of the population per island
     * @param islands               the amount of islands
     * @return                      the generated multi population
     */
    public MultiIndividualPopulation generateMultiIndividualPopulation(int populationPerIsland, int islands) {
        if (populationPerIsland < 2) {
            LogUtil.getInstance().warning("Population is too small for amount of problems. \n"
                    + "Population should at least be twice as big as the amount of problems.");
            System.exit(1);
        }

        MultiIndividualPopulation populations = new MultiIndividualPopulation();

        for (int i = 0; i < islands; i++) {
            IndividualPopulation individuals = new IndividualPopulation();
            for (int j = 0; j < populationPerIsland; j++) {
                individuals.add(createIndividual());
            }
            populations.add(individuals);
        }

        return populations;
    }

    /**
     * This method generates a individual population.
     *
     * @param populationSize    the size of the population
     * @return                  the generated population
     */
    public IndividualPopulation generateIndividualPopulation(int populationSize) {
        if (populationSize < 2) {
            LogUtil.getInstance().warning("Population is too small for amount of problems. \n"
                    + "Population should at least be twice as big as the amount of problems.");
            System.exit(1);
        }

        IndividualPopulation populations = new IndividualPopulation();

        for (int j = 0; j < populationSize; j++) {
            populations.add(createIndividual());
        }

        return populations;
    }
}
