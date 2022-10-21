import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        a = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (size == a.length) {
            resize(size << 1);
        }

        if (size == 0) {
            a[size++] = item;
            return;
        }

        int randomIndex = StdRandom.uniformInt(size);
        Item temp = a[randomIndex];
        a[randomIndex] = item;
        a[size++] = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (size == a.length / 4) {
            resize(a.length >> 1);
        }

        int randomIndex = StdRandom.uniformInt(size);
        Item item = a[randomIndex];
        a[randomIndex] = a[--size];
        a[size] = null; // to prevent loitering
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return a[StdRandom.uniformInt(size)];
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i;
        private int[] randomIndices;

        public ArrayIterator() {
            i = 0;
            randomIndices = new int[size];
            for (int j = 0; j < size; j++) {
                randomIndices[j] = j;
            }
            StdRandom.shuffle(randomIndices);
        }

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[randomIndices[i++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        queue.dequeue();
        Iterator<String> iterO = queue.iterator();
        Iterator<String> iter1 = queue.iterator();
        while (iterO.hasNext()) {
            System.out.println(iterO.next());
        }
        System.out.println();
        while (iter1.hasNext()) {
            System.out.println(iter1.next());
        }
        System.out.println();
    }

}