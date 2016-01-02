package querier.similarity;

import corpus.Document;
import invertedindex.invertedindex.InvertedIndex;

public class DocumentSimilarityCalculator {

    private final InvertedIndex invertedIndex;

    public DocumentSimilarityCalculator(final InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    //test these querymethods
    public double calculateQuerySimilarity(final Document query, final Document doc) {
        return calculateQueryCosineSimilarity(query, doc);
    }

    private double calculateQueryCosineSimilarity(final Document query, final Document doc) {
        final double dotProductOfVectors = calculateQueryDotProductOfVectors(query, doc);
        final double productOfVectorLengths = calculateUnitWeightsVectorLength(query) * calculateVectorLength(doc);

        final double marginOfError = .00000000001;
        final boolean isProductOfVectorLengthsZero = productOfVectorLengths < marginOfError;

        return (!isProductOfVectorLengthsZero ? dotProductOfVectors / productOfVectorLengths : 0);
    }

    private double calculateQueryDotProductOfVectors(final Document query, final Document doc) {
        return query.getTerms().keySet().stream()
                .filter(term -> doc.getTerms().containsKey(term))
                .mapToDouble(term -> invertedIndex.getTfIdf(term, doc.getDocumentId()))
                .sum();
    }

    //calculate length assuming all weights are 1
    private double calculateUnitWeightsVectorLength(final Document doc) {
        return Math.sqrt(doc.getTerms().size());
    }

    public double calculateSimilarity(final Document doc1, final Document doc2) {
        return calculateCosineSimilarity(doc1, doc2);
    }

    private double calculateCosineSimilarity(final Document doc1, final Document doc2) {
        final double dotProductOfVectors = calculateDotProduct(doc1, doc2);
        final double productOfVectorLengths = calculateVectorLength(doc1) * calculateVectorLength(doc2);

        final double marginOfError = .00000000001;
        final boolean isProductOfVectorLengthsZero = productOfVectorLengths < marginOfError;

        return (!isProductOfVectorLengthsZero ? dotProductOfVectors / productOfVectorLengths : 0);
    }

    private double calculateDotProduct(final Document doc1, final Document doc2) {
        double dotProductOfVectors = 0;

        //if term in this but not in otherDoc --> otherDoc.getTfIdf(term) == 0 --> will add 0
        //if term in otherDoc but not in this --> getTfIdf(term) == 0 --> will add 0 --> don't need to check
        for (final String term : doc1.getTerms().keySet()) {
            dotProductOfVectors += invertedIndex.getTfIdf(term, doc1.getDocumentId()) * invertedIndex.getTfIdf(term, doc2.getDocumentId());
        }

        return dotProductOfVectors;
    }

    private double calculateVectorLength(final Document doc) {
        double sumOfSquaredWeights = 0;
        for (final String term : doc.getTerms().keySet()) {
            sumOfSquaredWeights += Math.pow(invertedIndex.getTfIdf(term, doc.getDocumentId()), 2);
        }

        return Math.sqrt(sumOfSquaredWeights);
    }


}
