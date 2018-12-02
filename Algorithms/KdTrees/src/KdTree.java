import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree<Key extends Comparable<Key>, Value>
{
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node
    {
        Key key;
        Value value;
        Node left;
        Node right;
        boolean color;

        public Node(Key key, Value value, boolean color)
        {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node rotateLeft(Node h)
    {
        assert isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h)
    {
        assert isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h)
    {
        assert !isRed(h);
        assert isRed(h.left);
        assert isRed(h.right);
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    private boolean isRed(Node x)
    {
        if (x == null)
        {
            return false;
        }
        return x.color == RED;
    }

    private Value get(Key key)
    {
        Node x = root;
        while (x != null)
        {
            int cmp = key.compareTo(x.key);
            if (cmp < 0)
            {
                x = x.left;
            }
            else if (cmp > 0)
            {
                x = x.right;
            }
            else
            {
                return x.value;
            }
        }
        return null;
    }

    private Node put(Node h, Key key, Value value)
    {
        if (h == null)
        {
            return new Node(key, value, RED);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0)
        {
            h.left = put(h.left, key, value);
        }
        else if (cmp > 0)
        {
            h.right = put(h.right, key, value);
        }
        else
        {
            h.value = value;
        }

        if (isRed(h.right) && !isRed(h.left))
        {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left))
        {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right))
        {
            flipColors(h);
        }

        return h;
    }

    private void put(Key key, Value value)
    {
        root = put(root, key, value);
        root.color = BLACK;
    }

    int compare(Point2D p1, Point2D p2, int level)
    {
        if (level % 2 == 0)
        {
            return Point2D.X_ORDER.compare(p1, p2);
        }
        else
        {
            return Point2D.Y_ORDER.compare(p1, p2);
        }
    }

    public KdTree()
    {
        root = null;
    }

    public boolean isEmpty()                      // is the set empty?
    {

    }
    public int size()                         // number of points in the set
    {

    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {

    }
    public boolean contains(Point2D p)            // does the set contain point p?
    {

    }
    public void draw()                         // draw all points to standard draw
    {

    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {

    }
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {

    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

    }
}
