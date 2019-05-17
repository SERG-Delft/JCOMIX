package nl.tudelft.factories;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the alphabet factory which will build list of chars based on strings.
 *
 * @author Dimitri Stallenberg
 */
public class AlphabetFactory {

    // TODO also make a factory for the other kind of alphabet

    /**
     * This method creates the alphabet based on a given property.
     *
     * @param restricted    the restricted property
     * @return              a list of characters
     */
    public List<Character> getAlphabet(String restricted) {
        List<Character> list = new ArrayList<>();
        String characters;

        if ("true".equals(restricted)) {
            characters = ";'*~()#|! \"-/.1032547698:=<>CBEIHNRUacbedgfhkmlonqpsruitwvyx";
        } else {
            characters = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-*/()_!~|%;&$@'#<>?.\"=:+,{}[]^";
        }

        for (char c : characters.toCharArray()) {
            list.add(c);
        }

        return list;
    }
}
