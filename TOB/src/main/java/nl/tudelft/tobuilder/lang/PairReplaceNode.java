package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This class implements the language injector.
 * It implements a pair replacement injection.
 *
 * @author Dimitri Stallenberg
 */
public class PairReplaceNode extends ReplaceNode {

    private String follow;
    private String injection;

    private Map<String, String> targets;

    /**
     * Constructor.
     *
     * @param name the name of the injection
     * @param targets the targets the injector should look for
     * @param symbol the symbol to inject
     * @param follow the second symbol to inject
     * @param injection the actual injection
     */
    public PairReplaceNode(String name, Map<String, String> targets, String symbol, String follow, String injection) {
        super(name, symbol);
        this.targets = targets;
        this.follow = follow;
        this.injection = injection;
    }

    @Override
    public List<List<Pair<String, String>>> generateInjections(List<Pair<String, Integer>> strings) {
        strings.sort(Comparator.comparingInt(Pair::getSecond));

        List<List<Pair<String, String>>> permutations = new ArrayList<>();




//
//        for (int i = 0; i < strings.size(); i++) {
//            for (int j = i + 1; j < strings.size(); j++) {
//                List<Pair<String, String>> permutation = new ArrayList<>();
//
//                for (int k = 0; k < strings.size(); k++) {
//                    if (i == k) {
//                        permutation.add(new Pair<>(strings.get(k).getFirst(), getSymbol()));
//                    } else if (j == k) {
//                        permutation.add(new Pair<>(strings.get(k).getFirst(), getFollow()));
//                    } else {
//                        permutation.add(new Pair<>(strings.get(k).getFirst(), strings.get(k).getFirst()));
//                    }
//                }
//
//                permutations.add(permutation);
//            }
//        }

        return permutations;
    }

    /**
     * This method will get the second injection symbol.
     *
     * @return the second injection symbol
     */
    public String getFollow() {
        return follow;
    }
}
