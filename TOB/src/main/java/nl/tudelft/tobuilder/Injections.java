package nl.tudelft.tobuilder;

import nl.tudelft.tobuilder.lang.LangNode;

import java.util.List;

/**
 * This class contains the injections object.
 * It generates injections using the language injection objects.
 * It uses an iterator pattern to reduce the memory load.
 *
 * @author Dimitri Stallenberg
 */
public class Injections {

    private List<LangNode> injections;
    private int index;

    /**
     * Constructor.
     *
     * @param injections the list of language injection objects
     */
    public Injections(List<LangNode> injections) {
        this.injections = injections;
        this.index = 0;

        if (injections.isEmpty()) {
            throw new IllegalArgumentException("There should atleast be one injection!");
        }
    }

    /**
     * This method checks whether there is a next injection or not.
     *
     * @return whether there is a next injection or not
     */
    public boolean hasNext() {
        return injections.size() != index;
    }

    /**
     * This method gets the name of the previous injection.
     *
     * @return the name of the previous injection
     */
    public String lastName() {
        if (index == 0) {
            throw new IllegalArgumentException("There is no previous item!");
        }
        return injections.get(index - 1).getName();
    }

    /**
     * This method generates injections using the current injection object and returns those.
     *
     * @param orderedPositions the ordered list of matcher strings
     * @return the generated list of injections.
     */
    public List<List<Pair<String, String>>> getNext(List<Pair<String, Integer>> orderedPositions) {
        if (!hasNext()) {
            throw new IllegalArgumentException("There is no next item!");
        }

        LangNode injection = injections.get(index++);

        return injection.generateInjections(orderedPositions);
    }
}
