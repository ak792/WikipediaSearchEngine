package invertedindex.invertedindex;

import com.google.common.collect.ImmutableSet;
import invertedindex.dictionary.Dictionary;
import invertedindex.dictionary.HashDictionary;
import invertedindex.postingslist.PostingsList;
import invertedindex.postingslist.PostingsListFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private final List<String> documentStrings;
    private final Map<Document, Map<String, Double>> documents; //map of documents to term weights. maintain termWeights reference that we can change them as new documents are added
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

        this.documentStrings = new ArrayList<>();
        this.documents = new HashMap<>();
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

    //todo: figure out how to assign the document id rather than passing it in
    public void add(final int documentId, final String document) {
        documentStrings.add(document);

        final List<String> terms = getTerms(document);
        for (int position = 0; position < terms.size(); position++) {
            final String token = terms.get(position);
            add(documentId, token, position);
        }

        final Map<String, Double> termWeights = getTermWeights(documentId, terms);


        documents.put(new Document(documentId, termWeights), termWeights);

        //TODO: optimize
        updateAllDocuments();
    }

    //TODO: not working
    //TODO: optimize to take advantage of previously calculated vlaues. can probaly just store tf and calculate idf (and tf * idf) on the fly
    public void updateAllDocuments() {
        for (final Map.Entry<Document, Map<String, Double>> entry : documents.entrySet()) {
            final Document document = entry.getKey();
            final Map<String, Double> termWeights = entry.getValue();

            final Map<String, Double> newTermWeights = getTermWeights(document.getDocumentId(), termWeights.keySet());
            document.setTermWeights(newTermWeights);

            documents.put(document, newTermWeights);
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

    private Map<String, Double> getTermWeights(final int documentId, final Collection<String> terms) {
        final Map<String, Double> termWeights = new HashMap<>();

        for (final String term : terms) {
            termWeights.put(term, getTfIdf(term, documentId));
        }

        return termWeights;
    }

    private List<String> getTerms(final String str) {
        final String[] tokens = tokenize(str);

        return Arrays.stream(tokens)
                     .map(termNormalizer::normalizeTerm)
                     .filter(normalizedToken -> !dictionary.isStopWord(normalizedToken))
                     .collect(Collectors.toList());
    }

    private static String[] tokenize(final String str) {
        //split on one or more spaces
        //make handle punctuation
        return str.split(" +");
    }

    //WILL CHANGE EVERY TIME A NEW DOCUMENT IS ADDED
    public double getTfIdf(final String term, final int documentId) {
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

        return Math.log((double) documentStrings.size() / postingsList.getNumDocuments());
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
