package dictionary;

import invertedindex.dictionary.Dictionary;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;


public class DictionaryTest {

    @Test
    public void test_getTermFrequency() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Dictionary dictionary = TestUtils.constructInvertedIndexDictionary();

        //then
        Assert.assertEquals(2, dictionary.getTermFrequency("fair", 1));
        Assert.assertEquals(1, dictionary.getTermFrequency("fair", 2));
        Assert.assertEquals(1, dictionary.getTermFrequency("pig", 1));
        Assert.assertEquals(0, dictionary.getTermFrequency("lovely", 1));
        Assert.assertEquals(2, dictionary.getTermFrequency("lovely", 2));
        Assert.assertEquals(0, dictionary.getTermFrequency("notinanydocument", 2));
    }


}
