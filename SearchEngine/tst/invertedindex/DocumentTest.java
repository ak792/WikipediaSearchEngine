package invertedindex;

import com.google.common.collect.ImmutableMap;
import invertedindex.invertedindex.Document;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DocumentTest {


    @Test
    public void test_equals_areEqual() {

        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        Assert.assertEquals(document1, document2);
    }

    @Test
    public void test_equals_areNotEqual_differentTerm() {

        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("f", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        Assert.assertNotEquals(document1, document2);
    }

    @Test
    public void test_equals_areNotEqual_differentWeight() {

        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", 3.0)
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        Assert.assertNotEquals(document1, document2);
    }

    @Test
    public void test_calculateCosineSimilarity_sameDocument() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        Assert.assertEquals(Math.cos(0), invokeCalculateCosineSimilarity(document1, document2), .001);
    }

    @Test
    public void test_calculateCosineSimilarity_extraTermSameVector() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("extraterm", 0.0)
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        Assert.assertEquals(Math.cos(0), invokeCalculateCosineSimilarity(document1, document2), .001);
    }

    @Test
    public void test_calculateCosineSimilarity_differentVectors() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", 7.0)
                .put("fair", 2.0)
                .put("my", 3.0)
                .put("pig", 5.0)
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", 1.0)
                .put("fair", 3.0)
                .put("my", 2.0)
                .put("pig", 4.0)
                .build());

        Assert.assertEquals(0.76338628536, invokeCalculateCosineSimilarity(document1, document2), .001);
    }

    @Test
    public void test_calculateDotProduct_sameDocument() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        Assert.assertEquals(1.441359041754604, invokeCalculateDotProduct(document1, document2), .001);
    }

    @Test
    public void test_calculateDotProduct_notSameDocument() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", 7.0)
                .put("fair", 2.0)
                .put("my", 3.0)
                .put("pig", 5.0)
                .build());

        final Document document2 = new Document(2, ImmutableMap.<String, Double>builder()
                .put("lady", 1.0)
                .put("fair", 2.0)
                .put("my", 3.0)
                .put("pig", 4.0)
                .build());

        Assert.assertEquals(7*1 + 2*2 + 3*3 + 5*4, invokeCalculateDotProduct(document1, document2), .001);
    }

    @Test
    public void test_getVectorLength() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", 7.0)
                .put("fair", 2.0)
                .put("my", 3.0)
                .put("pig", 5.0)
                .build());

        Assert.assertEquals(Math.sqrt(Math.pow(7, 2) + Math.pow(2, 2) + Math.pow(3, 2) + Math.pow(5, 2)),
                document1.getVectorLength(), .001);
    }

    @Test
    public void test_getVectorLength_updatedTermWeights() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", 20.0)
                .put("fair", 200.0)
                .put("my", 334.0)
                .put("pig", 5243.0)
                .build());

        document1.setTermWeights(ImmutableMap.<String, Double>builder()
                .put("lady", 7.0)
                .put("fair", 2.0)
                .put("my", 3.0)
                .put("pig", 5.0)
                .build());

        Assert.assertEquals(Math.sqrt(Math.pow(7, 2) + Math.pow(2, 2) + Math.pow(3, 2) + Math.pow(5, 2)),
                document1.getVectorLength(), .001);
    }

    private double invokeCalculateCosineSimilarity(final Document document1, final Document document2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method calculateCosineSimilarityMethod = document1.getClass().getDeclaredMethod("calculateCosineSimilarity", Document.class);
        calculateCosineSimilarityMethod.setAccessible(true);

        return (double) calculateCosineSimilarityMethod.invoke(document1, document2);
    }

    private double invokeCalculateDotProduct(final Document document1, final Document document2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method calculateDotProductMethod = document1.getClass().getDeclaredMethod("calculateDotProduct", Document.class);
        calculateDotProductMethod.setAccessible(true);

        return (double) calculateDotProductMethod.invoke(document1, document2);
    }

}
