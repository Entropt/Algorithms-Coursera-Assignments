import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int size = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        while (size > 0) {
            System.out.println(queue.dequeue());
            size--;
        }
    }
}