package org.example;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.*;

import java.util.*;

public class DataPointExtractor {

    public static List<Pair<String, String>> extractDataPoints(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        List<Pair<String, String>> dataPoints = new ArrayList<>();
        for (CoreEntityMention em : document.entityMentions()) {
            dataPoints.add(new Pair<>(em.text(), em.entityType()));
        }

        return dataPoints;
    }
}
