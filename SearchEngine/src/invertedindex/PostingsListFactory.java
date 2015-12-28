package invertedindex;

public class PostingsListFactory {

    public enum PostingsListType {
        DynamicArray;
    }


    private final PostingsListType postingsListType;

    public PostingsListFactory(final PostingsListType postingsListType) {
        this.postingsListType = postingsListType;
    }

    public PostingsList getInstance() {
        final PostingsList postingsList;
        switch (postingsListType) {
            case DynamicArray:
                postingsList = new DynamicArrayPostingsList();
                break;

            default:
                throw new RuntimeException(String.format("PostingsList type %s not recognized", postingsListType));
        }

        return postingsList;
    }

}
