package invertedindex;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class InvertedIndexTest {


    @Test
    public void test_invertedIndexCreation() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Set<String> stopWords = ImmutableSet.of("a", "the", "is");
        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                .withDictionaryType(InvertedIndex.DictionaryType.Hash)
                .withStopWords(stopWords)
                .build();

        final int testDocument1Id = 1;
        final String testDocument1 = "a the my fair lady is a fair pig";
        final String[] testTokens1 = {"a", "the", "my", "fair", "lady", "is", "a", "fair", "pig"};

        final int testDocument2Id = 2;
        final String testDocument2 = "what a lovely lovely    table fair";
        final String[] testTokens2 = {"what", "a", "lovely", "lovely", "table", "fair"};

        //when
        invertedIndex.add(testDocument1Id, testDocument1);
        invertedIndex.add(testDocument2Id, testDocument2);


        //then
        final Field dictionaryField = invertedIndex.getClass().getDeclaredField("dictionary");
        dictionaryField.setAccessible(true);
        final Dictionary dictionary = (Dictionary) dictionaryField.get(invertedIndex);

        //be careful that any dictionaries have this field
        final Field termsToPostingsListsField = dictionary.getClass().getDeclaredField("termsToPostingsLists");
        termsToPostingsListsField.setAccessible(true);
        final Map<String, PostingsList> termsToPostingsList = (Map<String, PostingsList>) termsToPostingsListsField.get(dictionary);

        //test document 1 was added properly

        //Asserts that stop words were ignored and non-stop words were added
        for (final String token : testTokens1) {
            final boolean shouldContainToken = !stopWords.contains(token);
            Assert.assertEquals(shouldContainToken, termsToPostingsList.containsKey(token));
        }

        for (final String token : testTokens2) {
            final boolean shouldContainToken = !stopWords.contains(token);
            Assert.assertEquals(shouldContainToken, termsToPostingsList.containsKey(token));
        }

        //Asserts that all postings lists were properly created
        final PostingsList fairPostingsList = new DynamicArrayPositionalPostingsList();
        fairPostingsList.addAll(testDocument1Id, ImmutableList.of(3, 7));
        fairPostingsList.addAll(testDocument2Id, ImmutableList.of(5));
        Assert.assertEquals(fairPostingsList, termsToPostingsList.get("fair"));

        final PostingsList ladyPostingsList = new DynamicArrayPositionalPostingsList();
        ladyPostingsList.addAll(testDocument1Id, ImmutableList.of(4));
        Assert.assertEquals(ladyPostingsList, termsToPostingsList.get("lady"));

        final PostingsList myPostingsList = new DynamicArrayPositionalPostingsList();
        myPostingsList.addAll(testDocument1Id, ImmutableList.of(2));
        Assert.assertEquals(myPostingsList, termsToPostingsList.get("my"));

        final PostingsList pigPostingsList = new DynamicArrayPositionalPostingsList();
        pigPostingsList.addAll(testDocument1Id, ImmutableList.of(8));
        Assert.assertEquals(pigPostingsList, termsToPostingsList.get("pig"));

        final PostingsList whatPostingsList = new DynamicArrayPositionalPostingsList();
        whatPostingsList.addAll(testDocument2Id, ImmutableList.of(0));
        Assert.assertEquals(whatPostingsList, termsToPostingsList.get("what"));

        final PostingsList lovelyPostingsList = new DynamicArrayPositionalPostingsList();
        lovelyPostingsList.addAll(testDocument2Id, ImmutableList.of(2, 3));
        Assert.assertEquals(lovelyPostingsList, termsToPostingsList.get("lovely"));

        final PostingsList tablePostingsList = new DynamicArrayPositionalPostingsList();
        tablePostingsList.addAll(testDocument2Id, ImmutableList.of(4));
        Assert.assertEquals(tablePostingsList, termsToPostingsList.get("table"));
    }

    @Test
    public void testTokenize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //given
        final String testString = "My fair lady  is a   pig";

        //when
        //Tokenize is a private method, so need to make it accessible
        final Method tokenizeMethod = InvertedIndex.class.getDeclaredMethod("tokenize", String.class);
        tokenizeMethod.setAccessible(true);
        final String[] tokens = (String[]) tokenizeMethod.invoke(null, testString);

        //then
        final String[] expectedTokens = {"My", "fair", "lady", "is", "a", "pig"};
        Assert.assertArrayEquals(expectedTokens, tokens);
    }



}
