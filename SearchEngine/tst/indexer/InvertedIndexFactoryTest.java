package indexer;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvertedIndexFactoryTest {

    @Test
    public void testTokenize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final String testString = "My fair lady  is a   pig";

        //Tokenize is a private method, so need to make it accessible
        final Method tokenizeMethod = InvertedIndexFactory.class.getDeclaredMethod("tokenize", String.class);
        tokenizeMethod.setAccessible(true);

        final InvertedIndexFactory invertedIndexFactory = new InvertedIndexFactory();
        final String[] tokens = (String[]) tokenizeMethod.invoke(invertedIndexFactory, testString);

        final String[] expectedTokens = {"My", "fair", "lady", "is", "a", "pig"};
        Assert.assertArrayEquals(expectedTokens, tokens);
    }

}
