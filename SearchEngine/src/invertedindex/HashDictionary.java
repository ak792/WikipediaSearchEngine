package invertedindex;

import com.google.common.base.Joiner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class HashDictionary implements Dictionary {

    private final Map<String, PostingsList> termsToPostingsLists;

    private final Set<String> stopWords;
    private final PostingsListFactory postingsListFactory;

    private HashDictionary(final Set<String> stopWords, final PostingsListFactory.PostingsListType postingsListType) {
        this.termsToPostingsLists = new HashMap<>();
        this.stopWords = stopWords;
        this.postingsListFactory = new PostingsListFactory(postingsListType);
    }

    @Override
    public boolean hasPostingsList(final String term) {
        final String normalizedTerm = normalizeTerm(term);

        return getPostingsList(normalizedTerm) != null;
    }

    @Override
    public PostingsList getPostingsList(final String term) {
        final String normalizedTerm = normalizeTerm(term);

        return termsToPostingsLists.get(normalizedTerm);
    }


    /**
     * @throws StopWordException if term is a stop word
     */
    @Override
    public PostingsList getOrCreatePostingsList(final String term) {
        validateNotStopWord(term);

        final String normalizedTerm = normalizeTerm(term);

        termsToPostingsLists.putIfAbsent(normalizedTerm, postingsListFactory.getInstance());
        return termsToPostingsLists.get(normalizedTerm);
    }

    /**
     * @throws StopWordException if term is a stop word
     */
    @Override
    public void setPostingsList(final String term, final PostingsList postingsList) {
        validateNotStopWord(term);

        final String normalizedTerm = normalizeTerm(term);
        termsToPostingsLists.put(normalizedTerm, postingsList);
    }

    @Override
    public boolean isStopWord(final String term) {
        final String normalizedTerm = normalizeTerm(term);

        return stopWords.contains(normalizedTerm);
    }

    private void validateNotStopWord(final String term) {
        if (isStopWord(term)) {
            throw new StopWordException(String.format("Term %s is a stop word", term));
        }
    }

    @Override
    public int numTerms() {
        return termsToPostingsLists.size();
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

    private String normalizeTerm(final String term) {
        //Normalize words to lower case
        //Note: While this will help in many cases (such as considering a word at the beginning of the sentence
        //the same as if it were in the middle), this can hurt in others (for example, an acronym that spells a word
        //will be considered the same as the word

        return term.toLowerCase(Locale.ENGLISH);
    }


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

        public Dictionary build() {
            return new HashDictionary(stopWords, postingsListType);
        }
    }
}
