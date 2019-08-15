package nl.tudelft.testexecutor.instances.single;

import jga.actors.Actor;
import jga.actors.Reporter;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.IndividualPopulation;
import jga.populations.Population;
import nl.tudelft.io.LogUtil;
import nl.tudelft.util.Constants;

import java.util.Comparator;
import java.util.List;

/**
 * This class represents the reporter for the single objective environment.
 *
 * @author Dimitri Stallenberg
 */
public class SingleReporter extends Reporter implements Actor {

    private String objectiveName;

    private double startScore = -1;
    private double score = -1;

    private String solution;

    private int verbose;

    /**
     * Constructor.
     *
     * @param environment       the environment the reporter reports on
     * @param reportingInterval the interval the reporter should report on
     * @param objectiveName     the name of the objective the reporter reports on
     * @param verbose           the verbose level
     */
    public SingleReporter(Environment environment, double reportingInterval, String objectiveName, int verbose) {
        super(environment, reportingInterval);
        this.objectiveName = objectiveName;
        this.verbose = verbose;
    }

    @Override
    public void reportFunction() {
        if (verbose == 0) {
            return;
        }

        if (startScore == -1 && score != -1) {
            startScore = score;
        }

        StringBuilder log = new StringBuilder()
                .append("\nTime passed: ")
                .append((System.currentTimeMillis() - getStartTime()) / Constants.MILLIS_PER_SEC)
                .append(" seconds\n");

        if (score != -1 && startScore != score) {
            calculateTimings(log);
        }

        if (verbose == 2) {
            log.append(objectiveName)
                    .append(" :")
                    .append(String.format("%.2f", score))
                    .append(" : ")
                    .append(solution)
                    .append("\n");
        }

        log.append("Generation: ")
                .append(getEnvironment().getGen())
                .append("\n");


        LogUtil.getInstance().info(log.toString());
    }

    private void calculateTimings(StringBuilder log) {
        long timeDiff = (System.currentTimeMillis() - getStartTime());
        double fitnessDiff = score - startScore;
        double fitnessPerMilli = fitnessDiff / ((double) timeDiff);

        double millisLeft = (1 - score) / fitnessPerMilli;

        double secondsLeft = millisLeft / Constants.TO_PERCENT_SCALE;

        double elapsedPercentage =
                (Math.log10((timeDiff / Constants.MILLIS_PER_SEC))
                        / Math.log10(secondsLeft + (timeDiff / Constants.MILLIS_PER_SEC)))
                        * Constants.TO_PERCENT_SCALE;

        log.append("Estimated elapsed time: ")
                .append(String.format("%.2f", elapsedPercentage))
                .append(" %\n")
                .append("Estimated time left: ")
                .append(String.format("%.2f", secondsLeft))
                .append(" seconds\n");
    }

    @Override
    public void act(Environment environment, Population population) {
        IndividualPopulation current = population.toIndividualPopulation();

        current.sort(Comparator.comparingInt(Individual::getScore));

        double[] fitness = current.get(0).getFitness();

        double newScore = fitness[0];

        List<List<Character>> chromosomes = (List<List<Character>>) current.get(0).getDNA();
        StringBuilder dnaString = new StringBuilder();

        for (List<Character> chromosome : chromosomes) {
            for (Character gene : chromosome) {
                dnaString.append(gene);
            }
            dnaString.append("\t");
        }

        score = newScore;
        solution = dnaString.toString();

        if (score == 1) {
            environment.setSolutionFoundFlag(true);
        }
    }
}
