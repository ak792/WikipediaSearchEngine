package invertedindex;

import java.util.Collection;

public interface DocumentPosting {
    int getDocumentId();
    void add(int position);
    void addAll(final Collection<Integer> positions);
    int size();

    Collection<Integer> getPostings();
    String toString();
}
