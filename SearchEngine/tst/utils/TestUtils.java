package utils;

import com.google.common.collect.ImmutableSet;
import invertedindex.dictionary.Dictionary;
import invertedindex.invertedindex.Document;
import invertedindex.invertedindex.InvertedIndex;

import java.lang.reflect.Field;
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

    public static Map<Document, Map<String, Double>> getDocuments(final InvertedIndex invertedIndex) throws NoSuchFieldException, IllegalAccessException {
        final Field documentsField = invertedIndex.getClass().getDeclaredField("documents");
        documentsField.setAccessible(true);

        return (Map<Document, Map<String, Double>>) documentsField.get(invertedIndex);
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
}
