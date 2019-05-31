package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.*;

public class ReplicateNode implements LangNode {

    private String name;
    private String follow;

    private Map<String, String> targets;

    public ReplicateNode(String name, Map<String, String> targets, String follow) {
        this.name = name;
        this.targets = targets;
        this.follow = follow;
    }

    @Override
    public List<List<Pair<String, String>>> inject(List<Pair<String, Integer>> strings) {
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

            String forged = strings.get(i).getFirst() + parts[1] + " " + parts[0] + follow;

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

    @Override
    public String getName() {
        return name;
    }
}
