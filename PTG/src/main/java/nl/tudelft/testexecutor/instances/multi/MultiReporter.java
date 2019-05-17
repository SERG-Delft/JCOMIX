package nl.tudelft.testexecutor.instances.multi;

import jga.actors.Actor;
import jga.actors.Reporter;
import jga.environments.Environment;
import jga.individuals.Individual;
import jga.populations.MultiIndividualPopulation;
import jga.populations.Population;
import nl.tudelft.io.LogUtil;
import nl.tudelft.testexecutor.testing.Experiment;

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

    /**
     * Constructor.
     *
     * @param environment the environment to report on
     * @param reportingInterval the interval to report on
     * @param experiment the experiment the to report on
     */
    public MultiReporter(Environment environment, double reportingInterval, Experiment experiment) {
        super(environment, reportingInterval);
        this.startTime = System.currentTimeMillis();
        this.experiment = experiment;
    }

    @Override
    public void reportFunction() {
        StringBuilder information = new StringBuilder("\n");
        information.append("Stage: ")
                .append(getEnvironment().getStage())
                .append("\n");

        if (scores == null) {
            prevGen = getEnvironment().getGen();
            prevReport = System.currentTimeMillis();
            return;
        }

        double[] currentScores = scores.clone();

        if (previousScores == null) {
            previousScores = currentScores.clone();
        }

        double[] improvement = new double[currentScores.length];

        for (int i = 0; i < currentScores.length; i++) {
            improvement[i] = currentScores[i] - previousScores[i];
        }

        for (int i = 0; i < currentScores.length; i++) {
            information.append(i)
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

        final double millisPerSec = 1000d;

        double avgGenPerSec = ((double) getEnvironment().getGen()) / ((System.currentTimeMillis() - startTime) / millisPerSec);
        double genPerSec = ((double) (getEnvironment().getGen() - prevGen)) / ((System.currentTimeMillis() - prevReport) / millisPerSec);

        prevGen = getEnvironment().getGen();
        prevReport = System.currentTimeMillis();

        information.append("Generation: ")
                .append(getEnvironment().getGen())
                .append("\n")
                .append("Correct: ")
                .append(correctCount)
                .append(" / ")
                .append(currentScores.length)
                .append("\n")
                .append("Time: ")
                .append((System.currentTimeMillis() - startTime) / millisPerSec)
                .append("\n")
                .append("Generations per second: ")
                .append(String.format("%.1f", genPerSec))
                .append("\n")
                .append("Average generations per second: ")
                .append(String.format("%.1f", avgGenPerSec));

        LogUtil.getInstance().info(information.toString());

        previousScores = currentScores.clone();
        // TODO
    }

    @Override
    public void act(Environment environment, Population population) {
        MultiIndividualPopulation current = (MultiIndividualPopulation) population.deepCopy();

        double[] newscores = new double[current.size()];

        String[] newSolutions = new String[current.size()];

        correctCount = 0;

        for (int i = 0; i < current.size(); i++) {
            current.get(i).sort(Comparator.comparingInt(Individual::getScore));

            double[] fitness = current.get(i).get(0).getFitness();
            int[] hierarchy = current.get(i).get(0).getHierarchyPath();

            if (hierarchy[0] != i) {
                throw new IllegalStateException("Hierarchy tree of individual is incorrect!");
            }

            newscores[i] = fitness[i]; // TODO make argument or something

            if (newscores[i] == 1) {
                correctCount++;
            }

            List<List<Character>> dnaList = (List<List<Character>>) current.get(i).get(0).getDNA();
            StringBuilder dnaString = new StringBuilder();

            for (List<Character> characters : dnaList) {
                for (Character character : characters) {
                    dnaString.append(character);
                }
                dnaString.append("\t");
            }
            newSolutions[i] = dnaString.toString();
        }

        scores = newscores;
        solutions = newSolutions;


        if (correctCount == scores.length) {
            environment.setSolutionFoundFlag(true);
        }
    }
}
