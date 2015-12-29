package invertedindex.postingslist;

import java.util.Collection;
import java.util.List;

//a collection of DocumentPostings
public interface PostingsList {
    void addAll(int documentId, Collection<Integer> positions);
    void add(int documentId, int position);
    int getNumDocuments();

    int getTermFrequency(int documentId);

    boolean hasDocumentPosting(int documentId);
    DocumentPosting getDocumentPosting(int documentId);
    List<DocumentPosting> getPostings();
}
