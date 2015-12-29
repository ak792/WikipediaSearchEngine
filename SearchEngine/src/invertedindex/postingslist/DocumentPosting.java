package invertedindex.postingslist;

import java.util.Collection;

//{documentId, <position1, position2, ...>}
public interface DocumentPosting {
    int getDocumentId();
    void add(int position);
    void addAll(final Collection<Integer> positions);
    int getNumPositions();

    Collection<Integer> getPostings();
    String toString();
}
