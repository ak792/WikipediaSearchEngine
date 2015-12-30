package utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import invertedindex.dictionary.Dictionary;
import invertedindex.invertedindex.Document;
import invertedindex.invertedindex.InvertedIndex;
import querier.similarity.DocumentSimilarityCalculator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class TestUtils {


    private TestUtils() {

    }

    public static Dictionary constructInvertedIndexDictionary() throws NoSuchFieldException, IllegalAccessException {
        final InvertedIndex invertedIndex = TestUtils.constructInvertedIndex();
        return getDicitonary(invertedIndex);
    }

    public static Dictionary getDicitonary(final InvertedIndex invertedIndex) throws NoSuchFieldException, IllegalAccessException {
        final Field dictionaryField = invertedIndex.getClass().getDeclaredField("dictionary");
        dictionaryField.setAccessible(true);

        return (Dictionary) dictionaryField.get(invertedIndex);
    }

    public static InvertedIndex constructInvertedIndex() {
        final Set<String> stopWords = ImmutableSet.of("a", "the", "is");
        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                .withDictionaryType(InvertedIndex.DictionaryType.Hash)
                .withStopWords(stopWords)
                .build();

        final int testDocument1Id = 1;
        final String testDocument1 = "a the my fair lady is a fair pig";

        final int testDocument2Id = 2;
        final String testDocument2 = "what a lovely lovely    table fair";

        invertedIndex.add(testDocument1Id, testDocument1);
        invertedIndex.add(testDocument2Id, testDocument2);

        return invertedIndex;
    }



    public static double calculateTfIdf(final int tf, final double numTotalDocs, final int numDocsTermIsIn) {
        return numDocsTermIsIn != 0 ? tf * Math.log(numTotalDocs / numDocsTermIsIn) : 0;
    }

    public static Document getLadyFairMyPigDocument(final int documentId) {
        final Document document = new Document(documentId, ImmutableMap.<String, Set<Integer>>builder()
                .put("lady", ImmutableSet.of(1))
                .put("fair", ImmutableSet.of(2))
                .put("my", ImmutableSet.of(3))
                .put("pig", ImmutableSet.of(4))
                .build());

        return document;
    }

    public static Document getManFairMyPigDocument(final int documentId) {
        final Document document = new Document(documentId, ImmutableMap.<String, Set<Integer>>builder()
                .put("lady", ImmutableSet.of(1))
                .put("fair", ImmutableSet.of(2))
                .put("my", ImmutableSet.of(3))
                .put("pig", ImmutableSet.of(4))
                .build());

        return document;
    }


    public static double invokeCalculateCosineSimilarity(final DocumentSimilarityCalculator documentSimilarityCalculator, final Document document1, final Document document2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvocationTargetException {
        final Method calculateCosineSimilarityMethod = documentSimilarityCalculator.getClass().getDeclaredMethod("calculateCosineSimilarity", Document.class, Document.class);
        calculateCosineSimilarityMethod.setAccessible(true);

        return (double) calculateCosineSimilarityMethod.invoke(documentSimilarityCalculator, document1, document2);
    }

    public static double invokeCalculateDotProduct(final DocumentSimilarityCalculator documentSimilarityCalculator, final Document document1, final Document document2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method calculateDotProductMethod = documentSimilarityCalculator.getClass().getDeclaredMethod("calculateDotProduct", Document.class, Document.class);
        calculateDotProductMethod.setAccessible(true);

        return (double) calculateDotProductMethod.invoke(documentSimilarityCalculator, document1, document2);
    }

    public static double invokeCalculateVectorLength(final DocumentSimilarityCalculator documentSimilarityCalculator, final Document document) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method calculateVectorLengthMethod = documentSimilarityCalculator.getClass().getDeclaredMethod("calculateVectorLength", Document.class);
        calculateVectorLengthMethod.setAccessible(true);

        return (double) calculateVectorLengthMethod.invoke(documentSimilarityCalculator, document);
    }
}
