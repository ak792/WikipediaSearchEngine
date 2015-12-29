package invertedindex.dictionary;

import com.google.common.base.Joiner;
import invertedindex.postingslist.PostingsList;
import invertedindex.postingslist.PostingsListFactory;
import invertedindex.StopWordException;
import invertedindex.invertedindex.TermNormalizer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


public class HashDictionary implements Dictionary {

    private final Map<String, PostingsList> termsToPostingsLists;

    private final Set<String> stopWords;
    private final PostingsListFactory postingsListFactory;
    private final TermNormalizer termNormalizer;

    private HashDictionary(final Set<String> stopWords, final PostingsListFactory.PostingsListType postingsListType, final TermNormalizer termNormalizer) {
        this.termsToPostingsLists = new HashMap<>();
        this.stopWords = stopWords;
        this.postingsListFactory = new PostingsListFactory(postingsListType);
        this.termNormalizer = termNormalizer;
    }

    @Override
    public boolean hasPostingsList(final String term) {
        final String normalizedTerm = termNormalizer.normalizeTerm(term);

        return termsToPostingsLists.containsKey(normalizedTerm);
    }

    @Override
    public PostingsList getPostingsList(final String term) {
        final String normalizedTerm = termNormalizer.normalizeTerm(term);


        if (!hasPostingsList(term)) {
            throw new NoSuchElementException("No postings list found for " + term);
        }

        return termsToPostingsLists.get(normalizedTerm);
    }


    /**
     * @throws StopWordException if term is a stop word
     */
    @Override
    public PostingsList getOrCreatePostingsList(final String term) {
        validateNotStopWord(term);

        final String normalizedTerm = termNormalizer.normalizeTerm(term);

        termsToPostingsLists.putIfAbsent(normalizedTerm, postingsListFactory.getInstance());
        return termsToPostingsLists.get(normalizedTerm);
    }

    @Override
    public boolean isStopWord(final String term) {
        final String normalizedTerm = termNormalizer.normalizeTerm(term);

        return stopWords.contains(normalizedTerm);
    }

    private void validateNotStopWord(final String term) {
        if (isStopWord(term)) {
            throw new StopWordException(String.format("Term %s is a stop word", term));
        }
    }

    @Override
    public int getTermFrequency(final String term, final int documentId) {
        return hasPostingsList(term) ? getPostingsList(term).getTermFrequency(documentId) : 0;
    }

    //Returns each term and its postings in alphabetical order
    @Override
    public String toString() {
        return toString(Comparator.naturalOrder());
    }

    @Override
    public String toString(final Comparator<String> comparator) {
        final List<String> entries = termsToPostingsLists.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        entries.sort(comparator);

        return Joiner.on("\n").join(entries);
    }

    public static class Builder {

        private Set<String> stopWords;
        private PostingsListFactory.PostingsListType postingsListType;
        private TermNormalizer termNormalizer;


        private Builder() {
            //Initialize defaults
            stopWords = new HashSet<>();
            postingsListType = PostingsListFactory.PostingsListType.DynamicArray;
            termNormalizer = new TermNormalizer();
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

        public Builder withTermNormalizer(final TermNormalizer termNormalizer) {
            this.termNormalizer = termNormalizer;
            return this;
        }

        public Dictionary build() {
            return new HashDictionary(stopWords, postingsListType, termNormalizer);
        }
    }
}
