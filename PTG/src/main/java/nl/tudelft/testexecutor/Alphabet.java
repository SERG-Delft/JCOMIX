package nl.tudelft.testexecutor;

/*************************************************************************
 * Compilation: javac Alphabet.java Execution: java Alphabet
 *
 * A data type for alphabets, for use with string-processing code that must
 * convert between an alphabet of size R and the integers 0 through R-1.
 *
 * Warning: supports only the basic multilingual plane (BMP), i.e, Unicode
 * characters between U+0000 and U+FFFF.
 *
 * http://algs4.cs.princeton.edu/55compression/Alphabet.java.html
 *************************************************************************/
@SuppressWarnings("PMD")
public class Alphabet {

    private AlphabetType type;

    private char[] alphabet; // the characters in the alphabet
    private int[] inverse; // indices
    private int R; // the radix of the alphabet

    // Create a new Alphabet from sequence of characters in string.
    public Alphabet(String alpha, AlphabetType type) {
        this.type = type;

        // check that alphabet contains no duplicate chars
        boolean[] unicode = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < alpha.length(); i++) {
            char c = alpha.charAt(i);
            if (unicode[c]) {
                throw new IllegalArgumentException(
                        "Illegal alphabet: repeated character = '" + c + "'");
            }
            unicode[c] = true;
        }

        alphabet = alpha.toCharArray();
        R = alpha.length();
        inverse = new int[Character.MAX_VALUE];
        for (int i = 0; i < inverse.length; i++) {
            inverse[i] = -1;
        }

        // can't use char since R can be as big as 65,536
        for (int c = 0; c < R; c++) {
            inverse[alphabet[c]] = c;
        }
    }

    // Create a new Alphabet of Unicode chars 0 to R-1
    public Alphabet(int R, AlphabetType type) {
        this.type = type;
        alphabet = new char[R];
        inverse = new int[R];
        this.R = R;

        // can't use char since R can be as big as 65,536
        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }
        for (int i = 0; i < R; i++) {
            inverse[i] = i;
        }
    }

    // Create a new Alphabet of Unicode chars 0 to 255 (extended ASCII)
    public Alphabet() {
        this(256, AlphabetType.EXTENDED_ASCII);
    }

    // is character c in the alphabet?
    public boolean contains(char c) {
        return inverse[c] != -1;
    }

    // convert c to index between 0 and R-1.
    public int toIndex(char c) {
        if (c < 0 || c >= inverse.length || inverse[c] == -1) {
            throw new IllegalArgumentException("Character " + c
                    + " not in alphabet");
        }
        return inverse[c];
    }
}
