package invertedindex;

import java.util.Comparator;

public interface Dictionary {

    boolean hasPostingsList(String term);
    PostingsList getPostingsList(String term);
    PostingsList getOrCreatePostingsList(String term);
    void setPostingsList(String term, PostingsList postingsList);
    boolean isStopWord(String term);
    int size();

    int findMaxPostingsListSize();
    int findMinPostingsListSize();

    String getAllContents();
    String getAllContents(Comparator<String> comparator);

}
