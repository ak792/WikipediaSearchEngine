package invertedindex;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DynamicArrayPositionalPostingsList implements PostingsList {

    //why not use a map?
    private final List<DocumentPosting> documentPostings;

    public DynamicArrayPositionalPostingsList() {
        documentPostings = new ArrayList<>();
    }

    @Override
    public void addAll(final int documentId, final Collection<Integer> positions) {
        positions.forEach(position -> add(documentId, position));
    }

    @Override
    public void add(final int documentId, final int position) {
        //since adding in order, don't need to sort to get in ascending order
        final DocumentPosting documentPosting = getOrCreateDocumentPosting(documentId);
        documentPosting.add(position);
    }

    @Override
    public int numDocuments() {
        return documentPostings.size();
    }

    @Override
    public List<DocumentPosting> getPostings() {
        return documentPostings;
    }

    @Override
    public boolean hasDocumentPosting(final int documentId) {
        return getDocumentPostingOptional(documentId).isPresent();
    }

    public DocumentPosting getOrCreateDocumentPosting(final int documentId) {
        final DocumentPosting documentPosting;
        if (getDocumentPostingOptional(documentId).isPresent()) {
            documentPosting = getDocumentPostingOptional(documentId).get();
        } else {
            //not sure how to get Optional version working
            documentPosting = new DynamicArrayDocumentPosting(documentId);
            documentPostings.add(documentPosting);
        }

        return documentPosting;
    }

    @Override
    public DocumentPosting getDocumentPosting(final int documentId) {
        return getDocumentPostingOptional(documentId).orElseThrow(() -> new NoSuchElementException("No element with documentId " + documentId));
    }

    public Optional<DocumentPosting> getDocumentPostingOptional(final int documentId) {
        return documentPostings.stream()
                .filter(currDocPosting -> currDocPosting.getDocumentId() == documentId)
                .findAny();
    }

    @Override
    public String toString() {
        return Joiner.on("; ").join(documentPostings);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        //guaranteed to be in order
        final PostingsList otherPostingsList = (PostingsList) object;

        return documentPostings.equals(otherPostingsList.getPostings());
    }

    @Override
    public int hashCode() {
        return documentPostings.hashCode();
    }

}
