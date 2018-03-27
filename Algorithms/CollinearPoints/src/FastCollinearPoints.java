import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class FastCollinearPoints
{
    List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        segments = new LinkedList<LineSegment>();

        for (int i = 0; i != points.length; ++i) {
            Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());

            int j = i + 1;
            int startPointIndex = j;
            while (j < points.length) {
                if (startPointIndex != j
                        && !pointsCollinear(points[i], points[startPointIndex], points[j])) {
                    int collinearPointsCount = j - startPointIndex + 1;
                    if (collinearPointsCount >= 4) {
                        Arrays.sort(points, startPointIndex, j);
                        if (points[i].compareTo(points[startPointIndex]) < 0) {
                            segments.add(new LineSegment(points[i], points[j - 1]));
                        }
                        else if (collinearPointsCount == 4) {
                            segments.add(new LineSegment(points[startPointIndex], points[j - 1]));
                        }
                    }
                    startPointIndex = j;
                }
                ++j;
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
