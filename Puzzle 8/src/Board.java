import java.util.ArrayList;
import java.util.Queue;

public class Board {
    private final int[][] tiles;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != i * tiles.length + j + 1) {
                    count++;
                }
            }
        }

        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != 0) {
                    res += Math.abs((tiles[i][j] - 1) / tiles.length - i)
                            + Math.abs((tiles[i][j] - 1) % tiles.length - j);
                }
            }
        }

        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y instanceof Board) {
            if (((Board) y).dimension() == tiles.length) {
                return toString().equals(y.toString());
            }
        }

        return false;
    }

    private int Space() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    return i * tiles.length + j + 1;
                }
            }
        }
        return 0;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        // Location of space.
        int x = (Space() - 1) / tiles.length;
        int y = (Space() - 1) % tiles.length;

        Board newNeighbor = new Board(this.tiles);

        if (x > 0) {
            neighbors.add(new Board(tileSwap(tiles, x, y, x - 1, y)));
        }

        newNeighbor = new Board(this.tiles);
        if (x < tiles.length - 1) {
            neighbors.add(new Board(tileSwap(tiles, x, y, x + 1, y)));
        }

        newNeighbor = new Board(this.tiles);
        if (y > 0) {
            neighbors.add(new Board(tileSwap(tiles, x, y, x, y - 1)));
        }

        newNeighbor = new Board(this.tiles);
        if (y < tiles.length - 1) {
            neighbors.add(new Board(tileSwap(tiles, x, y, x, y + 1)));
        }

        return neighbors;
    }

    private int[][] tileSwap(int[][] tiles, int row1, int col1, int row2, int col2) {
        int mid = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = mid;

        return tiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (tiles[0][0] == 0 || tiles[0][1] == 0) {
            return new Board(tileSwap(tiles, 0, 0, 0, 1));
        }
        return new Board(tileSwap(tiles, 1, 0, 1, 1));
    }

    // unit testing (not graded)
        public static void main(String[] args) {
            Board board = new Board(new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 8, 5}});
            System.out.println(board.toString());
            for (Board board1 :
                    board.neighbors()) {
                System.out.println(board1);
            }

        }
}