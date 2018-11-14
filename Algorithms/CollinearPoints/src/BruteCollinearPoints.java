import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints
{
    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i != points.length; ++i) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
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
            for (int j = i + 1; j < points.length; ++j) {
                for (int k = j + 1; k < points.length; ++k) {
                    if (!pointsCollinear(points[i], points[j], points[k])) {
                        continue;
                    }
                    for (int m = k + 1; m < points.length; ++m) {
                        if (!pointsCollinear(points[i], points[j], points[m])) {
                            continue;
                        }
                        segments.add(new LineSegment(points[i], points[m]));
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private boolean pointsCollinear(Point p1, Point p2, Point p3) {
        double slope1 = p1.slopeTo(p2);
        double slope2 = p1.slopeTo(p3);
        return slope1 == slope2 || Math.abs(slope1 - slope2) < 0.00000001;
    }
}
