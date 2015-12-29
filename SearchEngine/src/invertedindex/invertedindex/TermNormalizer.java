package invertedindex.invertedindex;

import java.util.Locale;

public class TermNormalizer {

    public TermNormalizer() {

    }

    public String normalizeTerm(final String term) {
        //Normalize words to lower case
        //Note: While this will help in many cases (such as considering a word at the beginning of the sentence
        //the same as if it were in the middle), this can hurt in others (for example, an acronym that spells a word
        //will be considered the same as the word

        return term.toLowerCase(Locale.ENGLISH);
    }
}
