package invertedindex.invertedindex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document {

    private final int documentId;

    private Map<String, Double> termWeights;
    private double sumOfSquaredWeights;
    private double vectorLength;

    public Document(final int documentId, final Map<String, Double> termWeights) {
        this.documentId = documentId;
        setTermWeights(termWeights);
    }

    public void setTermWeights(final Map<String, Double> termWeights) {
        this.termWeights = termWeights;

        this.sumOfSquaredWeights = calculateSumOfSquaredWeights(termWeights);
        this.vectorLength = calculateVectorLength(sumOfSquaredWeights);
    }

    private double calculateSumOfSquaredWeights(final Map<String, Double> termWeights) {
        double sumOfSquaredWeights = 0;
        for (final String term : termWeights.keySet()) {
            sumOfSquaredWeights += Math.pow(termWeights.get(term), 2);
        }

        return sumOfSquaredWeights;
    }

    private double calculateVectorLength(final double sumOfSquaredWeights) {
        return Math.sqrt(sumOfSquaredWeights);
    }

    public double calculateSimilarity(final Document otherDoc) {
        return calculateCosineSimilarity(otherDoc);
    }

    private double calculateCosineSimilarity(final Document otherDoc) {
        final double dotProductOfVectors = calculateDotProduct(otherDoc);
        final double productOfVectorLengths = getVectorLength() * otherDoc.getVectorLength();

        return dotProductOfVectors / productOfVectorLengths;
    }

    private double calculateDotProduct(final Document otherDoc) {
        double dotProductOfVectors = 0;

        //if term in this but not in otherDoc --> otherDoc.getTfIdf(term) == 0 --> will add 0
        //if term in otherDoc but not in this --> getTfIdf(term) == 0 --> will add 0 --> don't need to check
        for (final String term : termWeights.keySet()) {
            dotProductOfVectors += getTfIdf(term) * otherDoc.getTfIdf(term);
        }

        return dotProductOfVectors;
    }

    public double getTfIdf(final String term) {
        return termWeights.containsKey(term) ? termWeights.get(term) : 0;
    }

    public double getVectorLength() {
        return vectorLength;
    }

    public int getDocumentId() {
        return documentId;
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

        if (getVectorLength() != otherDocument.getVectorLength()) {
            return false;
        }

        if (!termWeights.equals(otherDocument.termWeights)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "<Document " + documentId + ">: " + termWeights;
    }

}
