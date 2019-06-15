package nl.tudelft.testexecutor;

import java.util.EnumMap;

/**
 * This enum class contains an enum map that converts ENUMs to alphabet objects.
 *
 * @author Dimitri Stallenberg
 */
public enum AlphabetType {

    BINARY, OCTAL, DECIMAL, HEXADECIMAL, DNA, LOWERCASE, UPPERCASE, PROTEIN, BASE64, ASCII, EXTENDED_ASCII, UNICODE16, RESTRICTED, UNRESTRICTED;

    private static EnumMap<AlphabetType, Alphabet> map = buildEnumToAlphabetMap();

    private static EnumMap<AlphabetType, Alphabet> buildEnumToAlphabetMap() {
        final int asciiNum = 128;
        final int asciiExtendedNum = 256;
        final int unicode16Num = 65536;

        EnumMap<AlphabetType, Alphabet> map = new EnumMap<>(AlphabetType.class);
        map.put(BINARY, new Alphabet("01", BINARY));
        map.put(OCTAL, new Alphabet("01234567", OCTAL));
        map.put(DECIMAL, new Alphabet("0123456789", DECIMAL));
        map.put(HEXADECIMAL, new Alphabet("0123456789ABCDEF", HEXADECIMAL));
        map.put(DNA, new Alphabet("ACTG", DNA));
        map.put(LOWERCASE, new Alphabet("abcdefghijklmnopqrstuvwxyz", LOWERCASE));
        map.put(UPPERCASE, new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ", UPPERCASE));
        map.put(PROTEIN, new Alphabet("ACDEFGHIKLMNPQRSTVWY", PROTEIN));
        map.put(BASE64, new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", BASE64));
        map.put(ASCII, new Alphabet(asciiNum, ASCII));
        map.put(EXTENDED_ASCII, new Alphabet(asciiExtendedNum, EXTENDED_ASCII));
        map.put(UNICODE16, new Alphabet(unicode16Num, UNICODE16));
        map.put(RESTRICTED, new Alphabet(";'*~()#|! \"-/.1032547698:=<>CBEIHNRUacbedgfhkmlonqpsruitwvyx", RESTRICTED));
        map.put(UNRESTRICTED,
                new Alphabet(
                        " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-*/()_!~|%;&$@'#<>?.\"=:+,{}[]^",
                        UNRESTRICTED));

        return map;
    }

    /**
     * This method will get the appropriate alphabet based on the given enum.
     *
     * @param type the enum of the wanted alphabet
     * @return the wanted alphabet
     */
    public static Alphabet getAlphabet(AlphabetType type) {
        return map.get(type);
    }
}
