import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

public class Board
{
    private int[][] blocks;
    private int hamming;
    private int manhattan;
    private Board parent;

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

            Point obj = (Point) other;
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
        this.blocks = blocks.clone();
        for (int i = 0; i != dimension(); ++i)
        {
            this.blocks[i] = blocks[i].clone();
        }
        hamming = -1;
        manhattan = -1;
        parent = null;
    }

    private Board(Board other)
    {
        blocks = other.blocks.clone();
        for (int i = 0; i != dimension(); ++i)
        {
            blocks[i] = other.blocks[i].clone();
        }
        hamming = other.hamming;
        manhattan = other.manhattan;
        parent = other.parent;
    }

    public int dimension()                 // board dimension n
    {
        return blocks.length;
    }

    public int hamming()                   // number of blocks out of place
    {
        if (hamming > -1)
        {
            return hamming;
        }

        hamming = 0;
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                if (!elemValid(i, j))
                {
                    ++hamming;
                }
            }
        }
        return hamming;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        if (manhattan > -1)
        {
            return manhattan;
        }

        manhattan = 0;
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                if (blocks[i][j] == 0)
                {
                    continue;
                }
                Point goal = getGoal(i, j);
                manhattan += Math.abs(goal.i - i);
                manhattan += Math.abs(goal.j - j);
            }
        }
        return manhattan;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return manhattan() == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board other = new Board(this);
        Point[] points = null;
        int n = dimension() - 1;
        if (blocks[0][n] != 0 && blocks[n][0] != 0)
        {
            points = new Point[]{new Point(0, n), new Point(n, 0)};
        }
        else if (blocks[0][0] != 0 && blocks[n][n] != 0)
        {
            points = new Point[]{new Point(0, 0), new Point(n, n)};
        }
        assert points != null;
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

        Board obj = (Board) y;
        return Arrays.deepEquals(blocks, obj.blocks);
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
                sb.append(Integer.toString(blocks[i][j]));
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private boolean elemValid(int i, int j)
    {
        return blocks[i][j] == 0 || blocks[i][j] == i * dimension() + j + 1;
    }

    private Point getGoal(int i, int j)
    {
        return new Point(
                (blocks[i][j] - 1) / dimension(),
                (blocks[i][j] - 1) % dimension());
    }

    private void swap(Point p1, Point p2)
    {
        int temp = blocks[p1.i][p1.j];
        blocks[p1.i][p1.j] = blocks[p2.i][p2.j];
        blocks[p2.i][p2.j] = temp;
        hamming = -1;
        manhattan = -1;
    }

    private Point getEmpty()
    {
        for (int i = 0; i != dimension(); ++i)
        {
            for (int j = 0; j != dimension(); ++j)
            {
                if (blocks[i][j] == 0)
                {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalArgumentException("No empty block");
    }

    private Board moveEmpty(Point empty, Point dir)
    {
        assert dir.i + dir.j == 1;

        Board other = new Board(this);
        other.swap(empty, empty.move(dir));
        other.parent = this;
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

        int n = 10;
        while (!list.isEmpty() && n-- > 0)
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

        Board b1 = new Board(new int[][] {
                {6, 7, 2},
                {8, 3, 4},
                {1, 5, 0}
        });
        Board b2 = new Board(new int[][] {
                {6, 7, 2},
                {8, 3, 4},
                {1, 5, 0}
        });
        System.out.println(b1.equals(b2));
        System.out.println(((Object) b1).equals(b2));
        System.out.println(b1.equals((Object) b2));
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
