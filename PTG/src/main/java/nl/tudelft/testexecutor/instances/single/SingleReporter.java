package nl.tudelft.testexecutor.instances.single;

import jga.actors.Actor;
import jga.actors.Reporter;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.IndividualPopulation;
import jga.populations.Population;
import nl.tudelft.io.LogUtil;

import java.util.Comparator;
import java.util.List;

public class SingleReporter extends Reporter implements Actor {

    private String objectiveName;

    private double startScore = -1;
    private double score = -1;

    private String solution;

    public SingleReporter(Environment environment, double reportingInterval, String objectiveName) {
        super(environment, reportingInterval);
        this.objectiveName = objectiveName;
    }

    @Override
    public void reportFunction() {
        if (startScore == -1 && score != -1) {
            startScore = score;
        }

        StringBuilder log = new StringBuilder()
                .append("Time passed: ")
                .append((System.currentTimeMillis() - getStartTime()) / 1000d)
                .append(" seconds\n");

        if (score != -1 && startScore != score) {
            long timeDiff = (System.currentTimeMillis() - getStartTime());
            double fitnessDiff = score - startScore;
            double fitnessPerMilli = fitnessDiff / ((double) timeDiff);

            double millisLeft = (1 - score) / fitnessPerMilli;

            double secondsLeft = millisLeft / 1000d;

            double elapsedPercentage = (Math.log10((timeDiff / 1000d)) / Math.log10(secondsLeft + (timeDiff / 1000d))) * 100d;

            log.append("Estimated elapsed time: ")
                    .append(String.format("%.2f", elapsedPercentage))
                    .append(" %\n")
                    .append("Estimated time left: ")
                    .append(String.format("%.2f", secondsLeft))
                    .append(" seconds\n");
        }

        log.append(objectiveName)
                .append(" :")
                .append(String.format("%.2f", score))
                .append(" : ")
                .append(solution)
                .append("\n")
                .append("Generation: ")
                .append(getEnvironment().getGen())
                .append("\n");
        LogUtil.getInstance().info(log.toString());
    }

    @Override
    public void act(Environment environment, Population population) {
        IndividualPopulation current = population.toIndividualPopulation();

        current.sort(Comparator.comparingInt(Individual::getScore));

        double[] fitness = current.get(0).getFitness();

        double newScore = fitness[0];

        List<List<Character>> DNA = (List<List<Character>>) current.get(0).getDNA();
        StringBuilder dna = new StringBuilder();

        for (List<Character> characters : DNA) {
            for (Character character : characters) {
                dna.append(character);
            }
            dna.append("\t");
        }

        score = newScore;
        solution = dna.toString();
    }
}
