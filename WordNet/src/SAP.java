import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Graph is null");
        }

        digraph = new Digraph(G);
    }

    private void vertexChecker(int v) {
        int V = digraph.V();
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException();
    }

    private void verticesChecker(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new NullPointerException();
        }

        for (int v : vertices) {
            vertexChecker(v);
        }
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        vertexChecker(v);
        vertexChecker(w);

        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestLength = Integer.MAX_VALUE;

        for (int i = 0; i < digraph.V(); i++) {
            if (fromV.hasPathTo(i) && fromW.hasPathTo(i)) {
                int l = fromV.distTo(i) + fromW.distTo(i);
                if (l < shortestLength) {
                    shortestLength = l;
                }
            }
        }

        if (shortestLength == Integer.MAX_VALUE) {
            return -1;
        }

        return shortestLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        vertexChecker(v);
        vertexChecker(w);

        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(digraph, w);

        int shortestLength = Integer.MAX_VALUE;
        int shortestAncestor = -1;

        for (int i = 0; i < digraph.V(); i++) {
            if (fromV.hasPathTo(i) && fromW.hasPathTo(i)) {
                int l = fromV.distTo(i) + fromW.distTo(i);
                if (l < shortestLength) {
                    shortestLength = l;
                    shortestAncestor = i;
                }
            }
        }

        return shortestAncestor;
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int shortestLength = Integer.MAX_VALUE;
        for (int vr : v) {
            for (int wr : w) {
                int l = length(vr, wr);
                if (l != -1) {
                    shortestLength = Math.min(shortestLength, l);
                }
            }
        }

        if (shortestLength == Integer.MAX_VALUE) {
            return -1;
        }

        return shortestLength;
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int shortestLength = Integer.MAX_VALUE;
        int shortestAncestor = -1;
        for (int vr : v) {
            for (int wr : w) {
                int l = length(vr, wr);
                if (l != -1 && l < shortestLength) {
                    shortestLength = l;
                    shortestAncestor = ancestor(vr, wr);
                }
            }
        }

        return shortestAncestor;
    }

}
