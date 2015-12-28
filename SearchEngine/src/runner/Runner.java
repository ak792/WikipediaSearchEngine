package runner;

import com.jaunt.ResponseException;
import invertedindex.InvertedIndexFactory;
import invertedindex.InvertedIndex;
import scraper.Scraper;

public class Runner {


    public static void main(final String[] args) {
        try {
            final String url = "https://en.wikipedia.org/wiki/Alan_Turing";
            final Scraper scraper = new Scraper();
            final String document = scraper.getDocument(url);

            final InvertedIndex invertedIndex = InvertedIndexFactory.getInstance();
            invertedIndex.add(1, document);
            System.out.println(invertedIndex);
        }
        catch(final ResponseException e){
            e.printStackTrace();
        }
    }
}
