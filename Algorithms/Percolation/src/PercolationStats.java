/**
 * Created by oleg on 2/14/16.
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int T;
    private double[] x;


    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be > 0");
        }

        this.T = T;
        x = new double[T];

        int i, j, openedSites;
        int[][] grid;

        for (int t = 0; t < T; ++t) {

            Percolation perc = new Percolation(N);

            grid = new int[N][N];
            openedSites = 0;
            while (!perc.percolates()) {
                do {
                    i = StdRandom.uniform(1, N + 1);
                    j = StdRandom.uniform(1, N + 1);
                } while (grid[i - 1][j - 1] == 1);
                grid[i - 1][j - 1] = 1;
                perc.open(i, j);
                ++openedSites;
            }

            x[t] = (double) openedSites / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(x);
    }

    public double stddev() {
        return StdStats.stddev(x);
    }

    public double confidenceLo() {
        return StdStats.mean(x) - 1.96 * StdStats.stddev(x) / Math.sqrt(T);
    }

    public double confidenceHi() {
        return StdStats.mean(x) + 1.96 * StdStats.stddev(x) / Math.sqrt(T);
    }

    public static void main(String[] args) {
        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]),
                                                          Integer.parseInt(args[1]));
        System.out.println("mean: " + String.valueOf(percStats.mean()));
        System.out.println("stddev: " + String.valueOf(percStats.stddev()));
        System.out.println("95% confidence interval: " + String.valueOf(percStats.confidenceLo())
                           + ", "
                           + String.valueOf(percStats.confidenceHi()));
    }
}
