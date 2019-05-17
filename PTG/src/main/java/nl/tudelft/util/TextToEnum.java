package nl.tudelft.util;

import jga.factory.Approach;
import jga.factory.Operator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This utility class translates strings to enums that can be used by the factory classes of JGA.
 */
public final class TextToEnum {

    private static Map<String, Approach> approachMap = makeMap(Approach.values());
    private static Map<String, Operator.Compare> compareMap = makeMap(Operator.Compare.values());
    private static Map<String, Operator.Scoring> scoringMap = makeMap(Operator.Scoring.values());
    private static Map<String, Operator.Crossover> crossOverMap = makeMap(Operator.Crossover.values());
    private static Map<String, Operator.Selection> selectionMap = makeMap(Operator.Selection.values());
    private static Map<String, Operator.Mutation> mutationMap = makeMap(Operator.Mutation.values());

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private TextToEnum() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * Builds a string to enum map based on the enum values.
     *
     * @param values the enum values array
     * @param <T>    the enum class type
     * @return the mapping between strings and enums
     */
    private static <T extends Enum<T>> Map<String, T> makeMap(T[] values) {
        Map<String, T> map = new HashMap<>();

        for (T value : values) {
            map.put(value.toString(), value);
        }

        return map;
    }

    /**
     * Gets the approach enum belonging to the input text.
     *
     * @param text the name of the approach
     * @return the enum belonging to the name
     */
    public static Approach getApproach(String text) {
        if (approachMap.containsKey(text)) {
            return approachMap.get(text);
        }

        throw new IllegalArgumentException("Input is not a valid approach");
    }

    /**
     * Gets the comparing enum belonging to the input text.
     *
     * @param text the name of the comparing
     * @return the enum belonging to the name
     */
    public static Operator.Compare getComparing(String text) {
        if (compareMap.containsKey(text)) {
            return compareMap.get(text);
        }

        throw new IllegalArgumentException("Input is not a valid comparing operator!");
    }

    /**
     * Gets the scoring enum belonging to the input text.
     *
     * @param text the name of the scoring
     * @return the enum belonging to the name
     */
    public static Operator.Scoring getScoring(String text) {
        if (scoringMap.containsKey(text)) {
            return scoringMap.get(text);
        }

        throw new IllegalArgumentException("Input is not a valid scoring operator!");
    }

    /**
     * Gets the crossover enum belonging to the input text.
     *
     * @param text the name of the crossover
     * @return the enum belonging to the name
     */
    public static Operator.Crossover getCrossOver(String text) {
        if (crossOverMap.containsKey(text)) {
            return crossOverMap.get(text);
        }

        throw new IllegalArgumentException("Input is not a valid crossover operator!");
    }

    /**
     * Gets the selection enum belonging to the input text.
     *
     * @param text the name of the selection
     * @return the enum belonging to the name
     */
    public static Operator.Selection getSelection(String text) {
        if (selectionMap.containsKey(text)) {
            return selectionMap.get(text);
        }

        throw new IllegalArgumentException("Input is not a valid selection operator!");
    }

    /**
     * Gets the mutation enum belonging to the input text.
     *
     * @param text the name of the mutation
     * @return the enum belonging to the name
     */
    public static Operator.Mutation getMutation(String text) {
        if (mutationMap.containsKey(text)) {
            return mutationMap.get(text);
        }

        throw new IllegalArgumentException("Input is not a valid mutation operator!");
    }

    /**
     * Gets the options for the approach names.
     *
     * @return the Set of approach name options
     */
    public static Set<String> getApproachOptions() {
        return approachMap.keySet();
    }

    /**
     * Gets the options for the comparing names.
     *
     * @return the Set of comparing name options
     */
    public static Set<String> getCompareOptions() {
        return compareMap.keySet();
    }

    /**
     * Gets the options for the scoring names.
     *
     * @return the Set of scoring name options
     */
    public static Set<String> getScoringOptions() {
        return scoringMap.keySet();
    }

    /**
     * Gets the options for the crossover names.
     *
     * @return the Set of crossover name options
     */
    public static Set<String> getCrossOverOptions() {
        return crossOverMap.keySet();
    }

    /**
     * Gets the options for the selection names.
     *
     * @return the Set of selection name options
     */
    public static Set<String> getSelectionOptions() {
        return selectionMap.keySet();
    }

    /**
     * Gets the options for the mutation names.
     *
     * @return the Set of mutation name options
     */
    public static Set<String> getMutationOptions() {
        return mutationMap.keySet();
    }

    /**
     * Gets the entire approach map.
     * This is offered to allow custom extensions to the environments.
     *
     * @return the approach map
     */
    public static Map<String, Approach> getApproachMap() {
        return approachMap;
    }

    /**
     * Gets the entire comparing map.
     * This is offered to allow custom extensions to the operators.
     *
     * @return the comparing map
     */
    public static Map<String, Operator.Compare> getCompareMap() {
        return compareMap;
    }

    /**
     * Gets the entire scoring map.
     * This is offered to allow custom extensions to the operators.
     *
     * @return the scoring map
     */
    public static Map<String, Operator.Scoring> getScoringMap() {
        return scoringMap;
    }

    /**
     * Gets the entire crossover map.
     * This is offered to allow custom extensions to the operators.
     *
     * @return the crossover map
     */
    public static Map<String, Operator.Crossover> getCrossOverMap() {
        return crossOverMap;
    }

    /**
     * Gets the entire selection map.
     * This is offered to allow custom extensions to the operators.
     *
     * @return the selection map
     */
    public static Map<String, Operator.Selection> getSelectionMap() {
        return selectionMap;
    }

    /**
     * Gets the entire mutation map.
     * This is offered to allow custom extensions to the operators.
     *
     * @return the mutation map
     */
    public static Map<String, Operator.Mutation> getMutationMap() {
        return mutationMap;
    }
}
