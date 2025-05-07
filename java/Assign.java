import java.util.ArrayList;

public class Assign {

    private static double safeLog(double x) {
        return x > 0 ? Math.log(x) : -1e9;
    }

    public static float Assign(int n, int e, int m, float[][] prob, ArrayList<Integer> result) {
        double[][] dp = new double[n + 1][e + 1];
        int[][] choice = new int[n + 1][e + 1];

        // Initialize
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= e; j++) {
                dp[i][j] = -1e9;
            }
        }
        dp[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= e; j++) {
                for (int x = 1; x <= m; x++) {
                    if (j >= x && dp[i - 1][j - x] > -1e9) {
                        double logProb = safeLog(prob[i - 1][x - 1]);
                        double candidate = dp[i - 1][j - x] + logProb;
                        if (candidate > dp[i][j]) {
                            dp[i][j] = candidate;
                            choice[i][j] = x;
                        }
                    }
                }
            }
        }

        // Reconstruct
        int rem = e;
        for (int i = n; i >= 1; i--) {
            int x = choice[i][rem];
            result.add(0, x);
            rem -= x;
        }

        return (float) Math.exp(dp[n][e]);
    }

    public static float Assign2(int n, int e, int m, float[][] prob, ArrayList<Integer> result) {
        float bestProb = 0f;
        ArrayList<Integer> bestAssign = new ArrayList<>();

        for (int i = -1; i < n; i++) {
            for (int j = i; j < n; j++) {
                float[][] modified = new float[n][m];
                for (int a = 0; a < n; a++) {
                    for (int b = 0; b < m; b++) {
                        float val = prob[a][b];
                        if (a == i || a == j) {
                            val = Math.min(val + 0.2f, Math.max(0.999f, val));
                        }
                        modified[a][b] = val;
                    }
                }

                int overloadCost = 0;
                if (i >= 0) overloadCost++;
                if (j >= 0 && j != i) overloadCost++;
                if (e < overloadCost) continue;

                ArrayList<Integer> temp = new ArrayList<>();
                float probVal = Assign(n, e - overloadCost, m, modified, temp);

                if (i >= 0) temp.set(i, -temp.get(i));
                if (j >= 0 && j != i) temp.set(j, -temp.get(j));

                if (probVal > bestProb) {
                    bestProb = probVal;
                    bestAssign = new ArrayList<>(temp);
                }
            }
        }

        result.addAll(bestAssign);
        return bestProb;
    }
}
