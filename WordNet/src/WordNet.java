import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private final Map<String, ArrayList<Integer>> wordToId;
    private final Map<Integer, String> idToWord;
    private int n;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }


        wordToId = new HashMap<>();
        idToWord = new HashMap<>();
        n = 0;
        processSynsets(synsets);
        processHypernyms(hypernyms);
    }

    private void processSynsets(String synsets) {
        In input = new In(synsets);
        String line;

        while ((line = input.readLine()) != null) {
            String[] st = line.split(",");

            /** To remove spaces. */
            if (st.length < 2) {
                continue;
            }

            n++;
            int id = Integer.parseInt(st[0]);
            idToWord.put(id, st[1]);

            String[] nouns = st[1].split(" ");
            for (String noun : nouns) {
                ArrayList<Integer> ids = wordToId.get(noun);
                if (ids != null) {
                    ids.add(id);
                }
                else {
                    ArrayList<Integer> nids = new ArrayList<Integer>();
                    nids.add(id);
                    wordToId.put(noun, nids);
                }
            }
        }
    }

    private void processHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        String line;
        Digraph digraph = new Digraph(n);

        while ((line = in.readLine()) != null) {
            String[] st = line.split(",");
            if (st.length < 2) {
                continue;
            }
            int start = Integer.parseInt(st[0]);
            for (int i = 1; i < st.length; ++i) {
                digraph.addEdge(start, Integer.parseInt(st[i]));
            }
        }

        DirectedCycle dc = new DirectedCycle(digraph);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException();
        }

        int numberOfRoot = 0;
        for (int i = 0; i < digraph.V(); ++i) {
            if (digraph.outdegree(i) == 0) {
                numberOfRoot++;
                if (numberOfRoot > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        return wordToId.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return sap.length(wordToId.get(nounA), wordToId.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return idToWord.get(sap.ancestor(wordToId.get(nounA), wordToId.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}