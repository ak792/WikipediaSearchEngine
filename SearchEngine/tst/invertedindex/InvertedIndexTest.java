package invertedindex;

import org.junit.Test;

public class InvertedIndexTest {

    final String[] TEST_TOKENS = {"a", "the", "my", "fair", "lady", "is", "a", "pig"};

    @Test
    public void test_invertedIndexCreation(){
        final InvertedIndex invertedIndex = InvertedIndex.Builder.builder()
                .build();

        for (int position = 0; position < TEST_TOKENS.length; position++) {
            final String token = TEST_TOKENS[position];
            invertedIndex.add(token, position);
        }

        //todo add asserts
    }


}
