package nl.tudelft.testexecutor.instances.distances;

import nl.tudelft.testexecutor.Alphabet;

// some code taken from http://people.cs.pitt.edu/~kirk/cs1501/Pruhs/Fall2006/Assignments/editdistance/Levenshtein%20Distance.htm
public class RealCodedEditDistance extends IStringDistance {


    public RealCodedEditDistance(Alphabet alphabet) {
        super(alphabet);
    }

    public static double normalize(double x) {
        return x / (x + 1);
    }

    private double minimum(double a, double b, double c) {
        double mi;

        mi = a;
        if (b < mi) {
            mi = b;
        }

        if (c < mi) {
            mi = c;
        }
        return mi;

    }

    // *****************************
    // Compute a real-coded version of the Levenshtein distance
    // *****************************
    public double distance(String s, String t) {
        double[][] d; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        double cost; // cost

        if (s == null && t != null) {
            return t.length();
        }
        if (t == null && s != null) {
            return s.length();
        }
        if (s == null && t == null) {
            return Double.MAX_VALUE;
        }
        // Step 1

        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new double[n + 1][m + 1];

        // Step 2

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3

        for (i = 1; i <= n; i++) {

            s_i = s.charAt(i - 1);

            // Step 4

            for (j = 1; j <= m; j++) {

                t_j = t.charAt(j - 1);

                // Step 5

                if (s_i == t_j) {
                    cost = 0;
                } else {
                    //
                    if (!getAlphabet().contains(t_j) || !getAlphabet().contains(s_i)) {
                        cost = Math.abs((s_i) - (t_j));
                    } else {
                        cost = Math.abs(getAlphabet().toIndex(s_i) - getAlphabet().toIndex(t_j));
                    }
                    cost = normalize(cost);
                }

                // Step 6

                d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
                        d[i - 1][j - 1] + cost);

            }

        }

        // Step 7

        return d[n][m];
    }

}
