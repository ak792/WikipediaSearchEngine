package invertedindex;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class InvertedIndexFactory {

    private static final PostingsListFactory.PostingsListType defaultPostingsListType = PostingsListFactory.PostingsListType.DynamicArray;
    private static final Set<String> defaultStopWords = ImmutableSet.of("a", "the", "is");

    private InvertedIndexFactory() {
    }

    //TODO: construction should be independent of indexing
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
        return InvertedIndex.Builder.builder()
                .withStopWords(stopWords)
                .withPostingsListType(postingsListType)
                .build();
    }
}
