package nl.tudelft.tobuilder;

import java.util.List;

public class Modifier {

    public String modify(String text, List<Pair<String, Pair<String, String>>> pairs) {
        String modified = text;

        for (Pair<String, Pair<String, String>> pair : pairs) {
            String keyWord = pair.getFirst();
            String replacement = pair.getSecond().getSecond();
            String matcherString = pair.getSecond().getFirst();

            String toReplaceWith = matcherString.replace(keyWord, replacement);

            modified = modified.replace(matcherString, toReplaceWith);
        }

        return modified;
    }

}
