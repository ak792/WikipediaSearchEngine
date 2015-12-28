package invertedindex;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class DynamicArrayPositionalPostingsList implements PostingsList {

    private final List<Integer> postings;

    public DynamicArrayPositionalPostingsList() {
        postings = new ArrayList<>();
    }

    @Override
    public void addAll(final Collection<Integer> positions) {
        positions.forEach(this::add);
    }

    @Override
    public void add(final int position) {
        postings.add(position);

        //if adding in order, don't need to do this. make a new class UnsortedDynamicArrayPostingList when want to optimize
        postings.sort(Comparator.naturalOrder());
    }

    @Override
    public int size() {
        return postings.size();
    }

    public String toString() {
        return Joiner.on(", ").join(postings);
    }

    public Collection<Integer> getPostings() {
        return postings;
    }

    @Override
    public boolean equals(final Object object) {
        //guaranteed to be in order
        if (object == null) {
            return false;
        }

        if (!(object instanceof DynamicArrayPositionalPostingsList)) {
            return false;
        }

        final PostingsList otherPostingsList = (DynamicArrayPositionalPostingsList) object;

        return postings.equals(otherPostingsList.getPostings());
    }

    @Override
    public int hashCode() {
        return postings.hashCode();
    }

}
