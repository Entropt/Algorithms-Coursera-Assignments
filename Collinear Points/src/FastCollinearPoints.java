import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Input is null.");
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("Input contains null.");
            }
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        for (int i = 1; i < pointsCopy.length; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i - 1]) == 0) {
                throw new IllegalArgumentException("Input contains duplicate.");
            }
        }

        /**
         * Sort array named pointsCopy by the slope order of p.
         * We will realize that numbers with similar slope of p will be adjacent.
         */
        pointsCopy = Arrays.copyOf(points, points.length);
        for (Point p : points) {
            Arrays.sort(pointsCopy, p.slopeOrder());
            double slope = p.slopeTo(pointsCopy[0]);
            int count = 1, i;
            for (i = 1; i < pointsCopy.length; i++) {
                if (p.slopeTo(pointsCopy[i]) == slope) count++;
                else {
                    if (count >= 3) {
                        Arrays.sort(pointsCopy, i - count, i);
                        if (p.compareTo(pointsCopy[i - count]) < 0)
                            segments.add(new LineSegment(p, pointsCopy[i - 1]));
                    }
                    slope = p.slopeTo(pointsCopy[i]);
                    count = 1;
                }
            }
            if (count >= 3) {
                Arrays.sort(pointsCopy, i - count, i);
                if (p.compareTo(pointsCopy[i - count]) < 0)
                    segments.add(new LineSegment(p, pointsCopy[i - 1]));
            }
        }
        /** -------- */
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("Find " + collinear.numberOfSegments() + " lines.");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}