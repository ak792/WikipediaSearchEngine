package invertedindex;

import java.util.HashSet;
import java.util.Set;

public class InvertedIndex {


    public enum DictionaryType {
        Hash;
    }

    private final Dictionary dictionary;

    private InvertedIndex(final DictionaryType dictionaryType,
                          final Set<String> stopWords,
                          final PostingsListFactory.PostingsListType postingsListType) {
        switch (dictionaryType) {
            case Hash:
                dictionary = HashDictionary.Builder.builder()
                    .withStopWords(stopWords)
                    .withPostingsListType(postingsListType)
                    .build();
                break;

            default:
                throw new RuntimeException("DictionaryType " + dictionaryType + " not recognized");
        }
    }

    //todo: figure out how to assign the document id rather than passing it in
    public void add(final int documentId, final String document) {
        final String[] tokens = tokenize(document);

        add(documentId, tokens);
    }

    private void add(final int documentId, final String[] tokens) {
        for (int position = 0; position < tokens.length; position++) {
            final String token = tokens[position];
            add(documentId, token, position);
        }
    }

    private void add(final int documentId, final String term, final int position) {
        if (dictionary.isStopWord(term)) {
            return;
        }

        final PostingsList postingsList = dictionary.getOrCreatePostingsList(term);
        postingsList.add(documentId, position);
    }

    private static String[] tokenize(final String str) {
        //split on one or more spaces
        return str.split(" +");
    }

    public int getNumDocsInPostingsList(final String term) {
        return dictionary.getPostingsList(term).numDocuments();
    }

    public int getNumTerms() {
        return dictionary.numTerms();
    }


    @Override
    public String toString() {
        return dictionary.toString();
    }

    //TODO: try to consolidate this and the Dictionary's Builder
    public static class Builder {

        private DictionaryType dictionaryType;
        private Set<String> stopWords;
        private PostingsListFactory.PostingsListType postingsListType;

        private Builder() {
            //Initialize defaults
            dictionaryType = DictionaryType.Hash;
            stopWords = new HashSet<>();
            postingsListType = PostingsListFactory.PostingsListType.DynamicArray;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withDictionaryType(final DictionaryType dictionaryType) {
            this.dictionaryType = dictionaryType;
            return this;
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
            return new InvertedIndex(dictionaryType, stopWords, postingsListType);
        }
    }

}
