package ru.bmstu.iu6.controller;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class HistogramController {

    @RequestMapping(value = "/addHistogram", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getJson(){
        return JSONObject.quote("Hello World");
    }
}
