import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints
{
    private final List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        points = Arrays.copyOf(points, points.length);
        Arrays.sort(points);
        for (int i = 1; i < points.length; ++i) {
            if (points[i - 1].compareTo(points[i]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        segments = new LinkedList<LineSegment>();

        for (int i = 0; i != points.length; ++i) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }

            Arrays.sort(points, i, points.length);
            Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());

            int j = i + 1;
            int startPointIndex = j;
            while (j < points.length) {
                if (points[j] == null) {
                    throw new IllegalArgumentException();
                }
                if (startPointIndex != j
                        && !pointsCollinear(points[i], points[startPointIndex], points[j])) {
                    addSegment(points, i, startPointIndex, j);
                    startPointIndex = j;
                }
                ++j;
            }

            if (startPointIndex < points.length) {
                addSegment(points, i, startPointIndex, points.length);
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private boolean pointsCollinear(Point p1, Point p2, Point p3) {
        double slope1 = p1.slopeTo(p2);
        double slope2 = p1.slopeTo(p3);
        return slope1 == slope2 || Math.abs(slope1 - slope2) < 0.00000001;
    }

    private void addSegment(Point[] points, int originIndex, int startPointIndex, int endPointIndex) {
        int collinearPointsCount = endPointIndex - startPointIndex + 1;
        if (collinearPointsCount >= 4) {
            for (int i = 0; i != originIndex; ++i) {
                double slope1 = points[i].slopeTo(points[originIndex]);
                double slope2 = points[originIndex].slopeTo(points[startPointIndex]);
                if (slope1 == slope2 || Math.abs(slope1 - slope2) < 0.000001)
                {
                    return;
                }
            }
            segments.add(new LineSegment(points[originIndex], points[endPointIndex - 1]));
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
