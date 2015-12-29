package invertedindex.postingslist;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class DynamicArrayDocumentPosting implements DocumentPosting {

    private final int documentId;
    private final List<Integer> positions;

    public DynamicArrayDocumentPosting(final int documentId){
        this.documentId = documentId;
        this.positions = new ArrayList<>();
    }

    @Override
    public int getDocumentId() {
        return documentId;
    }

    @Override
    public void addAll(final Collection<Integer> positions) {
        positions.forEach(this::add);
    }

    @Override
    public void add(final int position) {
        positions.add(position);

        //if adding in order, don't need to do this. make a new class UnsortedDynamicArrayPostingList when want to optimize
        positions.sort(Comparator.naturalOrder());
    }

    @Override
    public int getNumPositions() {
        return positions.size();
    }

    public Collection<Integer> getPostings() {
        return positions;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{")
                .append(documentId)
                .append(": <")
                .append(Joiner.on(", ").join(positions))
                .append(">}")
                .toString();
    }

    @Override
    public boolean equals(final Object object) {
        //guaranteed to be in order
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        final DocumentPosting otherDocumentPosting = (DocumentPosting) object;

        return documentId == otherDocumentPosting.getDocumentId() &&
                positions.equals(otherDocumentPosting.getPostings());
    }

    @Override
    public int hashCode() {
        return positions.hashCode();
    }

}
