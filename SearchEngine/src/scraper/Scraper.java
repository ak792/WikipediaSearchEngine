package scraper;

import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

public class Scraper {

    public String getDocument(final String url) throws ResponseException {
        final UserAgent userAgent = new UserAgent();
        userAgent.visit(url);

        return getAllParagraphTextContentNoPunctuation(userAgent);
    }

    public String getAllParagraphTextContentNoPunctuation(final UserAgent userAgent) {
        final String allParagraphTextContent = userAgent.doc.findEach("<p>").innerText();
        final String allParagraphTextContentNoPunctuation = allParagraphTextContent.replaceAll("[^a-zA-Z ]", "");

        return allParagraphTextContentNoPunctuation;
    }

}
