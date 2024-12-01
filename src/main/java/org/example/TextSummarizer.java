package org.example;

import java.util.*;

public class TextSummarizer {

    public static String extractiveSummary(String text, int numSentences) {
        String[] sentences = text.split("\\. ");
        Map<String, Integer> wordFrequency = new HashMap<>();

        for (String sentence : sentences) {
            String[] words = sentence.split(" ");
            for (String word : words) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }

        PriorityQueue<Map.Entry<String, Integer>> sortedWords = new PriorityQueue<>(
                (a, b) -> b.getValue().compareTo(a.getValue()));

        sortedWords.addAll(wordFrequency.entrySet());

        Set<String> mostFrequentWords = new HashSet<>();
        for (int i = 0; i < 50 && !sortedWords.isEmpty(); i++) {
            mostFrequentWords.add(sortedWords.poll().getKey());
        }

        PriorityQueue<String> summarySentences = new PriorityQueue<>(
                (a, b) -> Integer.compare(
                        countFrequentWords(b, mostFrequentWords),
                        countFrequentWords(a, mostFrequentWords)
                ));

        summarySentences.addAll(Arrays.asList(sentences));

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < numSentences && !summarySentences.isEmpty(); i++) {
            summary.append(summarySentences.poll()).append(". ");
        }

        return summary.toString();
    }

    private static int countFrequentWords(String sentence, Set<String> mostFrequentWords) {
        int count = 0;
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (mostFrequentWords.contains(word)) {
                count++;
            }
        }
        return count;
    }
}
