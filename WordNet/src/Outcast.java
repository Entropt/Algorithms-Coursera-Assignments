public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] distance = new int[nouns.length];
        for (int i = 0; i < nouns.length; ++i) {
            for (int j = i + 1; j < nouns.length; ++j) {
                int dist = wordNet.distance(nouns[i], nouns[j]);
                distance[i] += dist;
                distance[j] += dist;
            }
        }
        int maxDistance = 0;
        String result = "";
        for (int i = 0; i < distance.length; ++i) {
            if (distance[i] > maxDistance) {
                maxDistance = distance[i];
                result = nouns[i];
            }
        }
        return result;
    }

    // see test client below
    public static void main(String[] args)  {

    }
}