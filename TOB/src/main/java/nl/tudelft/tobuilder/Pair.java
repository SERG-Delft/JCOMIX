package nl.tudelft.tobuilder;

public class Pair<TypeOne, TypeTwo> {

    private TypeOne first;
    private TypeTwo second;

    public Pair(TypeOne first, TypeTwo second) {
        this.first = first;
        this.second = second;
    }


    public TypeOne getFirst() {
        return first;
    }

    public void setFirst(TypeOne first) {
        this.first = first;
    }

    public TypeTwo getSecond() {
        return second;
    }

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
