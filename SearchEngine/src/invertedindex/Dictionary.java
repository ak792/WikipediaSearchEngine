package invertedindex;

import java.util.Comparator;

public interface Dictionary {

    boolean hasPostingsList(String term);
    PostingsList getPostingsList(String term);
    PostingsList getOrCreatePostingsList(String term);
    void setPostingsList(String term, PostingsList postingsList);
    boolean isStopWord(String term);
    int numTerms();

    String toString();
    String toString(Comparator<String> comparator);
}
