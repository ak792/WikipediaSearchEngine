package indexer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Dictionary {

    private final Map<String, PostingsList> termsToPostingsLists;

    public Dictionary() {
        termsToPostingsLists = new HashMap<>();
    }

    public boolean hasPostingsList(final String term) {
        return getPostingsList(term) != null;
    }

    public PostingsList getPostingsList(final String term) {
        return termsToPostingsLists.get(term);
    }

    public PostingsList getOrCreatePostingsList(final String term) {
        termsToPostingsLists.putIfAbsent(term, new PostingsList());
        return termsToPostingsLists.get(term);
    }

    public void setPostingsList(final String term, final PostingsList postingsList) {
        termsToPostingsLists.put(term, postingsList);
    }

    public int size() {
        return termsToPostingsLists.size();
    }

    public int findMaxPostingsListSize() {
        final Optional<Integer> maxPostingsListSizeOptional = termsToPostingsLists.keySet()
                                                                                  .stream()
                                                                                  .map(termsToPostingsLists::get)
                                                                                  .map(PostingsList::size)
                                                                                  .max(Comparator.naturalOrder());

        //if there are no terms in the dictionary, default to a max size of 0
        return maxPostingsListSizeOptional.orElse(0);
    }

    public int findMinPostingsListSize() {
        final Optional<Integer> maxPostingsListSizeOptional = termsToPostingsLists.keySet()
                .stream()
                .map(termsToPostingsLists::get)
                .map(PostingsList::size)
                .min(Comparator.naturalOrder());

        //if there are no terms in the dictionary, default to a max size of 0
        return maxPostingsListSizeOptional.orElse(0);
    }

}
