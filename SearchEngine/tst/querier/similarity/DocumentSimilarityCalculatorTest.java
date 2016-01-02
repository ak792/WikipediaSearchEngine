package querier.similarity;

import com.google.common.collect.ImmutableList;
import corpus.Document;
import invertedindex.invertedindex.InvertedIndex;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.lang.reflect.InvocationTargetException;

public class DocumentSimilarityCalculatorTest {



    @Test
    public void test_calculateCosineSimilarity_sameDocument() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document3 = new Document(3, ImmutableList.<String>builder()
                .add("man")
                .add("with")
                .add("my")
                .add("plan")
                .build());

        final InvertedIndex invertedIndex = InvertedIndex.getInstance();
        invertedIndex.add(document1);
        invertedIndex.add(document2);
        invertedIndex.add(document3);

        final DocumentSimilarityCalculator documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);

        Assert.assertEquals(Math.cos(0), TestUtils.invokeCalculateCosineSimilarity(documentSimilarityCalculator, document1, document2), .001);
    }

    @Test
    public void test_calculateCosineSimilarity_extraTermSameVector() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("extraterm") //influences the vector length --> changes the denominator
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(2, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());


        final Document document3 = new Document(3, ImmutableList.<String>builder()
                .add("man")
                .add("with")
                .add("my")
                .add("plan")
                .build());


        final InvertedIndex invertedIndex = InvertedIndex.getInstance();
        invertedIndex.add(document1);
        invertedIndex.add(document2);
        invertedIndex.add(document3);

        final DocumentSimilarityCalculator documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);



        Assert.assertEquals(0.5386043776164268, TestUtils.invokeCalculateCosineSimilarity(documentSimilarityCalculator, document1, document2), .001);
    }

    @Test
    public void test_calculateCosineSimilarity_differentVectors() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("extraterm") //influences the vector length --> changes the denominator
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(2, ImmutableList.<String>builder()
                .add("man")
                .add("fair")
                .add("my")
                .add("pig")
                .build());


        final Document document3 = new Document(3, ImmutableList.<String>builder()
                .add("man")
                .add("with")
                .add("my")
                .add("plan")
                .build());


        final InvertedIndex invertedIndex = InvertedIndex.getInstance();
        invertedIndex.add(document1);
        invertedIndex.add(document2);
        invertedIndex.add(document3);

        final DocumentSimilarityCalculator documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);


        Assert.assertEquals(0.2827050442469313, TestUtils.invokeCalculateCosineSimilarity(documentSimilarityCalculator, document1, document2), .001);
    }

    @Test
    public void test_calculateDotProduct_sameDocumentOnlyDocuments() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final InvertedIndex invertedIndex = InvertedIndex.getInstance();
        invertedIndex.add(document1);
        invertedIndex.add(document2);

        final DocumentSimilarityCalculator documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);


        Assert.assertEquals(0, TestUtils.invokeCalculateDotProduct(documentSimilarityCalculator, document1, document2), .001);
    }

    @Test
    public void test_calculateDotProduct_notSameDocument() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(2, ImmutableList.<String>builder()
                .add("lady")
                .add("what")
                .add("my")
                .add("pig")
                .build());

        final Document document3 = new Document(3, ImmutableList.<String>builder()
                .add("man")
                .add("with")
                .add("my")
                .add("plan")
                .build());


        final InvertedIndex invertedIndex = InvertedIndex.getInstance();
        invertedIndex.add(document1);
        invertedIndex.add(document2);
        invertedIndex.add(document3);

        final DocumentSimilarityCalculator documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);


        Assert.assertEquals(0.32880390778633084, TestUtils.invokeCalculateDotProduct(documentSimilarityCalculator, document1, document2), .001);
    }

    @Test
    public void test_getVectorLength() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(2, ImmutableList.<String>builder()
                .add("lady")
                .add("what")
                .add("my")
                .add("pig")
                .build());

        final Document document3 = new Document(3, ImmutableList.<String>builder()
                .add("man")
                .add("with")
                .add("my")
                .add("plan")
                .build());

        final InvertedIndex invertedIndex = InvertedIndex.getInstance();
        invertedIndex.add(document1);
        invertedIndex.add(document2);
        invertedIndex.add(document3);

        final DocumentSimilarityCalculator documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);


        Assert.assertEquals(1.2392549651298206, TestUtils.invokeCalculateVectorLength(documentSimilarityCalculator, document1), .001);
        Assert.assertEquals(1.2392549651298206, TestUtils.invokeCalculateVectorLength(documentSimilarityCalculator, document2), .001);
        Assert.assertEquals(1.902852301792692, TestUtils.invokeCalculateVectorLength(documentSimilarityCalculator, document3), .001);
    }

}
