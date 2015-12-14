package scraper;

import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import indexer.Indexer;
import indexer.InvertedIndex;

public class Scraper {

    public static void main(final String[] args) {
        try {
            final String url = "https://en.wikipedia.org/wiki/Alan_Turing";
//            final String text = getDocument(url);
            final InvertedIndex invertedIndex = new Indexer().createInvertedIndex(url);
            System.out.println(invertedIndex);
        }
        catch(final ResponseException e){
            e.printStackTrace();
        }
    }

    public static String getDocument(final String url) throws ResponseException {
        final UserAgent userAgent = new UserAgent();
        userAgent.visit(url);

        return getAllParagraphTextContentNoPunctuation(userAgent);
    }

    public static String getAllParagraphTextContentNoPunctuation(final UserAgent userAgent) {
        final String allParagraphTextContent = userAgent.doc.findEach("<p>").innerText();
        final String allParagraphTextContentNoPunctuation = allParagraphTextContent.replaceAll("[^a-zA-Z ]", "");

        return allParagraphTextContentNoPunctuation;
    }

}
