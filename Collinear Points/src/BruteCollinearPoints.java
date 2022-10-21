import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Input is null.");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Input contains null.");
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        for (int i = 1; i < pointsCopy.length; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i - 1]) == 0) {
                throw new IllegalArgumentException("Input contains duplicate.");
            }
        }

        /**
         * To check if 3 points are collinear.
         * If so, continue to check 2 out of 3 with the point s.
         */
        for (int p = 0; p < pointsCopy.length - 3; p++)
            for (int q = p + 1; q < pointsCopy.length - 2; q++)
                for (int r = q + 1; r < pointsCopy.length - 1; r++) {
                    if (pointsCopy[p].slopeTo(pointsCopy[q])
                            == pointsCopy[p].slopeTo(pointsCopy[r]))
                        for (int s = r + 1; s < pointsCopy.length; s++)
                            if (pointsCopy[p].slopeTo(pointsCopy[q])
                                    == pointsCopy[p].slopeTo(pointsCopy[s])) {
                                segments.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                            }
                }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
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
        StdOut.println("Find " + collinear.numberOfSegments() + " lines.");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
