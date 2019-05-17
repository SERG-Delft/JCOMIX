package nl.tudelft.testexecutor.instances.single;

import jga.actors.Actor;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.IndividualPopulation;
import jga.populations.Population;

public class SingleActor implements Actor {

    @Override
    public void act(Environment environment, Population population) {
        IndividualPopulation individuals = population.toIndividualPopulation();

        double best = 10000d;

        double lengthDiff = 0;

        for (Individual individual : individuals) {
            if (individual.getFitness() == null) {
                continue;
            }

            if (individual.getFitness()[0] < best) {
                best = individual.getFitness()[0];
                lengthDiff = individual.getFitness()[1];
            }
        }

        if (best == 1) {
            environment.setSolutionFoundFlag(true);
        }

        if (lengthDiff < 1) {
            if (best < 0.5) {
                environment.getMutation().setAddMutationChance(0.4);
                environment.getMutation().setRemoveMutationChance(0.4);
                environment.getMutation().setPointMutationChance(0.1);
                environment.getMutation().setDeltaMutationChance(0.1);
            } else {
                environment.getMutation().setAddMutationChance(0.2);
                environment.getMutation().setRemoveMutationChance(0.2);
                environment.getMutation().setPointMutationChance(0.2);
                environment.getMutation().setDeltaMutationChance(0.4);
            }
        } else {
            if (best < 0.5) {
                environment.getMutation().setAddMutationChance(0.25);
                environment.getMutation().setRemoveMutationChance(0.25);
                environment.getMutation().setPointMutationChance(0.25);
                environment.getMutation().setDeltaMutationChance(0.25);
            } else {
                environment.getMutation().setAddMutationChance(0);
                environment.getMutation().setRemoveMutationChance(0);
                environment.getMutation().setPointMutationChance(0.45);
                environment.getMutation().setDeltaMutationChance(0.55);
            }
        }
    }
}
