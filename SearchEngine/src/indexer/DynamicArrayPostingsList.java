package indexer;

import java.util.ArrayList;
import java.util.List;

public class DynamicArrayPostingsList {

    private final List<Integer> postings;

    public DynamicArrayPostingsList() {
        postings = new ArrayList<>();
    }

    public void add(final int position) {
        postings.add(position);
    }

    public int size() {
        return postings.size();
    }


}
