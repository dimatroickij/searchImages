package ru.bmstu.iu6.integrate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Indexer {

    private final IndexWriter indexWriter;

    public Indexer(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(indexDirectory, conf);
    }

    public void close() throws IOException {
        indexWriter.close();
    }

    private void indexFile(File file) throws IOException {
        Document document = getDocument(file);
        indexWriter.addDocument(document);
        System.out.println("Indexing " + file.getCanonicalPath());
    }

    private Document getDocumentJson(String id, String histogram) throws IOException {
        Document document = new Document();
        Gson gson = new Gson();
        List<HashMap<String, Float>> map = gson.fromJson(histogram, new TypeToken<List<HashMap<String, Float>>>() {
        }.getType());

        document.add(new StringField("ID", id, Field.Store.YES));
        for (HashMap<String, Float> el : map) {
            for (HashMap.Entry<String, Float> el2 : el.entrySet()) {
                List<String> key = Arrays.asList(el2.getKey().replace("(", "").replace(")", "").split(","));
                document.add(new StringField(key.toString(), el2.getValue().toString(), Field.Store.YES));
            }
        }

        return document;
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        Gson gson = new Gson();
        FileReader fileReader = new FileReader(file);

        HashMap<String, Float> map = gson.fromJson(fileReader, new TypeToken<HashMap<String, Float>>() {
        }.getType());

        for (HashMap.Entry<String, Float> el : map.entrySet()) {
            document.add(new StringField(el.getKey(), el.getValue().toString(), Field.Store.YES));
        }
        return document;
    }

    public int createIndex(String dataDirPath)
            throws IOException {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();

        assert files != null;
        for (File file : files) {
            if (!file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
            ) {
                indexFile(file);
            }
        }
        return indexWriter.numRamDocs();
    }

    public void addHistogram(String id, String histogram) throws IOException {
        Document document = getDocumentJson(id, histogram);
        indexWriter.addDocument(document);
        System.out.println("Indexing " + id);
    }

    public void rescanHistogram(String id, String histogram) throws IOException {
        Term term = new Term("ID", id.toString());
        Document document = getDocumentJson(id, histogram);
        indexWriter.updateDocument(term, document);
        System.out.println("Rescan " + id);
    }

    public void deleteHistogram(String id) throws IOException {
        Term term = new Term("ID", id);
        indexWriter.deleteDocuments(term);
        System.out.println("Delete " + id);
    }

}
