import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{
    private List<Board> solution;

    private class Node implements Comparable<Node>
    {
        public Board board;
        public Node parent;
        public int manhattan;
        public int moves;

        public Node(Board board, Node parent)
        {
            this.board = board;
            this.parent = parent;
            manhattan = board.manhattan();
            moves = 0;
            Node n = parent;
            while (n != null)
            {
                ++moves;
                n = n.parent;
            }
        }

        public int priority()
        {
            return manhattan + moves;
        }

        public int compareTo(Node other)
        {
            int diff = priority() - other.priority();
            return diff;
        }
    }

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
        {
            throw new IllegalArgumentException();
        }

        solution = null;

        MinPQ<Node> nodes = new MinPQ<Node>();
        nodes.insert(new Node(initial, null));

        MinPQ<Node> nodesTwinned = new MinPQ<Node>();
        nodesTwinned.insert(new Node(initial.twin(), null));

        while (!nodes.isEmpty() && !nodesTwinned.isEmpty())
        {
            Node node = nodes.delMin();
            if (node.board.isGoal())
            {
                solution = new LinkedList<Board>();
                while (node != null)
                {
                    solution.add(node.board);
                    node = node.parent;
                }
                Collections.reverse(solution);
                break;
            }

            Node nodeTwinned = nodesTwinned.delMin();
            if (nodeTwinned.board.isGoal())
            {
                // No solution
                break;
            }

            for (Board neighbor : node.board.neighbors())
            {
                if (node.parent == null || !neighbor.equals(node.parent.board))
                {
                    nodes.insert(new Node(neighbor, node));
                }

            }
            for (Board neighbor : nodeTwinned.board.neighbors())
            {
                if (nodeTwinned.parent == null || !neighbor.equals(nodeTwinned.parent.board))
                {
                    nodesTwinned.insert(new Node(neighbor, nodeTwinned));
                }
            }
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return solution != null;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if (solution != null)
        {
            return solution.size() - 1;
        }
        return -1;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return solution;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
