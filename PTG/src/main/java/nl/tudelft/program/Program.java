package nl.tudelft.program;

import jga.solutions.Solution;
import nl.tudelft.io.readers.ProxyReader;
import nl.tudelft.io.readers.TestObjectiveReader;
import nl.tudelft.io.writers.JUnitWriter;
import nl.tudelft.io.writers.JestWriter;
import nl.tudelft.proxy.HttpProcessor;
import nl.tudelft.tobuilder.Pair;
import nl.tudelft.io.*;
import nl.tudelft.io.writers.TestWriter;
import nl.tudelft.testexecutor.instances.GeneralRunner;
import nl.tudelft.testexecutor.instances.multi.MultiRunner;
import nl.tudelft.testexecutor.instances.single.SingleRunner;
import nl.tudelft.testexecutor.testing.*;
import nl.tudelft.util.Constants;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the program.
 * This basic version of a program runs in the command line without interface.
 *
 * @author Dimitri Stallenberg
 */
public class Program {

    private ArgumentProcessor argumentProcessor;

    /**
     * Constructor.
     *
     * @param argumentProcessor the argumentProcessor Object containing all the properties and arguments
     */
    public Program(ArgumentProcessor argumentProcessor) {
        this.argumentProcessor = argumentProcessor;
    }

    /**
     * Creates the HttpProcessor and the Experiment object to start the Runner.
     */
    public void start() {
        TestObjectiveReader reader = new TestObjectiveReader(getArgumentProcessor().getPropertyValue("to-read-path"));
        List<TestObjective> testObjectives = reader.readTestObjectives(ProxyReader.getLanguage());
        testObjectives.sort(Comparator.comparing(TestObjective::getFileName));

        Map<String, Pair<String, Integer>> proxyEntries = ProxyReader.readProxyEntries();

        HttpProcessor processor = new HttpProcessor(
                getArgumentProcessor().getPropertyValue("proxy-url"),
                getArgumentProcessor().getPropertyValue("connection"));

        Experiment experiment = new Experiment(testObjectives, proxyEntries);

        startRunner(processor, experiment);
    }

    /**
     * Creates the Runner and starts it.
     *
     * @param processor  the HttpProcessor object
     * @param experiment the Experiment object
     */
    private void startRunner(HttpProcessor processor, Experiment experiment) {
        TestExecutor iTestExecutor = new TestExecutor(processor);

        long last = System.currentTimeMillis();

        GeneralRunner runner;

        if (getArgumentProcessor().getPropertyValue("objective").equals("ONE")) {
            runner = new SingleRunner(iTestExecutor, experiment, getArgumentProcessor().getPropertyArgumentMap());

        } else {
            runner = new MultiRunner(iTestExecutor, experiment, getArgumentProcessor().getPropertyArgumentMap());
        }

        Solution[] resultSet = runner.runGA();

        TestWriter testWriter;

        if (getArgumentProcessor().getPropertyValue("test-suite").equals("junit")) {
            testWriter = new JUnitWriter(getArgumentProcessor().getPropertyArgumentMap());
        } else {
            testWriter = new JestWriter(getArgumentProcessor().getPropertyArgumentMap());
        }

        for (int i = 0; i < resultSet.length; i++) {
            if (resultSet[i] == null || resultSet[i].getFitness() != 1) {
                continue;
            }
            try {
                testWriter.writeTest(experiment.getObjectives().get(i), (TestCase) resultSet[i].getSolution());
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
        }

        logFinalReport(resultSet, last);
    }

    private void logFinalReport(Solution[] resultSet, long last) {
        StringBuilder overview = new StringBuilder("Overview: \n");
        overview.append("Amount of threads used: ")
                .append(getArgumentProcessor().getPropertyArgumentMap().get("threads"))
                .append("\nSeed used: ")
                .append(getArgumentProcessor().getPropertyArgumentMap().get("random-seed"))
                .append("\n");


        int solvedCount = 0;
        int unsolvedCount = 0;
        for (int i = 0; i < resultSet.length; i++) {
            if (resultSet[i] == null) {
                continue;
            }

            overview.append(i)
                    .append(" \t solution: ");

            Collection<Pair<String, Integer>> pairs = ((TestCase) resultSet[i].getSolution()).getInputs().values();

            for(Pair<String, Integer> pair : pairs) {
                overview.append(pair.getFirst())
                        .append(" ");
            }

            overview.append("\t fitness: ")
                    .append(resultSet[i].getFitness())
                    .append("\t time: ")
                    .append((resultSet[i].getTime() / Constants.MILLIS_PER_SEC))
                    .append("\n");

            if (resultSet[i].getFitness() != 1) {
                unsolvedCount++;
                continue;
            }
            solvedCount++;
        }
        StringBuilder text = new StringBuilder("\nSolved: ")
                .append("\t")
                .append(solvedCount)
                .append("\n")
                .append("Not solved: ")
                .append("\t")
                .append(unsolvedCount)
                .append('\n');

        overview.append("Total time: ")
                .append((System.currentTimeMillis() - last))
                .append("\n");

        text.append(overview);

        LogUtil.getInstance().info(text.toString());
    }

    /**
     * Sets a property given a field and a value.
     *
     * @param field the field name
     * @param value the new value
     */
    public void setProperty(String field, String value) {
        argumentProcessor.getPropertyArgumentMap().put(field, value);
    }

    /**
     * Gets sthe argument processor of the program.
     *
     * @return the argument processor object
     */
    public ArgumentProcessor getArgumentProcessor() {
        return argumentProcessor;
    }
}
