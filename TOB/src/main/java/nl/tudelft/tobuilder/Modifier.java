package nl.tudelft.tobuilder;

import java.util.List;

/**
 * This final class contains several utility functions for reading/writing/copying files.
 *
 * @author Dimitri Stallenberg
 */
public final class Modifier {

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private Modifier() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }

    /**
     * This static method modifies a given text using matcher strings and injections.
     *
     * @param text the text to modify.
     * @param pairs the list of pairs of matcher strings and injections
     * @return the modified text
     */
    public static String modify(String text, List<Pair<String, Pair<String, String>>> pairs) {
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
