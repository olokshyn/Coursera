import java.util.List;
import java.util.LinkedList;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET
{
    private SET<Point2D> points;

    public PointSET()                               // construct an empty set of points
    {
        points = new SET<Point2D>();
    }
    public boolean isEmpty()                      // is the set empty?
    {
        return points.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return points.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null)
        {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }
    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
        {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D point : points)
        {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
        {
            throw new IllegalArgumentException();
        }
        List<Point2D> inside = new LinkedList<Point2D>();
        for (Point2D point : points)
        {
            if (       point.x() >= rect.xmin()
                    && point.x() <= rect.xmax()
                    && point.y() >= rect.ymin()
                    && point.y() <= rect.ymax())
            {
                inside.add(point);
            }
        }
        return inside;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
        {
            throw new IllegalArgumentException();
        }
        double minDist = Double.POSITIVE_INFINITY;
        Point2D closest = null;
        for (Point2D point : points)
        {
            if (point == p)
            {
                continue;
            }
            double d = point.distanceSquaredTo(p);
            if (d < minDist)
            {
                minDist = d;
                closest = point;
            }
        }
        return closest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

    }
}
