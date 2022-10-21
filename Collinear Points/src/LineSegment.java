public class LineSegment {
    private final Point p;   // one endpoint of this line segment
    private final Point q;

    // constructs the line segment between points p and q
    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new NullPointerException("Input contains null");
        }

        this.p = p;
        this.q = q;
    }

    // draws this line segment
    public void draw() {
        p.drawTo(q);
    }

    // string representation
    public String toString() {
        return p + " -> " + q;
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
