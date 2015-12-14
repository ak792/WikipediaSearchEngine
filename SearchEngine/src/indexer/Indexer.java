package indexer;

import com.jaunt.ResponseException;
import scraper.Scraper;

public class Indexer {

    private final Scraper scraper;

    public Indexer() {
        scraper = new Scraper();
    }

    public InvertedIndex createInvertedIndex(final String url) throws ResponseException {
        final String document = scraper.getDocument(url);
        final String[] tokens = document.split(" ");

        final InvertedIndex invertedIndex = new InvertedIndex();

        for (int position = 0; position < tokens.length; position++) {
            final String token = tokens[position];
            invertedIndex.add(token, position);
        }

        return invertedIndex;
    }


}
