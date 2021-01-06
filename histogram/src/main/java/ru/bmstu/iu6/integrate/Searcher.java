package ru.bmstu.iu6.integrate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import ru.bmstu.iu6.Evaluator;
import ru.bmstu.iu6.Parsing;
import ru.bmstu.iu6.element.HElement;
import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.histogram.Histogram;
import ru.bmstu.iu6.operation.OperationBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Searcher {

    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    Directory directory;

    public Searcher(String indexDirectoryPath) throws IOException {
        directory = FSDirectory.open(Paths.get(indexDirectoryPath));
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
    }

    public void search(String expr) throws IOException {
        Parsing parsing = new Parsing();
        Vector<String> expression = parsing.parseString(expr);

//        BooleanQuery booleanQuery = new BooleanQuery();
//        for (String s : expression) {
//            System.out.println(s);
//            Term term = new Term(s, "0");
//            Query ee = new TermQuery(term);
//            TopDocs search = indexSearcher.search(ee, 10);
//            ScoreDoc[] hits = search.scoreDocs;
//            for (ScoreDoc hit: hits){
//                System.out.println(hit);
//            }
//            //terms.add(term);
//            Query query = new TermQuery(term);
//            booleanQuery.add(query, BooleanClause.Occur.SHOULD);
//            //TopDocs search = indexSearcher.search(query, 2);
//            //ScoreDoc[] hits = search.scoreDocs;
//            //for (ScoreDoc hit : hits) {
//            //    System.out.println();
//            //}
//        }
//
//        System.out.println(booleanQuery);
//        TopDocs search = indexSearcher.search(booleanQuery, 2);
//        ScoreDoc[] hits = search.scoreDocs;
//        for (ScoreDoc hit: hits){
//            System.out.println(hit);
//        }
        Gson gson = new Gson();
        HashMap<Integer, Histogram> histograms = new HashMap<>();
        HashMap<Integer, Float> evalHistograms = new HashMap<>();
        FileReader fileReader = new FileReader(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("high_level_elements.json")).getPath()));
        HashMap<String, HashSet<String>> high_level_elements = gson.fromJson(fileReader,
                new TypeToken<HashMap<String, HashSet<String>>>() {
                }.getType());
        Evaluator evaluator = new Evaluator(OperationBase.operations(), high_level_elements);
        for (int i = 0; i < indexReader.numDocs(); i++) {

            Document d = indexReader.document(i);
            Histogram histogram = new Histogram();
            List<IndexableField> fields = d.getFields();
            for (IndexableField field : fields) {
                Vector<String> el = new Vector<>();
                el.add(field.name());
                HElement hElement = new HElement(el, Float.parseFloat(field.stringValue()));
                histogram.add(hElement);
            }
            histograms.put(i, histogram);
            Vector<String> vector = new Vector<>(expression);
            HElementSet HE_result = evaluator.eval(vector, histogram);
            evalHistograms.put(i, HE_result.sum());
        }
        List<Float> listEvalHistograms = new ArrayList<>(evalHistograms.values());
        listEvalHistograms.sort(Collections.reverseOrder());

        List<Map.Entry<Integer, Float>> result = evalHistograms.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
                .collect(Collectors.toList());

        System.out.println("Результаты поиска:");
        int j = 1;
        for (int i = result.size() - 1; i >= result.size() - 5; i--) {
            System.out.printf("%d) %.2f, ID: %d, Гистограмма: ", j, result.get(i).getValue(), result.get(i).getKey());
            j++;
            System.out.println(histograms.get(result.get(i).getKey()).toMap());
        }
    }
}
