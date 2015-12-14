package indexer;

import java.util.List;
import java.util.Map;

public class InvertedIndex {


    private final Dictionary dictionary;

    public InvertedIndex() {
        dictionary = new Dictionary();
    }

    public void add(final String term, final int position) {
        final PostingsList postingsList = dictionary.getOrCreatePostingsList(term);
        postingsList.add(position);
    }

    public int postingsListSize(final String term) {
        return dictionary.getPostingsList(term).size();
    }

    public int findMaxPostingsListSize() {
        return dictionary.findMaxPostingsListSize();
    }

    public int findMinPostingsListSize() {
        return dictionary.findMinPostingsListSize();
    }

    public int dictionarySize() {
        return dictionary.size();
    }

    public String toString() {
        return "InvertedIndex with " + dictionarySize() + " PostingsLists.\n Longest: " + findMaxPostingsListSize()
                + "\n Shortest:" + findMinPostingsListSize();
    }

}
