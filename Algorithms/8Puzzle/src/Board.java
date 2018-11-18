import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

public class Board
{
    private int[][] m_blocks;
    private int m_hamming;
    private int m_manhattan;
    private Board m_parent;

    private static Random s_random = new Random();

    private class Point
    {
        public int i;
        public int j;

        public Point(int i, int j)
        {
            this.i = i;
            this.j = j;
        }

        public boolean equals(Object other)
        {
            if (this == other)
            {
                return true;
            }

            if (other == null)
            {
                return false;
            }

            if (this.getClass() != other.getClass())
            {
                return false;
            }

            Point obj = (Point)other;
            return i == obj.i && j == obj.j;
        }

        public Point move(Point dir)
        {
            return new Point(i + dir.i, j + dir.j);
        }
    }

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    {
        m_blocks = blocks;
        m_hamming = -1;
        m_manhattan = -1;
        m_parent = null;
    }

    private Board(Board other)
    {
        m_blocks = other.m_blocks.clone();
        for (int i = 0; i != dimension(); ++i)
        {
            m_blocks[i] = other.m_blocks[i].clone();
        }
        m_hamming = other.m_hamming;
        m_manhattan = other.m_manhattan;
        m_parent = other.m_parent;
    }

    public int dimension()                 // board dimension n
    {
        return m_blocks.length;
    }

    public int hamming()                   // number of blocks out of place
    {
        if (m_hamming > -1)
        {
            return m_hamming;
        }

        m_hamming = 0;
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                if (!elemValid(i, j))
                {
                    ++m_hamming;
                }
            }
        }
        return m_hamming;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        if (m_manhattan > -1)
        {
            return m_manhattan;
        }

        m_manhattan = 0;
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                if (m_blocks[i][j] == 0)
                {
                    continue;
                }
                Point goal = getGoal(i, j);
                m_manhattan += Math.abs(goal.i - i);
                m_manhattan += Math.abs(goal.j - j);
            }
        }
        return m_manhattan;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return hamming() == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board other = new Board(this);
        Point[] points = getRandom();
        other.swap(points[0], points[1]);
        return other;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (this == y)
        {
            return true;
        }

        if (y == null)
        {
            return false;
        }

        if (this.getClass() != y.getClass())
        {
            return false;
        }

        Board obj = (Board)y;
        return m_blocks == obj.m_blocks;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Point empty = getEmpty();

        List<Point> moves = new LinkedList<Point>();
        if (empty.i > 0)
        {
            moves.add(new Point(-1, 0));
        }
        if (empty.i < dimension() - 1)
        {
            moves.add(new Point(1, 0));
        }
        if (empty.j > 0)
        {
            moves.add(new Point(0, -1));
        }
        if (empty.j < dimension() - 1)
        {
            moves.add(new Point(0, 1));
        }

        if (m_parent != null)
        {
            Point parentEmpty = m_parent.getEmpty();
            moves = moves.stream()
                    .filter(dir -> !empty.move(dir).equals(parentEmpty))
                    .collect(Collectors.toList());
        }

        LinkedList<Board> neighbors = new LinkedList<Board>();
        for (Point dir : moves)
        {
            neighbors.add(moveEmpty(empty, dir));
        }
        return neighbors;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(dimension()));
        sb.append(System.lineSeparator());
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                sb.append(Integer.toString(m_blocks[i][j]));
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private boolean elemValid(int i, int j)
    {
        return m_blocks[i][j] == 0 || m_blocks[i][j] == i * dimension() + j + 1;
    }

    private Point getGoal(int i, int j)
    {
        return new Point(
                (m_blocks[i][j] - 1) / dimension(),
                (m_blocks[i][j] - 1) % dimension());
    }

    private void swap(Point p1, Point p2)
    {
        int temp = m_blocks[p1.i][p1.j];
        m_blocks[p1.i][p1.j] = m_blocks[p2.i][p2.j];
        m_blocks[p2.i][p2.j] = temp;
        m_hamming = -1;
        m_manhattan = -1;
    }

    private Point getEmpty()
    {
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                if (m_blocks[i][j] == 0)
                {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalArgumentException("No empty block");
    }

    private Point[] getRandom()
    {
        int i1, j1, i2, j2;
        do
        {
            i1 = s_random.nextInt(dimension());
            j1 = s_random.nextInt(dimension());
            i2 = s_random.nextInt(dimension());
            j2 = s_random.nextInt(dimension());
        } while ((i1 == i2 && j1 == j2)
                || m_blocks[i1][j1] == 0
                || m_blocks[i2][j2] == 0);
        return new Point[]{new Point(i1, j1), new Point(i2, j2)};
    }

    private Board moveEmpty(Point empty, Point dir)
    {
        assert dir.i + dir.j == 1;

        Board other = new Board(this);
        other.swap(empty, empty.move(dir));
        other.m_parent = this;
        return other;
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        List<Board> list = new LinkedList<Board>();
        list.add(new Board(new int[][]{
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
        }));

        int N = 10;
        while (!list.isEmpty() && N-- > 0)
        {
            Board board = list.remove(0);
            board.printBoard();
            if (board.isGoal())
            {
                break;
            }
            for (Board neighbor : board.neighbors())
            {
                list.add(neighbor);
            }
        }
    }

    private void printBoard()
    {
        System.out.println("Board: ");
        System.out.print(toString());
        System.out.print("Dimensions: ");
        System.out.println(dimension());
        System.out.print("Hamming: ");
        System.out.println(hamming());
        System.out.print("Manhattan: ");
        System.out.println(manhattan());
        System.out.print("isGoal: ");
        System.out.println(isGoal());
        System.out.println();
    }
}
