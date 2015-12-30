package querier;


import invertedindex.invertedindex.Document;
import invertedindex.invertedindex.InvertedIndex;
import org.apache.lucene.util.PriorityQueue;
import querier.similarity.DocumentSimilarityCalculator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Querier {

    private final InvertedIndex invertedIndex;
    private final DocumentSimilarityCalculator documentSimilarityCalculator;

    public Querier(final InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
        this.documentSimilarityCalculator = new DocumentSimilarityCalculator(invertedIndex);
    }

    public List<Document> queryMostSimilar(final Document query, final int numResults) {
        final PriorityQueue<DocumentSimilarity> topDocumentSimilarities = findTopDocumentSimilarities(query, numResults);
        return createDocListFromDocSimPriorityQueue(topDocumentSimilarities);
    }

    private PriorityQueue<DocumentSimilarity> findTopDocumentSimilarities(final Document query, final int numResults) {
        final PriorityQueue<DocumentSimilarity> topDocumentSimilarities = new DocumentSimilarityPriorityQueue(numResults);

        for (final Document document : invertedIndex.getDocuments()) {
            final DocumentSimilarity documentSimilarity = new DocumentSimilarity(documentSimilarityCalculator, query, document);

            topDocumentSimilarities.insertWithOverflow(documentSimilarity);
        }

        return topDocumentSimilarities;
    }

    private List<Document> createDocListFromDocSimPriorityQueue(final PriorityQueue<DocumentSimilarity> topDocumentSimilarities) {
        final LinkedList<Document> topDocuments = new LinkedList<>();
        while (topDocumentSimilarities.size() > 0) {
            final DocumentSimilarity mostSimilarDocument = topDocumentSimilarities.pop();
            //will be returned in reverse order because the PriorityQueue is a min heap, so the min gets popped off first
            //correct this by prepending each one to a linked list
            topDocuments.addFirst(mostSimilarDocument.getDocument());
        }

        return topDocuments;
    }

    //decorator to store similarity to this specific query
    private class DocumentSimilarity {
        private final Document queryDocument;
        private final Document document;
        private final double similarity;

        public DocumentSimilarity(final DocumentSimilarityCalculator documentSimilarityCalculator, final Document queryDocument, final Document document) {
            this.queryDocument = queryDocument;
            this.document = document;
            this.similarity = documentSimilarityCalculator.calculateQuerySimilarity(queryDocument, document);
        }

        //just in case, not sure if we need these
        public Document getQueryDocument() {
            return queryDocument;
        }

        public Document getDocument() {
            return document;
        }

        public double getSimilarity() {
            return similarity;
        }
    }

    private class DocumentSimilarityPriorityQueue extends PriorityQueue<DocumentSimilarity> {

        public DocumentSimilarityPriorityQueue(final int maxSize) {
            super(maxSize);
        }

        @Override
        public boolean lessThan(final DocumentSimilarity documentSimilarity1, final DocumentSimilarity documentSimilarity2) {
            return documentSimilarity1.getSimilarity() < documentSimilarity2.getSimilarity();
        }

    }





}
