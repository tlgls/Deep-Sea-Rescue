#include <vector>
#include <utility>
#include <cmath>
#include <algorithm>

using namespace std;

float safe_log(float x) {
    return x > 0 ? log(x) : -1e9;
}

// base case: maximize product of probabilities using dynamic programming
pair<vector<int>, float> assign(int N, int E, int M, float **prob) {
    vector<vector<float>> dp(N + 1, vector<float>(E + 1, -1e9));
    vector<vector<int>> choice(N + 1, vector<int>(E + 1, -1));

    dp[0][0] = 0;

    for (int i = 1; i <= N; ++i) {
        for (int e = 0; e <= E; ++e) {
            for (int x = 1; x <= M; ++x) {
                if (e >= x && dp[i - 1][e - x] > -1e9) {
                    float log_prob = safe_log(prob[i - 1][x - 1]);
                    float candidate = dp[i - 1][e - x] + log_prob;
                    if (candidate > dp[i][e]) {
                        dp[i][e] = candidate;
                        choice[i][e] = x;
                    }
                }
            }
        }
    }

    int rem = E;
    vector<int> result(N);
    for (int i = N; i >= 1; --i) {
        result[i - 1] = choice[i][rem];
        rem -= result[i - 1];
    }

    return make_pair(result, exp(dp[N][E]));
}

// bonus: try overload device on up to 2 vehicles
pair<vector<int>, float> assign2(int N, int E, int M, float **prob) {
    pair<vector<int>, float> best_result;
    float best_prob = 0;

    for (int i = -1; i < N; ++i) {
        for (int j = i; j < N; ++j) {
            float **mod_prob = new float*[N];
            for (int k = 0; k < N; ++k) {
                mod_prob[k] = new float[M];
                for (int q = 0; q < M; ++q) {
                    float val = prob[k][q];
                    if (k == i || k == j) {
                        val = min(val + 0.2f, max(0.999f, val));
                    }
                    mod_prob[k][q] = val;
                }
            }

            int overload_count = (i >= 0 ? 1 : 0) + (j >= 0 && j != i ? 1 : 0);
            if (E < overload_count) {
                for (int k = 0; k < N; ++k) delete[] mod_prob[k];
                delete[] mod_prob;
                continue;
            }

            auto result = assign(N, E - overload_count, M, mod_prob);
            float prob_value = result.second;

            if (i >= 0) result.first[i] = -result.first[i];
            if (j >= 0 && j != i) result.first[j] = -result.first[j];

            if (prob_value > best_prob) {
                best_result = result;
                best_prob = prob_value;
            }

            for (int k = 0; k < N; ++k) delete[] mod_prob[k];
            delete[] mod_prob;
        }
    }

    return best_result;
}
