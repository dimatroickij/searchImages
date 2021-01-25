package ru.bmstu.iu6.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.iu6.integrate.Indexer;

import java.io.IOException;
import java.util.UUID;

@RestController
public class HistogramController {

    @RequestMapping(value = "/addHistogram/{id}", method = RequestMethod.POST)
    public HttpStatus indexingHistogram(@PathVariable("id") UUID id, @RequestBody String payload) throws IOException {
        Indexer indexer = new Indexer("/indexing");
        indexer.createIndexJson(id, payload);
        indexer.close();
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/rescanHistogram/{id}", method = RequestMethod.POST)
    public HttpStatus rescanHistogram(@PathVariable("id") UUID id, @RequestBody String payload) throws IOException {
        Indexer indexer = new Indexer("/indexing");
        indexer.createIndexJson(id, payload);
        indexer.close();
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam String request){
        System.out.println(request);
        return JSONObject.quote(request);
    }
}
