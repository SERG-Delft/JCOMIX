package nl.tudelft.testexecutor.instances.distances;

import nl.tudelft.testexecutor.Alphabet;

public class SequenceAllignmentDistance extends IStringDistance {

    public SequenceAllignmentDistance(Alphabet alphabet) {
        super(alphabet);
    }

    @Override
    public double distance(String s, String t) {
        int[][] mem = new int[s.length() + 1][t.length() + 1];

        for (int i = 0; i <= s.length(); i++) {
            mem[i][0] = i;
        }

        for (int i = 0; i <= t.length(); i++) {
            mem[0][i] = i;
        }

        for (int j = 1; j <= t.length(); j++) {
            for (int i = 1; i <= s.length(); i++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    mem[i][j] = mem[i - 1][j - 1];
                } else {
                    mem[i][j] = 1 + mem[i - 1][j - 1];
                }
                mem[i][j] = Math.min(mem[i][j], Math.min(1 + mem[i - 1][j], 1 + mem[i][j - 1]));
            }
        }

        return mem[s.length()][t.length()];
    }
}
