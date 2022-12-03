import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;
    // construct an empty deque
    public Deque() {
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        first = new Node(item, first, null);
        if (size == 0) {
            last = first;
        } else {
            first.next.pre = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        last = new Node(item, null, last);
        if (size == 0) {
            first = last;
        } else {
            last.pre.next = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = first.item;
        size--;
        first = first.next;
        if (first != null) {
            first.pre = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = last.item;
        size--;
        last = last.pre;
        if (last != null) {
            last.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new IteratorList();
    }

    private class Node {
        private Item item;
        private Node next;
        private Node pre;

        private Node() {

        }

        private Node(Item item, Node next, Node pre) {
            this.item = item;
            this.next = next;
            this.pre = pre;
        }
    }

    private class IteratorList implements Iterator<Item> {
        private Node current = first;
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        System.out.println(deque.size());
        deque.addFirst("1");
        deque.addFirst("2");
        deque.addFirst("3");
        deque.addFirst("4");
        deque.addLast("5");
        System.out.println(deque.size());
        Iterator<String> iter = deque.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}

