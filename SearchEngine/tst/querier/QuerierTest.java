package querier;

import com.google.common.collect.ImmutableMap;
import invertedindex.invertedindex.Document;
import invertedindex.invertedindex.InvertedIndex;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuerierTest {

    @Test
    public void queryMostSimilarTest() throws NoSuchFieldException, IllegalAccessException {

        //remove
        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                .withDictionaryType(InvertedIndex.DictionaryType.Hash)
                .withStopWords(new HashSet<>())
                .build();

        invertedIndex.add(1, "a the my fair lady is a fair pig");
        invertedIndex.add(2, "what a lovely lovely table fair");
        invertedIndex.add(3, "isnt everything just a great pleasure");



        final Querier querier = new Querier(getTestDocuments(invertedIndex));

        //all weights of queries are 1.0
        final Map<String, Double> termWeights = new HashMap<>();
        termWeights.put("lady", 1.0);
        termWeights.put("fair", 1.0);
        termWeights.put("pig", 1.0);

        final Document query = new Query(0, termWeights);

        final List<Document> topDocuments = querier.queryMostSimilar(query, 2);

        final int numDocs = 3;
        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("a", TestUtils.calculateTfIdf(2, numDocs, 3))
                .put("the", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("is", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(2, ImmutableMap.<String, Double>builder()
                .put("what", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("a", TestUtils.calculateTfIdf(1, numDocs, 3))
                .put("lovely", TestUtils.calculateTfIdf(2, numDocs, 1))
                .put("table", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(1, numDocs, 2))
                .build());

        final List<Document> expectedTopDocuments = Arrays.asList(document1, document2);

        Assert.assertEquals(expectedTopDocuments, topDocuments);
    }

    private Set<Document> getTestDocuments(final InvertedIndex invertedIndex) throws NoSuchFieldException, IllegalAccessException {
        final Map<Document, Map<String, Double>> documents = TestUtils.getDocuments(invertedIndex);
        return documents.keySet();
    }

}
