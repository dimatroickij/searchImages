package ru.bmstu.iu6.integrate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class Indexer {

    private IndexWriter indexWriter;

    public Indexer(String indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));

        //create the indexer
        IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(indexDirectory, conf);
    }

    public void close() throws CorruptIndexException, IOException {
        indexWriter.close();
    }

    private void indexFile(File file) throws IOException {
        System.out.println("Indexing "+ file.getCanonicalPath());
        Document document = getDocument(file);
        indexWriter.addDocument(document);
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        Gson gson = new Gson();
        FileReader fileReader = new FileReader(file);

        HashMap<String, Float> map = gson.fromJson(fileReader, new TypeToken<HashMap<String, Float>>(){}.getType());

        for (HashMap.Entry<String, Float> el : map.entrySet()){
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
            if(!file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
            ){
                indexFile(file);
            }
        }
        return indexWriter.numRamDocs();
    }

}
