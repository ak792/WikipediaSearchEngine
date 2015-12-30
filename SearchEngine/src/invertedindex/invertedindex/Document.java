package invertedindex.invertedindex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Document {

    private final int documentId;

    private final Map<String, Set<Integer>> terms;

    //be careful terms only are here once
    public Document(final int documentId, final Map<String, Set<Integer>> terms) {
        this.documentId = documentId;
        this.terms = terms;
    }

    public Document(final int documentId, final List<String> terms) {
        this.documentId = documentId;

        this.terms = InvertedIndex.getTermsMap(terms);
    }

    public int getDocumentId() {
        return documentId;
    }

    public Map<String, Set<Integer>> getTerms() {
        //copy?
        return terms;
    }

    @Override
    public int hashCode() {
        return documentId;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        if (this == object) {
            return true;
        }

        final Document otherDocument = (Document) object;

        if (getDocumentId() != otherDocument.getDocumentId()) {
            return false;
        }

        return getTerms().equals(otherDocument.getTerms());

    }

    @Override
    public String toString() {
        return "<Document " + documentId + ">: " + terms;
    }

}
