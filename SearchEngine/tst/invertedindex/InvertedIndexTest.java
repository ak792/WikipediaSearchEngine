package invertedindex;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import invertedindex.dictionary.Dictionary;
import invertedindex.invertedindex.Document;
import invertedindex.invertedindex.InvertedIndex;
import invertedindex.postingslist.DynamicArrayPositionalPostingsList;
import invertedindex.postingslist.PostingsList;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
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
        fairPostingsList.addAll(testDocument1Id, ImmutableList.of(1, 3));
        fairPostingsList.addAll(testDocument2Id, ImmutableList.of(4));
        Assert.assertEquals(fairPostingsList, termsToPostingsList.get("fair"));

        final PostingsList ladyPostingsList = new DynamicArrayPositionalPostingsList();
        ladyPostingsList.addAll(testDocument1Id, ImmutableList.of(2));
        Assert.assertEquals(ladyPostingsList, termsToPostingsList.get("lady"));

        final PostingsList myPostingsList = new DynamicArrayPositionalPostingsList();
        myPostingsList.addAll(testDocument1Id, ImmutableList.of(0));
        Assert.assertEquals(myPostingsList, termsToPostingsList.get("my"));

        final PostingsList pigPostingsList = new DynamicArrayPositionalPostingsList();
        pigPostingsList.addAll(testDocument1Id, ImmutableList.of(4));
        Assert.assertEquals(pigPostingsList, termsToPostingsList.get("pig"));

        final PostingsList whatPostingsList = new DynamicArrayPositionalPostingsList();
        whatPostingsList.addAll(testDocument2Id, ImmutableList.of(0));
        Assert.assertEquals(whatPostingsList, termsToPostingsList.get("what"));

        final PostingsList lovelyPostingsList = new DynamicArrayPositionalPostingsList();
        lovelyPostingsList.addAll(testDocument2Id, ImmutableList.of(1, 2));
        Assert.assertEquals(lovelyPostingsList, termsToPostingsList.get("lovely"));

        final PostingsList tablePostingsList = new DynamicArrayPositionalPostingsList();
        tablePostingsList.addAll(testDocument2Id, ImmutableList.of(3));
        Assert.assertEquals(tablePostingsList, termsToPostingsList.get("table"));



        final int numDocs = 2;

        final Document document1 = new Document(1, ImmutableMap.<String, Double>builder()
                .put("lady", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(2, numDocs, 2))
                .put("my", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pig", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());

        final Document document2 = new Document(2, ImmutableMap.<String, Double>builder()
                .put("lovely", TestUtils.calculateTfIdf(2, numDocs, 1))
                .put("what", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("fair", TestUtils.calculateTfIdf(1, numDocs, 2))
                .put("table", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());


        final Map<Document, Map<String, Double>> documents = TestUtils.getDocuments(invertedIndex);
        for (final Document document : documents.keySet()) {
            if (document.getDocumentId() == document1.getDocumentId()) {
                Assert.assertEquals(document1, document);
            } else if (document.getDocumentId() == document2.getDocumentId()) {
                Assert.assertEquals(document2, document);
            }
        }
    }

    @Test
    public void test_invertedIndexCreation2() throws NoSuchFieldException, IllegalAccessException {

        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                .withDictionaryType(InvertedIndex.DictionaryType.Hash)
                .withStopWords(new HashSet<>())
                .build();

        invertedIndex.add(1, "a the my fair lady is a fair pig");
        invertedIndex.add(2, "what a lovely lovely table fair");
        invertedIndex.add(3, "isnt everything just a great pleasure");

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

        final Document document3 = new Document(2, ImmutableMap.<String, Double>builder()
                .put("isnt", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("everything", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("just", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("a", TestUtils.calculateTfIdf(1, numDocs, 3))
                .put("great", TestUtils.calculateTfIdf(1, numDocs, 1))
                .put("pleasure", TestUtils.calculateTfIdf(1, numDocs, 1))
                .build());


        final Map<Document, Map<String, Double>> documents = TestUtils.getDocuments(invertedIndex);
        for (final Document document : documents.keySet()) {
            if (document.getDocumentId() == document1.getDocumentId()) {
                Assert.assertEquals(document1, document);
            } else if (document.getDocumentId() == document2.getDocumentId()) {
                Assert.assertEquals(document2, document);
            } else if (document.getDocumentId() == document3.getDocumentId()) {
                Assert.assertEquals(document3, document);
            }
        }
    }

    @Test
    public void test_Tokenize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

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



    @Test
    public void test_getInverseDocumentFrequency() throws NoSuchFieldException, IllegalAccessException {
        //given
        final InvertedIndex invertedIndex = TestUtils.constructInvertedIndex();
        final int numDocs = 2;

        //then
        final double marginOfError = .0001; //for double rounding issues
        Assert.assertEquals(Math.log(numDocs / 2), invertedIndex.getInverseDocumentFrequency("fair"), marginOfError);
        Assert.assertEquals(Math.log(numDocs / 1), invertedIndex.getInverseDocumentFrequency("pig"), marginOfError);
        Assert.assertEquals(Math.log(numDocs / 1), invertedIndex.getInverseDocumentFrequency("lovely"), marginOfError);
        Assert.assertEquals(0, invertedIndex.getInverseDocumentFrequency("notinanydocument"), marginOfError);
    }

    @Test
    public void test_getTfIdf() {
        //given
        final InvertedIndex invertedIndex = TestUtils.constructInvertedIndex();
        final int numDocs = 2;

        //then
        final double marginOfError = .0001; //for double rounding issues
        Assert.assertEquals(TestUtils.calculateTfIdf(2, numDocs, 2), invertedIndex.getTfIdf("fair", 1), marginOfError);
        Assert.assertEquals(TestUtils.calculateTfIdf(1, numDocs, 2), invertedIndex.getTfIdf("fair", 2), marginOfError);
        Assert.assertEquals(TestUtils.calculateTfIdf(1, numDocs, 1), invertedIndex.getTfIdf("pig", 1), marginOfError);
        Assert.assertEquals(TestUtils.calculateTfIdf(0, numDocs, 1), invertedIndex.getTfIdf("lovely", 1), marginOfError);
        Assert.assertEquals(TestUtils.calculateTfIdf(2, numDocs, 1), invertedIndex.getTfIdf("lovely", 2), marginOfError);
        Assert.assertEquals(TestUtils.calculateTfIdf(0, numDocs, 0), invertedIndex.getTfIdf("notinanydocument", 2), marginOfError);
    }

}
