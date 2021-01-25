package ru.bmstu.iu6.controller;

import org.json.JSONObject;
import org.omg.CORBA.MARSHAL;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.iu6.E;
import ru.bmstu.iu6.integrate.Indexer;
import ru.bmstu.iu6.integrate.Searcher;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
public class HistogramController {

    public static void indexing() throws IOException {
        Indexer indexer = new Indexer("/indexing");
        int i = indexer.createIndex("/dataHistogram");
        indexer.close();
    }

    @RequestMapping(value = "/addHistogram/{id}", method = RequestMethod.POST)
    public String getJson(@PathVariable("id") UUID id, @RequestBody String payload) throws IOException {
        System.out.println(payload);

//        indexing();
//        String expr = "(e1+e2+e3)|(e2+e3+e4)";
//        E E1 = new E(expr);
//        Searcher searcher = new Searcher("C:\\indexing");
//        searcher.search(E1.getValue());
        return JSONObject.quote("Hello World");
    }
}
