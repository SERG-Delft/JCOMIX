package nl.tudelft.tobuilder;

import nl.tudelft.tobuilder.lang.LangNode;

import java.util.List;

public class Injections {

    private List<LangNode> injections;
    private int index;

    public Injections(List<LangNode> injections) {
        this.injections = injections;
        this.index = 0;

        if (injections.isEmpty()) {
            throw new IllegalArgumentException("There should atleast be one injection!");
        }
    }

    public boolean hasNext() {
        return injections.size() != index;
    }

    public String lastName() {
        if (index == 0) {
            throw new IllegalArgumentException("There is no previous item!");
        }
        return injections.get(index - 1).getName();
    }

    public List<List<Pair<String, String>>> getNext(List<Pair<String, Integer>> orderedPositions) {
        if (!hasNext()) {
            throw new IllegalArgumentException("There is no next item!");
        }

        LangNode injection = injections.get(index++);

        return injection.inject(orderedPositions);
    }
}
