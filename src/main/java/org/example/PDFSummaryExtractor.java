package org.example;

import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PDFSummaryExtractor {

    public static void main(String[] args) {
        // Start monitoring heap memory asynchronously
        startHeapMemoryMonitoring();

        // Set up properties for the StanfordCoreNLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos,lemma,ner,parse,sentiment");
        props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/english-left3words-distsim.tagger");

        // Create the StanfordCoreNLP pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String pdfPath = "/Users/hardik.agarwal/Downloads/learn/java/iPDF/ksolves.pdf";

        try {
            // Step 1: Extract Text from PDF
            String text = PDFExtractor.extractTextFromPDF(pdfPath);

            // Step 2: Preprocess the Text
            String cleanedText = TextPreprocessor.preprocessText(text);

            // Step 3: Annotate the Preprocessed Text using the pipeline
            CoreDocument document = new CoreDocument(cleanedText);
            pipeline.annotate(document);

            // Optional: Use the annotated document for further processing
            // Example: Extracting sentences, tokens, etc.
            List<CoreSentence> sentences = document.sentences();
            for (CoreSentence sentence : sentences) {
                System.out.println("Annotated Sentence: " + sentence.text());
                System.out.println("Part-of-Speech Tags: " + sentence.posTags());
            }

            // Step 4: Summarize the Text
            String summary = TextSummarizer.extractiveSummary(cleanedText, 5);

            // Step 5: Extract Data Points
            List<Pair<String, String>> dataPoints = DataPointExtractor.extractDataPoints(summary);

            // Print Summary
            System.out.println("Summary:");
            System.out.println(summary);

            // Print Data Points
            System.out.println("\nData Points:");
            for (Pair<String, String> dp : dataPoints) {
                System.out.println(dp.first() + " : " + dp.second());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startHeapMemoryMonitoring() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        Runnable memoryMonitor = () -> printHeapMemoryInfo();

        // Schedule the memory monitoring task to run every 20 seconds
        executorService.scheduleAtFixedRate(memoryMonitor, 0, 20, TimeUnit.SECONDS);
    }

    private static void printHeapMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

        long maxHeapSize = heapMemoryUsage.getMax(); // Maximum heap size
        long usedHeapSize = heapMemoryUsage.getUsed(); // Used heap size
        long committedHeapSize = heapMemoryUsage.getCommitted(); // Committed heap size

        System.out.println("\nHeap Memory Info:");
        System.out.println("Max Heap Size: " + formatSize(maxHeapSize));
        System.out.println("Used Heap Size: " + formatSize(usedHeapSize));
        System.out.println("Committed Heap Size: " + formatSize(committedHeapSize));
    }

    private static String formatSize(long size) {
        if (size < 1024) return size + " Bytes";
        int unit = 1024;
        String[] units = {"Bytes", "KB", "MB", "GB", "TB"};
        int i = 0;
        while (size >= unit && i < units.length - 1) {
            size /= unit;
            i++;
        }
        return size + " " + units[i];
    }
}
