package ru.bmstu.iu6;

import org.junit.Assert;
import org.junit.Test;
import ru.bmstu.iu6.histogram.Histogram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class HistogramTest {

    Vector<String> e1 = new Vector<>();
    Vector<String> e2 = new Vector<>();
    Vector<String> e3 = new Vector<>();
    Vector<String> e4 = new Vector<>();
    Vector<String> e5 = new Vector<>();
    Vector<String> e6 = new Vector<>();

    public HistogramTest(){
        e1.add("e1");
        e2.add("e2");
        e3.add("e3");
        e4.add("e4");
        e5.add("e5");
        e6.add("e6");
    }

    @Test
    public void call() {
        Vector<Vector<String>> data = new Vector<>();

        data.add(e1);
        data.add(e1);
        data.add(e1);
        data.add(e1);
        data.add(e2);
        data.add(e3);
        data.add(e3);
        data.add(e3);
        data.add(e4);
        data.add(e5);

        Histogram he = new Histogram(data);

        HashMap<String, Float> call = new HashMap<>();
        call.put("e2", (float) 0.1);

        HashMap<String, Float> callAndCompostition = new HashMap<>();
        callAndCompostition.put("e2", (float) 0.1);
        callAndCompostition.put("e1", (float) 0.4);

        HashMap<String, HashSet<String>> composition = new HashMap<>();
        HashSet<String> zz = new HashSet<>();

        zz.add("e1");
        zz.add("e2");

        composition.put("xyz", zz);

        Assert.assertEquals(call, he.call("e2").toMap());

        Assert.assertEquals(callAndCompostition, he.call("xyz", composition).toMap());
    }

    @Test
    public void testUnion() {
        Vector<Vector<String>> data1 = new Vector<>();

        data1.add(e1);
        data1.add(e1);
        data1.add(e1);
        data1.add(e1);
        data1.add(e2);
        data1.add(e3);
        data1.add(e3);
        data1.add(e3);
        data1.add(e4);
        data1.add(e5);

        Vector<Vector<String>> data2 = new Vector<>();
        data2.add(e1);
        data2.add(e2);
        data2.add(e3);
        data2.add(e4);
        data2.add(e6);

        Histogram hist1 = new Histogram(data1, false);
        Histogram hist2 = new Histogram(data2, false);

        Histogram histUnion = hist1.HistUnion(hist2);

        HashMap<String, Float> dict = new HashMap<>();
        dict.put("e1", (float) 5);
        dict.put("e2", (float) 2);
        dict.put("e3", (float) 4);
        dict.put("e4", (float) 2);
        dict.put("e5", (float) 1);
        dict.put("e6", (float) 1);


        Float sum = (float) 15.0;

        Assert.assertEquals(sum, histUnion.sum());
        Assert.assertEquals(dict, histUnion.toMap());
    }

    @Test
    public void testIntersection() {
        Vector<Vector<String>> data1 = new Vector<>();
        data1.add(e1);
        data1.add(e1);
        data1.add(e2);
        data1.add(e3);
        data1.add(e5);
        data1.add(e3);
        data1.add(e1);
        data1.add(e3);
        data1.add(e1);
        data1.add(e4);

        Vector<Vector<String>> data2 = new Vector<>();
        data2.add(e1);
        data2.add(e2);
        data2.add(e3);
        data2.add(e4);
        data2.add(e6);

        Histogram hist1 = new Histogram(data1, false);
        Histogram hist2 = new Histogram(data2, false);

        Histogram histIntersection;
        histIntersection = hist1.HistIntersection(hist2);

        HashMap<String, Float> dict = new HashMap<>();
        dict.put("e1", (float) 1);
        dict.put("e2", (float) 1);
        dict.put("e3", (float) 1);
        dict.put("e4", (float) 1);

        Float sum = (float) 4.0;

        Assert.assertEquals(sum, histIntersection.sum());
        Assert.assertEquals(dict, histIntersection.toMap());

    }
}
