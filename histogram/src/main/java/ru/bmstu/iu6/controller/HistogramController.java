package ru.bmstu.iu6.controller;

import org.apache.lucene.index.IndexNotFoundException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.iu6.integrate.Indexer;
import ru.bmstu.iu6.integrate.Searcher;

import java.io.IOException;
import java.util.UUID;

@RestController
public class HistogramController {

    String indexDirectoryPath = "C:\\indexing";

    @RequestMapping(value = "/addHistogram/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> indexingHistogram(@PathVariable("id") UUID id, @RequestBody String payload) throws IOException {
        Indexer indexer = new Indexer(indexDirectoryPath);
        try {
            indexer.rescanHistogram(id.toString(), payload);
            indexer.close();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.toString());
            indexer.close();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/rescanHistogram/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> rescanHistogram(@PathVariable("id") UUID id, @RequestBody String payload) throws IOException {
        Indexer indexer = new Indexer(indexDirectoryPath);
        try {
            indexer.rescanHistogram(id.toString(), payload);
            indexer.close();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            indexer.close();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/deleteHistogram/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> deleteHistogram(@PathVariable("id") UUID id) throws IOException {
        Indexer indexer = new Indexer(indexDirectoryPath);
        try {
            indexer.deleteHistogram(id.toString());
            indexer.close();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.toString());
            indexer.close();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/search")
    public ResponseEntity<String> search(@RequestParam String request) throws IOException {
        try {
            Searcher searcher = new Searcher(indexDirectoryPath);
            System.out.println(request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IndexNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
