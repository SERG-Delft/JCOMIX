package nl.tudelft.testexecutor.instances.distances;

import nl.tudelft.testexecutor.Alphabet;

// some code taken from http://people.cs.pitt.edu/~kirk/cs1501/Pruhs/Fall2006/Assignments/editdistance/Levenshtein%20Distance.htm
@SuppressWarnings("PMD")
public class LinearDistance extends StringDistance {

    public LinearDistance(Alphabet alphabet) {
        super(alphabet);
    }

    public static double normalize(double x) {
        return x / (x + 1);
    }

    // *****************************
    // Compute a real-coded version of the Levenshtein distance
    // *****************************
    @Override
    public double distance(String s, String t) {
        char s_i; // ith character of s
        char t_j; // jth character of t
        double cost; // cost

        if (s != null && t != null) {
            int minLength = Math.min(s.length(), t.length());
            int maxLength = Math.max(s.length(), t.length());
            double dist = maxLength - minLength;

            for (int i = 0; i < minLength; i++) {
                s_i = s.charAt(i);
                t_j = t.charAt(i);

                if (s_i == t_j) {
                    cost = 0;
                } else {
                    if (!getAlphabet().contains(t_j) || !getAlphabet().contains(s_i)) {
                        cost = Math.abs((s_i) - (t_j));
                    } else {
                        cost = Math.abs(getAlphabet().toIndex(s_i) - getAlphabet().toIndex(t_j));
                    }
                    cost = normalize(cost);
                }
                dist = dist + cost;
            }
            return dist;

        } else if (s == null && t != null) {
            return t.length();
        } else if (t == null && s != null) {
            return s.length();
        } else {
            return Double.MAX_VALUE;
        }

    }
}
