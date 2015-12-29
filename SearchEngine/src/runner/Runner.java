package runner;

import com.jaunt.ResponseException;
import invertedindex.invertedindex.InvertedIndex;
import scraper.Scraper;

public class Runner {


    public static void main(final String[] args) {
        try {
            final InvertedIndex invertedIndex = InvertedIndex.getInstance();

            final Scraper scraper = new Scraper();
            add(invertedIndex, scraper, "https://en.wikipedia.org/wiki/Alan_Turing", 1);
            add(invertedIndex, scraper, "https://en.wikipedia.org/wiki/Consistent_hashing", 2);

            System.out.println(invertedIndex);
        }
        catch(final ResponseException e) {
            e.printStackTrace();
        }
    }

    private static void add(final InvertedIndex invertedIndex,
                            final Scraper scraper,
                            final String url,
                            final int documentId) throws ResponseException {
        final String document = scraper.getDocument(url);
        invertedIndex.add(documentId, document);
    }
}
