package corpus;

import invertedindex.invertedindex.InvertedIndex;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Document {

    private final int documentId;
    private final Map<String, Set<Integer>> terms;

    public Document(final int documentId, final List<String> terms) {
        this(documentId, InvertedIndex.createTermsMap(terms));
    }

    public Document(final int documentId, final Map<String, Set<Integer>> terms) {
        this.documentId = documentId;
        this.terms = terms;
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

        return getDocumentId() == otherDocument.getDocumentId() &&
                getTerms().equals(otherDocument.getTerms());

    }

    @Override
    public String toString() {
        return "<Document " + documentId + ">";
    }

}
