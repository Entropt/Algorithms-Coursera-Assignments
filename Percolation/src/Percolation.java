import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP = 0;
    private final int bottom;
    private final boolean[][] open;
    private final int size;
    private int openSites;
    private final WeightedQuickUnionUF qf;

    private final WeightedQuickUnionUF topF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        open = new boolean[n][n];
        size = n;
        openSites = 0;
        bottom = n * n + 1;
        qf = new WeightedQuickUnionUF(n * n + 2);
        topF = new WeightedQuickUnionUF(n * n + 1);
    }

    private void checkException(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException();
        }
    }

    private int getIndex(int row, int col) {
        return size * (row - 1) + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkException(row, col);

        if (!open[row - 1][col - 1]) {
            openSites++;
            open[row - 1][col - 1] = true;

            /** To check if the opened site is at the TOP -> Connect to TOP point. */
            if (row == 1) {
                qf.union(getIndex(row, col), TOP);
                topF.union(getIndex(row, col), TOP);
            }

            /** To check if the opened site is at the bottom -> Connect to bottom point. */
            if (row == size) {
                qf.union(getIndex(row, col), bottom);
            }

            /** To connect the point to neighboring points. */
            if (row > 1 && isOpen(row - 1, col)) {
                qf.union(getIndex(row, col), getIndex(row - 1, col));
                topF.union(getIndex(row, col), getIndex(row - 1, col));
            }

            if (row < size && isOpen(row + 1, col)) {
                qf.union(getIndex(row, col), getIndex(row + 1, col));
                topF.union(getIndex(row, col), getIndex(row + 1, col));
            }

            if (col > 1 && isOpen(row, col - 1)) {
                qf.union(getIndex(row, col), getIndex(row, col - 1));
                topF.union(getIndex(row, col), getIndex(row, col - 1));
            }

            if (col < size && isOpen(row, col + 1)) {
                qf.union(getIndex(row, col), getIndex(row, col + 1));
                topF.union(getIndex(row, col), getIndex(row, col + 1));
            }
            /*  ------------------------------------------  */
        }


    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkException(row, col);
        return open[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkException(row, col);
        return topF.find(getIndex(row, col)) ==  topF.find(TOP);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return qf.find(TOP) == qf.find(bottom);
    }
}
