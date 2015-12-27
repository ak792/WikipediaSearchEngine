package indexer;

import com.google.common.collect.ImmutableSet;
import com.jaunt.ResponseException;
import scraper.Scraper;

import java.util.HashSet;
import java.util.Set;

//TODO: there shouldn't be so many c
public class InvertedIndexFactory {

    private final Scraper scraper;
    private static final PostingsListFactory.PostingsListType defaultPostingsListType = PostingsListFactory.PostingsListType.DynamicArray;
    private static final Set<String> defaultStopWords = ImmutableSet.of("a", "the", "is");

    public InvertedIndexFactory() {
        scraper = new Scraper();
    }

    public InvertedIndex createInvertedIndex(final String url) throws ResponseException {
        return createInvertedIndex(url, defaultStopWords, defaultPostingsListType);

    }

    public InvertedIndex createInvertedIndex(final String url, final Set<String> stopWords) throws ResponseException {
        return createInvertedIndex(url, stopWords, defaultPostingsListType);

    }

    public InvertedIndex createInvertedIndex(final String url, final PostingsListFactory.PostingsListType postingsListType) throws ResponseException {
        return createInvertedIndex(url, defaultStopWords, postingsListType);
    }

    public InvertedIndex createInvertedIndex(final String url, final Set<String> stopWords, final PostingsListFactory.PostingsListType postingsListType) throws ResponseException {
        final String document = scraper.getDocument(url);
        final String[] tokens = tokenize(document);

        return createIndexFromTokens(tokens, stopWords, postingsListType);
    }

    private String[] tokenize(final String str) {
        //split on one or more spaces
        return str.split(" +");
    }

    private InvertedIndex createIndexFromTokens(final String[] tokens, final Set<String> stopWords, final PostingsListFactory.PostingsListType postingsListType) {
        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                                                                .withStopWords(stopWords)
                                                                .withPostingsListType(postingsListType)
                                                                .build();

        for (int position = 0; position < tokens.length; position++) {
            final String token = tokens[position];
            invertedIndex.add(token, position);
        }

        return invertedIndex;
    }


}
