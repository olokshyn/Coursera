import java.util.List;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree
{
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int size;

    private class Node
    {
        private Point2D point;
        private final RectHV box;
        private Node left;
        private Node right;
        private boolean color;

        private Node(Point2D point, boolean color, double xmin, double ymin, double xmax, double ymax)
        {
            this.point = point;
            box = new RectHV(xmin, ymin, xmax, ymax);
            this.color = color;
        }
    }

    public KdTree()
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        root = put(root, p, 0, 0, 0, 1, 1);
        root.color = BLACK;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        return get(p) != null;
    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root, 0);
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        List<Point2D> list = new LinkedList<Point2D>();
        range(root, rect, list);
        return list;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (root == null)
        {
            return null;
        }
        return nearest(root, p, root.point, 0);
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

    private int compare(Point2D p1, Point2D p2, int level)
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

    private Node get(Point2D point)
    {
        Node x = root;
        int level = 0;
        while (x != null)
        {
            int cmp = compare(point, x.point, level++);
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
                return x;
            }
        }
        return null;
    }

    private Node put(Node h, Point2D point, int level, double xmin, double ymin, double xmax, double ymax)
    {
        if (h == null)
        {
            ++size;
            return new Node(point, RED, xmin, ymin, xmax, ymax);
        }
        if (point.distanceSquaredTo(h.point) < 1e-5)
        {
            return h;
        }
        if (level % 2 == 0)
        {
            int cmp = Point2D.X_ORDER.compare(point, h.point);
            if (cmp < 0)
            {
                h.left = put(h.left, point, level + 1, xmin, ymin, h.point.x(), ymax);
            }
            else
            {
                h.right = put(h.right, point, level + 1, h.point.x(), ymin, xmax, ymax);
            }
        }
        else
        {
            int cmp = Point2D.Y_ORDER.compare(point, h.point);
            if (cmp < 0)
            {
                h.left = put(h.left, point, level + 1, xmin, ymin, xmax, h.point.y());
            }
            else
            {
                h.right = put(h.right, point, level + 1, xmin, h.point.y(), xmax, ymax);
            }
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

    private void draw(Node h, int level)
    {
        if (h == null)
        {
            return;
        }
        if (level % 2 == 0)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(h.point.x(), h.box.ymin(), h.point.x(), h.box.ymax());
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(h.box.xmin(), h.point.y(), h.box.xmax(), h.point.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        h.point.draw();

        draw(h.left, level + 1);
        draw(h.right, level + 1);
    }

    private void range(Node h, RectHV rect, List<Point2D> list)
    {
        if (h ==  null)
        {
            return;
        }
        if (rect.contains(h.point))
        {
            list.add(h.point);
        }
        if (rect.intersects(h.box))
        {
            range(h.left, rect, list);
            range(h.right, rect, list);
        }
    }

    private Point2D nearest(Node h, Point2D point, Point2D closest, int level)
    {
        if (h == null)
        {
            return closest;
        }
        if (h.point.distanceSquaredTo(point) < closest.distanceSquaredTo(point))
        {
            closest = h.point;
        }
        if (h.box.distanceSquaredTo(point) < closest.distanceSquaredTo(point))
        {
            int cmp = compare(point, h.point, level);
            if (cmp < 0)
            {
                closest = nearest(h.left, point, closest, level + 1);
                closest = nearest(h.right, point, closest, level + 1);
            }
            else
            {
                closest = nearest(h.right, point, closest, level + 1);
                closest = nearest(h.left, point, closest, level + 1);
            }
        }
        return closest;
    }
}
