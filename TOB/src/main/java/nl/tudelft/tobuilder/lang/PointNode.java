package nl.tudelft.tobuilder.lang;


import nl.tudelft.tobuilder.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class implements the language injector.
 * It implements a point injection.
 *
 * @author Dimitri Stallenberg
 */
public class PointNode extends ReplaceNode {

    /**
     * Constructor.
     *
     * @param name the name of the injection
     * @param symbol the symbol to inject
     */
    public PointNode(String name, String symbol) {
        super(name, symbol);
    }

    @Override
    public List<List<Pair<String, String>>> generateInjections(List<Pair<String, Integer>> strings) {
        strings.sort(Comparator.comparingInt(Pair::getSecond));

        List<List<Pair<String, String>>> permutations = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            List<Pair<String, String>> permutation = new ArrayList<>();

            for (int j = 0; j < strings.size(); j++) {
                if (i == j) {
                    String forged = strings.get(j).getFirst();
                    int mid = (forged.length() / 2) - 1;

                    forged = forged.substring(0, mid) + getSymbol() + forged.substring(mid + 1);

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
