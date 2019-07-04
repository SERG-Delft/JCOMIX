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

        System.out.println(targets.toString());
    }

    @Override
    public List<List<Pair<String, String>>> generateInjections(List<Pair<String, Integer>> strings) {
        strings.sort(Comparator.comparingInt(Pair::getSecond));

        List<List<Pair<String, String>>> permutations = new ArrayList<>();

        System.out.println(strings.toString());

        for (int i = 0; i < strings.size() - 2; i++) {
            List<Pair<String, String>> permutation = new ArrayList<>();

            String key = strings.get(i).getFirst();
            String closeTag = targets.get(key).split(key)[1];
            String value = key + closeTag + getSymbol();

            String keyReplacement = strings.get(i + 1).getFirst();
            String targetReplacement = targets.get(keyReplacement);

            String keyFinal = strings.get(i + 2).getFirst();
            String openTag = targets.get(keyFinal).split(keyFinal)[0];

            String valueFinal = getFollow() + targetReplacement.split(keyReplacement)[0] + injection + targetReplacement.split(keyReplacement)[1] + openTag + keyFinal;

            for (int j = 0; j < strings.size(); j++) {
                if (i == j) {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), value));
                } else if (i + 1 == j) {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), strings.get(j).getFirst())); // Stays the same
                } else if (i + 2 == j) {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), valueFinal));
                } else {
                    permutation.add(new Pair<>(strings.get(j).getFirst(), strings.get(j).getFirst())); // Anything else stays the same as well.
                }
            }
            System.out.println(permutation.toString());
            permutations.add(permutation);
        }

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
