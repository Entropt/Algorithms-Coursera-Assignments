import edu.princeton.cs.algs4.Picture;
import java.util.Arrays;

public class SeamCarver {
    private static final double BORDER_PIXEL_ENERGY = 1000d;
    private static final boolean VERTICAL = true, HORIZONTAL = false;
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null){
            throw new IllegalArgumentException();
        }

        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int col, int row) {
        validatePixel(col, row);

        if (col == 0 || col == width() -1 || row == 0 || row == height() - 1) {
            return BORDER_PIXEL_ENERGY;
        }

        double rx = picture.get(col + 1, row).getRed() - picture.get(col - 1, row).getRed();
        double gx = picture.get(col + 1, row).getGreen() - picture.get(col - 1, row).getGreen();
        double bx = picture.get(col + 1, row).getBlue() - picture.get(col - 1, row).getBlue();
        double ry = picture.get(col, row + 1).getRed() - picture.get(col, row - 1).getRed();
        double gy = picture.get(col, row + 1).getGreen() - picture.get(col, row - 1).getGreen();
        double by = picture.get(col, row + 1).getBlue() - picture.get(col, row - 1).getBlue();

        return Math.sqrt(rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Pair[][] energies = new Pair[height()][width()];

        for (int i = 0; i < height(); i++){
            energies[i][0] = new Pair(BORDER_PIXEL_ENERGY, -1);
        }

        for (int col = 1; col < width(); col++) {
            energies[0][col] = new Pair(BORDER_PIXEL_ENERGY, -1);
            for (int row = 0; row < height(); row++) {
                relaxHorizontal(energies, row, col);
            }
        }

        return extractHorizontalSeam(energies);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        Pair[][] energies = new Pair[height()][width()];
        for (int i = 0; i < width(); i++){
            energies[0][i] = new Pair(BORDER_PIXEL_ENERGY, -1);
        }

        for (int row = 1; row < height(); row++) {
            energies[row][0] = new Pair(BORDER_PIXEL_ENERGY, -1);
            for (int col = 0; col < width(); col++) {
                relaxVertical(energies, row, col);
            }
        }

        return extractVerticalSeam(energies);
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!isValidSeam(seam, HORIZONTAL)) {
            throw new IllegalArgumentException("Illegal seam!");
        }
        Picture seamedPicture = new Picture(width(), height() - 1);

        for (int col = 0; col < width(); col++) {
            int rowBias = 0;
            for (int row = 0; row < height() - 1; row++) {
                if (seam[col] == row){
                    rowBias = 1;
                }
                seamedPicture.set(col, row, picture.get(col, row + rowBias));
            }
        }
        this.picture = seamedPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!isValidSeam(seam, VERTICAL)){
            throw new IllegalArgumentException("Illegal seam!");
        }

        Picture seamedPicture = new Picture(width() - 1, height());
        for(int row = 0; row < height(); row++){
            int colBias = 0;
            for(int col = 0; col < width() - 1; col++) {
                if (seam[row] == col){
                    colBias = 1;
                }
                seamedPicture.set(col, row, picture.get(col + colBias, row));
            }
        }
        picture = seamedPicture;
    }

    private void validatePixel(int col, int row) {
        if (!isValidPixel(col, row)){
            throw new IllegalArgumentException("Invalid pixel: col: " + col + ", row: " + row );
        }
    }

    private boolean isValidPixel(int col, int row) {
        return col >= 0 && col < width() && row >= 0 && row < height();
    }

    private void relaxVertical(Pair[][] energies, int row, int col) {
        double myEnergy = energy(col, row);
        Pair[] paths = {
                new Pair( isValidPixel(col - 1, row -1) ? myEnergy + energies[row - 1][col - 1].energy : Double.MAX_VALUE, col - 1),
                new Pair( isValidPixel(col, row - 1) ? myEnergy + energies[row - 1][col].energy : Double.MAX_VALUE, col),
                new Pair( isValidPixel(col + 1, row - 1) ? myEnergy + energies[row - 1][col + 1].energy : Double.MAX_VALUE, col + 1)
        };

        Arrays.sort(paths);
        energies[row][col] = paths[0];
    }


    private void relaxHorizontal(Pair[][] energies, int row, int col) {
        double myEnergy = energy(col, row);
        Pair[] paths = {
                new Pair( isValidPixel(col - 1, row - 1) ? myEnergy + energies[row - 1][col - 1].energy : Double.MAX_VALUE, row - 1),
                new Pair( isValidPixel(col - 1, row) ? myEnergy + energies[row][col - 1].energy : Double.MAX_VALUE, row),
                new Pair( isValidPixel(col - 1, row + 1) ? myEnergy + energies[row + 1][col - 1].energy : Double.MAX_VALUE, row + 1)
        };
        Arrays.sort(paths);
        energies[row][col] = paths[0];
    }

    private int[] extractVerticalSeam(Pair[][] energies) {
        int[] seam = new int[height()];
        double lowestEnergy = Double.MAX_VALUE;
        int index = -1;

        // Find the lowest energy
        for (int col = 0; col < width(); col++) {
            if (energies[height() - 1][col].energy < lowestEnergy) {
                lowestEnergy = energies[height() - 1][col].energy;
                index = col;
            }
        }

        int row = height() - 1;
        while (row > -1) {
            seam[row] = index;
            index = energies[row][index].prev;
            row--;
        }
        return seam;
    }

    private int[] extractHorizontalSeam(Pair[][] energies) {
        int[] seam = new int[width()];
        double lowestEnergy = Double.MAX_VALUE;
        int index = -1;

        // Find the lowest energy
        for (int row = 0; row < height(); row++){
            if (energies[row][width() - 1].energy < lowestEnergy) {
                lowestEnergy = energies[row][width() - 1].energy;
                index = row;
            }
        }

        int col = width() - 1;
        while (col > -1){
            seam[col] = index;
            index = energies[index][col].prev;
            col--;
        }

        return seam;
    }


    private boolean isValidSeam(int[] seam, boolean vertical) {
        if (seam == null){
            return false;
        }

        if ((vertical && seam.length != height()) || (!vertical && seam.length != width())) {
            return false;
        }

        for(int i : seam){
            if ((i < 0 ) || (vertical && i >= width()) || (!vertical && i>= height())) {
                return false;
            }
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1){
                return false;
            }
        }
        return true;
    }


    private static class Pair implements Comparable<Pair> {
        public final double energy;
        public final int prev;

        public Pair(double energy, int prev) {
            this.energy = energy;
            this.prev = prev;
        }

        @Override
        public int compareTo(Pair o) {
            return Double.compare(this.energy, o.energy);
        }
    }

    public static void main(String[] args) {
        SeamCarver pic = new SeamCarver(new Picture("Pic.jpg"));

        int deleted = 300;
        while (deleted > 0) {
            deleted--;
            pic.removeVerticalSeam(pic.findVerticalSeam());
        }

        pic.picture.show();
    }

}