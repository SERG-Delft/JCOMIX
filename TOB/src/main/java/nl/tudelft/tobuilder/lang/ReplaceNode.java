package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReplaceNode implements LangNode {

    private String name;
    private String symbol;

    public ReplaceNode(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    @Override
    public List<List<Pair<String, String>>> inject(List<Pair<String, Integer>> strings) {
        strings.sort(Comparator.comparingInt(Pair::getSecond));

        List<List<Pair<String, String>>> permutations = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            List<Pair<String, String>> permutation = new ArrayList<>();

            for (int j = 0; j < strings.size(); j++) {
                if (i == j) {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), getSymbol()));
                } else {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), strings.get(j).getFirst()));
                }
            }

            permutations.add(permutation);
        }

        return permutations;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}
