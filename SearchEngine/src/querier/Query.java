package querier;

import invertedindex.invertedindex.Document;

import java.util.HashMap;
import java.util.Map;

public class Query extends Document {

    public Query(final int documentId, final Map<String, Double> termWeights) {
        super(documentId, termWeights);

        setTermWeights(cleanTermWeights(termWeights));
    }

    private Map<String, Double> cleanTermWeights(final Map<String, Double> termWeights) {
        final Map<String, Double> cleanedTermWeights = new HashMap<>();
        for (final String key : termWeights.keySet()) {
            cleanedTermWeights.put(key, 1.0);
        }

        return cleanedTermWeights;
    }

    @Override
    public double getTfIdf(final String term) {
        return 1.0;
    }
}
