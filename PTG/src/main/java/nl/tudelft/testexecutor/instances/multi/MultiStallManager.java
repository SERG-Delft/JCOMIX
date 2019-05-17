package nl.tudelft.testexecutor.instances.multi;

import jga.actors.Actor;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.IndividualPopulation;
import jga.populations.MultiIndividualPopulation;
import jga.populations.Population;
import nl.tudelft.io.LogUtil;
import nl.tudelft.testexecutor.testing.TestObjective;

import java.util.Comparator;
import java.util.List;

/**
 * This class is an implementation of an actor.
 * It will reset stalled populations.
 * Stalled means that a population hasn't made progress for a certain amount of generations.
 *
 * @author Dimitri Stallenberg
 */
public class MultiStallManager implements Actor {

    private MultiIndividualPopulation previous;

    private int[] stalledGens;

    private List<TestObjective> objectives;

    public MultiStallManager(List<TestObjective> objectives) {
        stalledGens = new int[objectives.size()];
        this.objectives = objectives;
    }

    @Override
    public void act(Environment environment, Population population) {
        if (population.size() < 2) {
            return;
        }

        MultiIndividualPopulation actual = (MultiIndividualPopulation) population;

        if (previous == null) {
            previous = actual.deepCopy();
            return;
        }

        for (int i = 0; i < actual.size(); i++) {
            if (environment.getSkipList().contains(i)) {
                stalledGens[i] = 0;
                continue; // Skip islands where the solution has been found
            }

            previous.get(i).sort(Comparator.comparingInt(Individual::getScore));
            actual.get(i).sort(Comparator.comparingInt(Individual::getScore));

            double prev = previous.get(i).get(0).getFitness()[i];
            double now = actual.get(i).get(0).getFitness()[i];
            double dif = now - prev;

            final double threshold = 0.5d;

            if (dif == 0 && now < threshold) {
                stalledGens[i]++;
            } else {
                stalledGens[i] = 0;
            }
        }

        for (int i = 0; i < stalledGens.length; i++) {
            if (stalledGens[i] > 1000) { // TODO make this value a property
                LogUtil.getInstance().info("Reset island: " + i);

                // Reset this island
                IndividualPopulation newIsland = new IndividualPopulation();
                for (int j = 0; j < actual.get(i).size(); j++) {
                    Individual individual = actual.get(i).get(j).copy();

                    int[] dnaSizes = new int[((List) individual.getDNA()).size()];
                    for (int k = 0; k < dnaSizes.length; k++) {
                        dnaSizes[k] = 10; // TODO make this value a property
                    }

                    individual.setDNA(individual.generateDNA(dnaSizes));
                    individual.processData(objectives);
                    newIsland.add(individual);
                }
                actual.set(i, newIsland);
            }
        }

        previous = actual.deepCopy();
    }
}
