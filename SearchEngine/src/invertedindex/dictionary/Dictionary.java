package invertedindex.dictionary;

import invertedindex.postingslist.PostingsList;

import java.util.Comparator;

public interface Dictionary {

    boolean hasPostingsList(String term);
    PostingsList getPostingsList(String term);
    PostingsList getOrCreatePostingsList(String term);
    boolean isStopWord(String term);

    int getTermFrequency(String term, int documentId);

    String toString();
    String toString(Comparator<String> comparator);
}
