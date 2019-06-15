package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.List;

/**
 * This interface describes a language injector object.
 * This class will create injections using matcher strings.
 *
 * @author Dimitri Stallenberg
 */
public interface LangNode {

    /**
     * This method will generate injections for the given matcher strings.
     *
     * @param strings the list of matcher strings
     * @return a list of generated injections
     */
    List<List<Pair<String, String>>> generateInjections(List<Pair<String, Integer>> strings);

    /**
     * This method will get the name of the injection.
     *
     * @return the injection name
     */
    String getName();
}
