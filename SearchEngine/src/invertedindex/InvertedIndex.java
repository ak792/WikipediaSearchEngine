package invertedindex;

import java.util.HashSet;
import java.util.Set;

public class InvertedIndex {


    private final Dictionary dictionary;

    private InvertedIndex(final Set<String> stopWords, final PostingsListFactory.PostingsListType postingsListType) {
        dictionary = HashDictionary.Builder.builder()
                                            .withStopWords(stopWords)
                                            .withPostingsListType(postingsListType)
                                            .build();
    }

    //TODO: try to consolidate this and the Dictionary's Builder
    public static class Builder {

        private Set<String> stopWords;
        private PostingsListFactory.PostingsListType postingsListType;

        private Builder() {
            //Initialize defaults
            stopWords = new HashSet<>();
            postingsListType = PostingsListFactory.PostingsListType.DynamicArray;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withStopWords(final Set<String> stopWords) {
            this.stopWords = stopWords;
            return this;
        }

        public Builder withPostingsListType(final PostingsListFactory.PostingsListType postingsListType) {
            this.postingsListType = postingsListType;
            return this;
        }

        public InvertedIndex build() {
            return new InvertedIndex(stopWords, postingsListType);
        }
    }

    public void add(final String term, final int position) {
        if (dictionary.isStopWord(term)) {
            return;
        }

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

    public String allContentsToString() {
        return dictionary.getAllContents();
    }

}
