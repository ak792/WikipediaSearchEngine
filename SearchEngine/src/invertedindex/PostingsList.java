package invertedindex;

import java.util.Collection;
import java.util.List;

public interface PostingsList {
    void addAll(int documentId, Collection<Integer> positions);
    void add(int documentId, int position);
    int numDocuments();

    boolean hasDocumentPosting(int documentId);
    DocumentPosting getDocumentPosting(int documentId);
    List<DocumentPosting> getPostings();
}
