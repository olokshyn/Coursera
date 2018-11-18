import java.lang.Comparable;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{
    private List<Board> m_solution;
    private int m_moves;

    private class Node implements Comparable<Node>
    {
        public Board board;
        public Node parent;
        public int moves;

        public Node(Board board, Node parent, int moves)
        {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
        }

        public int priority()
        {
            return board.manhattan() + moves;
        }

        public int compareTo(Node other)
        {
            return priority() - other.priority();
        }
    }

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
        {
            throw new IllegalArgumentException();
        }

        m_solution = null;
        m_moves = 0;

        MinPQ<Node> nodes = new MinPQ<Node>();
        nodes.insert(new Node(initial, null, m_moves));

        MinPQ<Node> nodes_twinned = new MinPQ<Node>();
        nodes_twinned.insert(new Node(initial.twin(), null, m_moves));

        while (!nodes.isEmpty() && !nodes_twinned.isEmpty())
        {
            Node node = nodes.delMin();
            if (node.board.isGoal())
            {
                m_solution = new LinkedList<Board>();
                while (node != null)
                {
                    m_solution.add(node.board);
                    node = node.parent;
                }
                Collections.reverse(m_solution);
                break;
            }

            Node node_twinned = nodes_twinned.delMin();
            if (node_twinned.board.isGoal())
            {
                // No solution
                break;
            }

            ++m_moves;
            for (Board neighbor : node.board.neighbors())
            {
                nodes.insert(new Node(neighbor, node, m_moves));
            }
            for (Board neighbor : node_twinned.board.neighbors())
            {
                nodes_twinned.insert(new Node(neighbor, node_twinned, m_moves));
            }
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return m_solution != null;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return m_moves;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return m_solution;
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
