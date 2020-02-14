package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.*;

/**
 * This class implements the language injector.
 * It implements a replicate injection.
 *
 * @author Dimitri Stallenberg
 */
public class ReplicateNode extends ReplaceNode {

    private Map<String, String> targets;

    /**
     * Constructor.
     *
     * @param name the name of the injection
     * @param symbol the symbol to inject
     * @param targets the matcher strings
     */
    public ReplicateNode(String name, String symbol, Map<String, String> targets) {
        super(name, symbol);
        this.targets = targets;
    }

    @Override
    public List<List<Pair<String, String>>> generateInjections(List<Pair<String, Integer>> strings) {
        strings.sort(Comparator.comparingInt(Pair::getSecond));

        List<List<Pair<String, String>>> permutations = new ArrayList<>();

        for (int i = 0; i < strings.size() - 1; i++) {
            List<Pair<String, String>> permutation = new ArrayList<>();

            if (!targets.containsKey(strings.get(i).getFirst())) {
                // TODO
                throw new IllegalArgumentException("Error");
            }

            String matcher = targets.get(strings.get(i).getFirst());

            String[] parts = matcher.split(strings.get(i).getFirst());

            String forged = "";

            if (parts.length == 0) {
                forged = strings.get(i).getFirst() + getSymbol();
            } else {
                forged = strings.get(i).getFirst() + parts[1] + " " + parts[0] + getSymbol();
            }


            for (int j = 0; j < strings.size(); j++) {
                if (i == j) {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), forged));
                } else {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), strings.get(j).getFirst()));
                }
            }

            permutations.add(permutation);
        }

        return permutations;
    }
}
