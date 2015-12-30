package invertedindex;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import invertedindex.invertedindex.Document;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DocumentTest {


    @Test
    public void test_equals_areEqual() {
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

        Assert.assertEquals(document1, document2);
    }

    @Test
    public void test_equals_areNotEqual_differentTerm() {

        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableList.<String>builder()
                .add("lady")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        final Document document2 = new Document(1, ImmutableList.<String>builder()
                .add("f")
                .add("fair")
                .add("my")
                .add("pig")
                .build());

        Assert.assertNotEquals(document1, document2);
    }

}
