package indexer;

import java.util.ArrayList;
import java.util.List;

public class PostingsList {

    private final List<Integer> postings;

    public PostingsList() {
        postings = new ArrayList<>();
    }

    public void add(final int position) {
        postings.add(position);
    }

    public int size() {
        return postings.size();
    }


}
