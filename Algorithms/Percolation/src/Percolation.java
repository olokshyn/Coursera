/**
 * Created by oleg on 2/14/16.
 */

//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private int[] grid;
    private WeightedQuickUnionUF wquf;
    private WeightedQuickUnionUF wqufNoBack;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be > 0");
        }
        this.N = N;
        grid = new int[N * N];
        wquf = new WeightedQuickUnionUF(N * N + 2);
        wqufNoBack = new WeightedQuickUnionUF(N * N + 1);
    }

    public void open(int i, int j) {
        int index = getIndex(i, j);
        if (grid[index] == 1) {
            return;
        }
        grid[index] = 1;
        if (i == 1) {
            wquf.union(index, N * N);
            wqufNoBack.union(index, N * N);
        }
        if (i == N) {
            wquf.union(index, N * N + 1);
        }
        if (testOpen(i - 1, j)) {
            wquf.union(index, getIndex(i - 1, j));
            wqufNoBack.union(index, getIndex(i - 1, j));
        }
        if (testOpen(i + 1, j)) {
            wquf.union(index, getIndex(i + 1, j));
            wqufNoBack.union(index, getIndex(i + 1, j));
        }
        if (testOpen(i, j - 1)) {
            wquf.union(index, getIndex(i, j - 1));
            wqufNoBack.union(index, getIndex(i, j - 1));
        }
        if (testOpen(i, j + 1)) {
            wquf.union(index, getIndex(i, j + 1));
            wqufNoBack.union(index, getIndex(i, j + 1));
        }
    }

    public boolean isOpen(int i, int j) {
        return grid[getIndex(i, j)] == 1;
    }

    public boolean isFull(int i, int j) {
        return wqufNoBack.connected(getIndex(i, j), N * N);
    }

    public boolean percolates() {
        return wquf.connected(N * N, N * N + 1);
    }

    private int getIndex(int i, int j) {
        if (i <= 0 || i > N || j <= 0 || j > N) {
            throw new IndexOutOfBoundsException("i or j is out of bounds");
        }
        return --i * N + --j;
    }

    private boolean testOpen(int i, int j) {
        return !(i <= 0 || i > N || j <= 0 || j > N) && grid[getIndex(i, j)] == 1;
    }

//    public static void main(String[] args) {
//        try {
//            File file = new File("./tests/input50.txt");
//            Scanner in = new Scanner(file);
//            int N = in.nextInt();
//
//            Percolation perc = new Percolation(N);
//
//            while (in.hasNextInt()) {
//                int i = in.nextInt();
//                int j = in.nextInt();
//                perc.open(i, j);
//            }
//            System.out.println("Percolates: " + String.valueOf(perc.percolates()));
//        }
//        catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        }
//    }

}
