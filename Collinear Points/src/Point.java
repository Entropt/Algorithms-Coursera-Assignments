import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    // constructs the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) {
            return -1;
        }

        if (this.y == that.y) {
            return Integer.compare(this.x, that.x);
        }
        else {
            return 1;
        }
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {
        if (compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        if (this.y == that.y) {
            return 0;
        }
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }

        return (double) (that.y - this.y) / (double) (that.x - this.x);
    }

    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return (p1, p2) -> Double.compare(slopeTo(p1), slopeTo(p2));
    }

    public static void main(String[] args) {
        Point pt1 = new Point(1, 2);
        Point pt2 = new Point(2, 2);
        StdOut.println("Slope: " + pt1.slopeTo(pt2));
        StdOut.println("Pt1 Compare to Pt2: " + pt1.compareTo(pt2));
    }
}