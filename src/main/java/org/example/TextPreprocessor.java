package org.example;

public class TextPreprocessor {

    public static String preprocessText(String text) {
        // Remove unnecessary whitespace and special characters
        text = text.replaceAll("\\s+", " ");
        text = text.replaceAll("[^\\w\\s]", "");
        return text;
    }
}
