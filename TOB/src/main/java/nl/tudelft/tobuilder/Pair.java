package nl.tudelft.tobuilder;

/**
 * This class contains a general purpose tuple data structure.
 *
 * @param <TypeOne> the first class type of the pair
 * @param <TypeTwo> the second class type of the pair
 *
 * @author Dimitri Stallenberg
 */
public class Pair<TypeOne, TypeTwo> {

    private TypeOne first;
    private TypeTwo second;

    /**
     * Constructor.
     *
     * @param first the first object of the pair
     * @param second the second object of the pair
     */
    public Pair(TypeOne first, TypeTwo second) {
        this.first = first;
        this.second = second;
    }

    /**
     * This method gets the first object of the pair.
     *
     * @return the first object of the pair
     */
    public TypeOne getFirst() {
        return first;
    }

    /**
     * This method sets the first object of the pair.
     *
     * @param first the new first object
     */
    public void setFirst(TypeOne first) {
        this.first = first;
    }

    /**
     * This method gets the second object of the pair.
     *
     * @return the second object of the pair
     */
    public TypeTwo getSecond() {
        return second;
    }

    /**
     * This method sets the second object of the pair.
     *
     * @param second the new second object
     */
    public void setSecond(TypeTwo second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
