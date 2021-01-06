package ru.bmstu.iu6;

import org.junit.Assert;
import org.junit.Test;
import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.histogram.Histogram1D;
import ru.bmstu.iu6.histogram.HistogramModel;
import ru.bmstu.iu6.operation.OperationBase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class Evaluator1DTest {
    Histogram1D hist;
    Parsing parser;
    Evaluator evaluator;

    public Evaluator1DTest() throws Exception {
        Vector<Vector<String>> data = new Vector<>();

        Vector<String> d1 = new Vector<>();
        d1.add("ep1");
        d1.add("e1");

        Vector<String> d2 = new Vector<>();
        d2.add("ep1");
        d2.add("e2");

        Vector<String> d3 = new Vector<>();
        d3.add("ep2");
        d3.add("e2");

        Vector<String> d4 = new Vector<>();
        d4.add("ep2");
        d4.add("e3");

        Vector<String> d5 = new Vector<>();
        d5.add("ep3");
        d5.add("e3");

        Vector<String> d6 = new Vector<>();
        d6.add("ep3");
        d6.add("e4");

        data.add(d1);
        data.add(d1);
        data.add(d2);
        data.add(d3);
        data.add(d3);
        data.add(d4);
        data.add(d4);
        data.add(d5);
        data.add(d5);
        data.add(d6);

        HistogramModel hist_model = new HistogramModel("1d");
        hist = (Histogram1D) hist_model.transform(data);

        parser = new Parsing();

        HashMap<Integer, HashMap<String, HashSet<String>>> high_level_elements = new HashMap<>();
        HashMap<String, HashSet<String>> hl0 = new HashMap<>();
        HashMap<String, HashSet<String>> hl1 = new HashMap<>();

        HashSet<String> v1 = new HashSet<>();
        v1.add("ep1");
        v1.add("ep2");

        HashSet<String> v2 = new HashSet<>();
        v2.add("ep2");
        v2.add("ep3");

        hl0.put("Ep1", v1);
        hl0.put("Ep2", v2);

        HashSet<String> v3 = new HashSet<>();
        v3.add("e1");
        v3.add("e2");

        hl1.put("E1", v3);

        high_level_elements.put(0, hl0);
        high_level_elements.put(1, hl1);

        evaluator = new Evaluator(OperationBase.operations(), hist, high_level_elements);
    }

    @Test
    public void test_HE_from_H__all_position_element() {
        Vector<String> d = new Vector<>();
        d.add("all");
        d.add("e3");
        HElementSet HE = hist.call(d);

        HashMap<Vector<String>, Float> comp = new HashMap<>();
        Vector<String> el1 = new Vector<>();
        el1.add("ep2");
        el1.add("e3");

        Vector<String> el2 = new Vector<>();
        el2.add("ep3");
        el2.add("e3");

        comp.put(el1, (float) 0.2);
        comp.put(el2, (float) 0.2);
        Assert.assertEquals(comp, HE.toMap());
        Assert.assertEquals((float) 0.4, HE.sum(), 0.000001);
    }

    @Test
    public void test_evaluate__all_position_element() {
        E E1 = new E("all, e3");

        Vector<Vector<String>> expression = parser.parseString1D(E1.getValue());
        HElementSet HE_result = evaluator.eval1D(expression);

        HashMap<Vector<String>, Float> comp = new HashMap<>();
        Vector<String> el1 = new Vector<>();
        el1.add("ep2");
        el1.add("e3");

        Vector<String> el2 = new Vector<>();
        el2.add("ep3");
        el2.add("e3");

        comp.put(el1, (float) 0.2);
        comp.put(el2, (float) 0.2);

        Assert.assertEquals(comp, HE_result.toMap());
        Assert.assertEquals((float) 0.4, HE_result.sum(), 0.000001);
    }

    @Test
    public void test_evaluate__high_level_elements() {
        E E1 = new E("Ep1, E1");

        Vector<Vector<String>> expression = parser.parseString1D(E1.getValue());
        HElementSet HE_result = evaluator.eval1D(expression);

        HashMap<Vector<String>, Float> comp = new HashMap<>();
        Vector<String> el1 = new Vector<>();
        el1.add("ep1");
        el1.add("e1");

        Vector<String> el2 = new Vector<>();
        el2.add("ep1");
        el2.add("e2");

        Vector<String> el3 = new Vector<>();
        el3.add("ep2");
        el3.add("e2");

        comp.put(el1, (float) 0.2);
        comp.put(el2, (float) 0.1);
        comp.put(el3, (float) 0.2);

        Assert.assertEquals(comp, HE_result.toMap());
        Assert.assertEquals((float) 0.5, HE_result.sum(), 0.000001);
    }

    @Test
    public void test_evaluate_AndOr__low_level_elements() {
        E E1 = new E("ep1, e1");
        E E2 = new E("ep2, e3");
        E E3 = new E("ep2, e3");

        E query = E1.add(E2).add(E3);

        Vector<Vector<String>> expression = parser.parseString1D(query.getValue());
        HElementSet HE_result = evaluator.eval1D(expression);

        HashMap<Vector<String>, Float> comp = new HashMap<>();
        Vector<String> el1 = new Vector<>();
        el1.add("ep1");
        el1.add("e1");

        Vector<String> el2 = new Vector<>();
        el2.add("ep2");
        el2.add("e3");

        comp.put(el1, (float) 0.2);
        comp.put(el2, (float) 0.2);
        Assert.assertEquals(comp, HE_result.toMap());
        Assert.assertEquals((float) 0.4, HE_result.sum(), 0.000001);

    }

    @Test
    public void test_evaluate_AndOr__high_level_elements() {
        E E1 = new E("Ep1, E1");
        E E2 = new E("Ep2, e4");

        E query = E1.add(E2);

        Vector<Vector<String>> expression = parser.parseString1D(query.getValue());
        HElementSet HE_result = evaluator.eval1D(expression);

        HashMap<Vector<String>, Float> comp = new HashMap<>();
        Vector<String> el1 = new Vector<>();
        el1.add("ep1");
        el1.add("e1");

        Vector<String> el2 = new Vector<>();
        el2.add("ep1");
        el2.add("e2");

        Vector<String> el3 = new Vector<>();
        el3.add("ep2");
        el3.add("e2");

        Vector<String> el4 = new Vector<>();
        el4.add("ep3");
        el4.add("e4");

        comp.put(el1, (float) 0.2);
        comp.put(el2, (float) 0.1);
        comp.put(el3, (float) 0.2);
        comp.put(el4, (float) 0.1);

        Assert.assertEquals(comp, HE_result.toMap());
        Assert.assertEquals((float) 0.6, HE_result.sum(), 0.000001);
    }
}
