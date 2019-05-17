package nl.tudelft.testexecutor.instances.distances;

import nl.tudelft.testexecutor.Alphabet;

public abstract class IStringDistance {

    private Alphabet alphabet;

    public IStringDistance(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public abstract double distance(String s, String t);

    public Alphabet getAlphabet() {
        return alphabet;
    }
}
