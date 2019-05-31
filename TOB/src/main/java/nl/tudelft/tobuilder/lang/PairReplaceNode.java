package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PairReplaceNode extends ReplaceNode {

    private String follow;
    private String injection;

    private Map<String, String> targets;

    public PairReplaceNode(String name, Map<String, String> targets, String symbol, String follow, String injection) {
        super(name, symbol);
        this.targets = targets;
        this.follow = follow;
        this.injection = injection;
    }

    @Override
    public List<List<Pair<String, String>>> inject(List<Pair<String, Integer>> strings) {
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

    public String getFollow() {
        return follow;
    }
}
