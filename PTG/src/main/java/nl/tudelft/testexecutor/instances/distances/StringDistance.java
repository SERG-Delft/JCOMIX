package nl.tudelft.testexecutor.instances.distances;

import nl.tudelft.testexecutor.Alphabet;

/**
 * This abstract class describes the string distance fitness functions.
 *
 * @author Dimitri Stallenberg
 */
public abstract class StringDistance {

    private Alphabet alphabet;

    /**
     * Constructor.
     *
     * @param alphabet the used alphabet
     */
    public StringDistance(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * This method will calculate the distance between 2 strings.
     *
     * @param s the first string to compare
     * @param t the second string to compare
     * @return the distance between the strings
     */
    public abstract double distance(String s, String t);

    /**
     * This method will get the used alphabet.
     *
     * @return the alphabet object
     */
    public Alphabet getAlphabet() {
        return alphabet;
    }
}
