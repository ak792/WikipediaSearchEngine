package invertedindex;

import com.google.common.collect.ImmutableList;
import corpus.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DocumentTest {


    @Test
    public void test_equals_areEqual() {
        final Document document1 = getDocument1();
        final Document document2 = getDocument1();

        Assert.assertEquals(document1, document2);
    }

    @Test
    public void test_equals_areNotEqual_differentTerm() {
        final Document document1 = getDocument1();
        final Document document2 = getDocument1DifferentWords();

        Assert.assertNotEquals(document1, document2);
    }

    public Document getDocument1() {
        return new Document(1, Arrays.asList("lady", "fair", "my", "pig"));
    }

    public Document getDocument1DifferentWords() {
        return new Document(1, Arrays.asList("f", "fair", "my", "pig"));
    }


}
