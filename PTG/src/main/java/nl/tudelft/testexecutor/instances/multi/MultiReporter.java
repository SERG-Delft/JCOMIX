package nl.tudelft.testexecutor.instances.multi;

import jga.actors.Actor;
import jga.actors.Reporter;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.IndividualPopulation;
import jga.populations.MultiIndividualPopulation;
import jga.populations.Population;
import nl.tudelft.io.LogUtil;
import nl.tudelft.testexecutor.testing.Experiment;
import nl.tudelft.util.Constants;

import java.util.Comparator;
import java.util.List;

/**
 * This class represents the reporter for the multi objective environment.
 *
 * @author Dimitri Stallenberg
 */
public class MultiReporter extends Reporter implements Actor {

    private double[] previousScores;
    private double[] scores;

    private String[] solutions;

    private int correctCount;

    private long startTime;

    private Experiment experiment;
    private int prevGen;
    private long prevReport;

    private int verbose;

    /**
     * Constructor.
     *
     * @param environment       the environment to report on
     * @param reportingInterval the interval to report on
     * @param experiment        the experiment the to report on
     * @param verbose           the verbose level
     */
    public MultiReporter(Environment environment, double reportingInterval, Experiment experiment, int verbose) {
        super(environment, reportingInterval);
        this.startTime = System.currentTimeMillis();
        this.experiment = experiment;
        this.verbose = verbose;
    }

    @Override
    public void reportFunction() {
        if (verbose == 0) {
            return;
        }

        StringBuilder report = new StringBuilder("\n");

        if (verbose == 2) {
            report.append("Stage: ")
                    .append(getEnvironment().getStage())
                    .append("\n");
        }

        if (scores == null) {
            prevGen = getEnvironment().getGen();
            prevReport = System.currentTimeMillis();
            return;
        }

        double[] currentScores = scores.clone();

        if (previousScores == null) {
            previousScores = currentScores.clone();
        }

        if (verbose == 2) {
            reportToInformation(report, currentScores);
        }

        reportGeneralInformation(report, currentScores);

        LogUtil.getInstance().info(report.toString());

        prevGen = getEnvironment().getGen();
        prevReport = System.currentTimeMillis();
        previousScores = currentScores.clone();
        // TODO
    }

    private void reportToInformation(StringBuilder report, double[] currentScores) {
        double[] improvement = new double[currentScores.length];

        for (int i = 0; i < currentScores.length; i++) {
            improvement[i] = currentScores[i] - previousScores[i];
        }

        for (int i = 0; i < currentScores.length; i++) {
            report.append(i)
                    .append(" : ")
                    .append(experiment.getObjectives().get(i).getFileName())
                    .append(" : fitness: ")
                    .append(String.format("%.4f", currentScores[i]))
                    .append(" : improvement: ")
                    .append(String.format("%.4f", improvement[i]))
                    .append(" : Solution: ")
                    .append(solutions[i])
                    .append("\n");
        }
    }

    private void reportGeneralInformation(StringBuilder report, double[] currentScores) {
        double avgGenPerSec = ((double) getEnvironment().getGen()) / ((System.currentTimeMillis() - startTime) / Constants.MILLIS_PER_SEC);
        double genPerSec = ((double) (getEnvironment().getGen() - prevGen)) / ((System.currentTimeMillis() - prevReport) / Constants.MILLIS_PER_SEC);

        report.append("Generation: ")
                .append(getEnvironment().getGen())
                .append("\n")
                .append("Correct: ")
                .append(correctCount)
                .append(" / ")
                .append(currentScores.length)
                .append("\n")
                .append("Time: ")
                .append((System.currentTimeMillis() - startTime) / Constants.MILLIS_PER_SEC);
        if (verbose == 2) {
            report.append("\n")
                    .append("Generations per second: ")
                    .append(String.format("%.1f", genPerSec))
                    .append("\n")
                    .append("Average generations per second: ")
                    .append(String.format("%.1f", avgGenPerSec));
        }
    }

    @Override
    public void act(Environment environment, Population population) {
        MultiIndividualPopulation current = (MultiIndividualPopulation) population.deepCopy();

        double[] newscores = new double[current.size()];

        String[] newSolutions = new String[current.size()];

        correctCount = 0;

        for (int i = 0; i < current.size(); i++) {
            IndividualPopulation individuals = current.get(i);

            individuals.sort(Comparator.comparingInt(Individual::getScore));

            double[] fitness = individuals.get(0).getFitness();
            int[] hierarchy = individuals.get(0).getHierarchyPath();

            if (hierarchy[0] != i) {
                throw new IllegalStateException("Hierarchy tree of individual is incorrect!");
            }

            newscores[i] = fitness[i];

            if (newscores[i] == 1) {
                correctCount++;
            }

            newSolutions[i] = getDnaString(individuals.get(0));
        }

        scores = newscores;
        solutions = newSolutions;

        if (correctCount == scores.length) {
            environment.setSolutionFoundFlag(true);
        }
    }

    private String getDnaString(Individual individual) {
        List<List<Character>> dna = (List<List<Character>>) individual.getDNA();
        StringBuilder dnaString = new StringBuilder();

        for (List<Character> chromosome : dna) {
            for (Character gene : chromosome) {
                dnaString.append(gene);
            }
            dnaString.append("\t");
        }

        return dnaString.toString();
    }
}
