package invertedindex.invertedindex;

import com.google.common.collect.ImmutableSet;
import invertedindex.dictionary.Dictionary;
import invertedindex.dictionary.HashDictionary;
import corpus.Document;
import invertedindex.postingslist.PostingsList;
import invertedindex.postingslist.PostingsListFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InvertedIndex {

    private static final PostingsListFactory.PostingsListType defaultPostingsListType = PostingsListFactory.PostingsListType.DynamicArray;
    private static final Set<String> defaultStopWords = ImmutableSet.of("a", "the", "is");

    public enum DictionaryType {
        Hash;
    }

    private final Dictionary dictionary;
    private final Set<Document> documents;
    private final TermNormalizer termNormalizer;


    private InvertedIndex(final DictionaryType dictionaryType,
                          final Set<String> stopWords,
                          final PostingsListFactory.PostingsListType postingsListType,
                          final TermNormalizer termNormalizer) {
        switch (dictionaryType) {
            case Hash:
                this.dictionary = HashDictionary.Builder.builder()
                    .withStopWords(stopWords)
                    .withPostingsListType(postingsListType)
                    .withTermNormalizer(termNormalizer)
                    .build();
                break;

            default:
                throw new RuntimeException("DictionaryType " + dictionaryType + " not recognized");
        }

        this.documents = new HashSet<>();
        this.termNormalizer = termNormalizer;
    }

    public static InvertedIndex getInstance() {
        return getInstance(defaultStopWords, defaultPostingsListType);
    }

    public static InvertedIndex getInstance(final Set<String> stopWords) {
        return getInstance(stopWords, defaultPostingsListType);
    }

    public static InvertedIndex getInstance(final PostingsListFactory.PostingsListType postingsListType) {
        return getInstance(defaultStopWords, postingsListType);
    }

    public static InvertedIndex getInstance(final Set<String> stopWords,
                                            final PostingsListFactory.PostingsListType postingsListType) {

        //todo: make better
        final TermNormalizer termNormalizer = new TermNormalizer();

        return InvertedIndex.Builder.builder()
                .withStopWords(stopWords)
                .withPostingsListType(postingsListType)
                .withTermNormalizer(termNormalizer)
                .build();
    }

    public Set<Document> getDocuments() {
        //defensive copy?
        return documents;
    }

    public void add(final Document document) {
        final int documentId = document.getDocumentId();
        final Map<String, Set<Integer>> terms = document.getTerms();
        add(documentId, terms);
    }

    //todo: figure out how to assign the document id rather than passing it in
    public void add(final int documentId, final String document) {
        final Map<String, Set<Integer>> terms = getTerms(document);
        add(documentId, terms);
    }

    public void add(final int documentId, final Map<String, Set<Integer>> terms) {
        documents.add(new Document(documentId, terms));

        for (final String term : terms.keySet()) {
            for (final int position : terms.get(term)) {
                add(documentId, term, position);
            }
        }
    }

    private void add(final int documentId, final String term, final int position) {
        //should be already true by the time we call this, but just in case
        if (dictionary.isStopWord(term)) {
            return;
        }

        final PostingsList postingsList = dictionary.getOrCreatePostingsList(term);
        postingsList.add(documentId, position);
    }

    private Map<String, Set<Integer>> getTerms(final String str) {
        final String[] tokens = tokenize(str);

        final List<String> termsList = Arrays.stream(tokens)
                     .map(termNormalizer::normalizeTerm)
                     .filter(normalizedToken -> !dictionary.isStopWord(normalizedToken))
                     .collect(Collectors.toCollection(ArrayList::new));

        return createTermsMap(termsList);
    }

    public static Map<String, Set<Integer>> createTermsMap(final List<String> termsList) {
        final Map<String, Set<Integer>> termsMap = new HashMap<>();
        for (int position = 0; position < termsList.size(); position++) {
            final String term = termsList.get(position);

            final Set<Integer> positionsInDocument;
            if (termsMap.containsKey(term)) {
                positionsInDocument = termsMap.get(term);
            } else {
                positionsInDocument = new HashSet<>();
                termsMap.put(term, positionsInDocument);
            }

            positionsInDocument.add(position);
        }

        return termsMap;
    }


    private static String[] tokenize(final String str) {
        //split on one or more spaces
        //make handle punctuation
        return str.split(" +");
    }

    public double getTfIdf(final String term, final int documentId) {
        if (documentId == 0) {
            return 1;
        }

        return dictionary.getTermFrequency(term, documentId) * getInverseDocumentFrequency(term);
    }

    //log( num docs / num docs the term appears in)
    public double getInverseDocumentFrequency(final String term) {
        if (!dictionary.hasPostingsList(term)) {
            return 0;
        }

        final PostingsList postingsList = dictionary.getPostingsList(term);

        if (postingsList.getNumDocuments() == 0) {
            return 0;
        }

        return Math.log((double) documents.size() / postingsList.getNumDocuments());
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
        private TermNormalizer termNormalizer;

        private Builder() {
            //Initialize defaults
            dictionaryType = DictionaryType.Hash;
            stopWords = new HashSet<>();
            postingsListType = PostingsListFactory.PostingsListType.DynamicArray;
            termNormalizer = new TermNormalizer();
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

        public Builder withTermNormalizer(final TermNormalizer termNormalizer) {
            this.termNormalizer = termNormalizer;
            return this;
        }

        public InvertedIndex build() {
            return new InvertedIndex(dictionaryType, stopWords, postingsListType, termNormalizer);
        }
    }

}
