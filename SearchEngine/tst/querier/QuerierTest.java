package querier;

import com.google.common.collect.ImmutableList;
import invertedindex.invertedindex.Document;
import invertedindex.invertedindex.InvertedIndex;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class QuerierTest {

    @Test
    public void queryMostSimilarTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //remove
        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                .withDictionaryType(InvertedIndex.DictionaryType.Hash)
                .withStopWords(new HashSet<>())
                .build();

        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("a")
                .add("the")
                .add("my")
                .add("fair")
                .add("lady")
                .add("is")
                .add("a")
                .add("fair")
                .add("pig")
                .build());

        final Document document2 = new Document(2, ImmutableList.<String>builder()
                .add("what")
                .add("a")
                .add("lovely")
                .add("lovely")
                .add("table")
                .add("lady")
                .add("fair")
                .build());

        final Document document3 = new Document(3, ImmutableList.<String>builder()
                .add("isnt")
                .add("everything")
                .add("just")
                .add("a")
                .add("great")
                .add("pleasure")
                .build());

        invertedIndex.add(document1);
        invertedIndex.add(document2);
        invertedIndex.add(document3);


        final Querier querier = new Querier(invertedIndex);
        final Document query = new Document(0, ImmutableList.of("lady", "fair", "pig"));
        final List<Document> topDocuments = querier.queryMostSimilar(query, 2);

        final List<Document> expectedTopDocuments = Arrays.asList(document1, document2);

        Assert.assertEquals(expectedTopDocuments, topDocuments);
    }
}
