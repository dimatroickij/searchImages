package ru.bmstu.iu6;

import org.junit.Assert;
import org.junit.Test;
import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.histogram.Histogram;
import ru.bmstu.iu6.operation.OperationBase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class EvaluatorTest {

    Histogram hist;
    Parsing parsing;
    Evaluator evaluator;

    Vector<String> data = new Vector<>();

    public EvaluatorTest() {
        data.add("e1");
        data.add("e1");
        data.add("e1");
        data.add("e1");
        data.add("e2");
        data.add("e3");
        data.add("e3");
        data.add("e3");
        data.add("e4");
        data.add("e5");
        // Преобразование входных данных в гистограмму
        hist = new Histogram(data);

        // Инициализация парсера
        parsing = new Parsing();

        // Определение элементов высокого уровня
        HashMap<String, HashSet<String>> high_level_elements = new HashMap<>();
        HashSet<String> E1 = new HashSet<>();
        HashSet<String> E2 = new HashSet<>();

        E1.add("e1");
        E1.add("e2");
        E1.add("e3");

        E2.add("e2");
        E2.add("e3");
        E2.add("e4");

        high_level_elements.put("E1", E1);
        high_level_elements.put("E2", E2);

        // Инициализация Evaluator
        evaluator = new Evaluator(OperationBase.operations(), hist, high_level_elements);
    }

    @Test
    public void test_H_normalized() {
        // Преобразование входных данных в нормализовнную гистограмму
        Assert.assertEquals(1, Math.round(hist.sum()), 0.000001);

        HashMap<String, Float> comp = new HashMap<>();
        comp.put("e2", (float) 0.1);
        comp.put("e1", (float) 0.4);
        comp.put("e3", (float) 0.3);
        comp.put("e5", (float) 0.1);
        comp.put("e4", (float) 0.1);

        Assert.assertEquals(comp, hist.toMap());
    }

    @Test
    public void test_HE_from_H__low_level_element() {
        //Извлечение низкоуровневого элемента из гистограммы
        HElementSet HE = hist.call("e1");

        HashMap<String, Float> hashMap = new HashMap<>();
        hashMap.put("e1", (float) 0.4);

        Assert.assertEquals(hashMap, HE.toMap());
        Float sum = (float) 0.4;
        Assert.assertEquals(sum, HE.sum());
    }

    @Test
    public void test_high_level_element() {
        //Извлечение высокоуровневого элемента из гистограммы
        HashMap<String, HashSet<String>> hashMap = new HashMap<>();
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("e1");
        hashSet.add("e2");
        hashMap.put("E1", hashSet);
        HElementSet HE = hist.call("E1", hashMap);

        HashMap<String, Float> comp = new HashMap<>();
        comp.put("e1", (float) 0.4);
        comp.put("e2", (float) 0.1);

        Assert.assertEquals(comp, HE.toMap());

        Float sum = (float) 0.5;
        Assert.assertEquals(sum, HE.sum());
    }

    @Test
    public void test_evaluate_Or__low_level_elements() {
        // Оценка выражения с низкоуровневыми элементами и операцией ИЛИ
        E E1 = new E("e1+e2+e3");
        E E2 = new E("e2+e3+e4");

        Vector<String> expression = parsing.parseString(E1.Or(E2).getValue());

        HElementSet HE_result = evaluator.eval(expression);

        HashMap<String, Float> comp = new HashMap<>();
        comp.put("e1", (float) 0.4);
        comp.put("e2", (float) 0.1);
        comp.put("e3", (float) 0.3);
        comp.put("e4", (float) 0.1);

        Assert.assertEquals(comp, HE_result.toMap());

        float sum = (float) 0.9;
        Assert.assertEquals(sum, HE_result.sum(), 0.000001);
    }

    @Test
    public void test_evaluate_Or__high_level_elements() {
        // Оценка выражения с высокоуровневым элементом и операцией ИЛИ
        E E1 = new E("E1");
        E E2 = new E("e3");

        Vector<String> expression = parsing.parseString(E1.Or(E2).getValue());
        HElementSet HE_result = evaluator.eval(expression);

        HashMap<String, Float> comp = new HashMap<>();
        comp.put("e1", (float) 0.4);
        comp.put("e2", (float) 0.1);
        comp.put("e3", (float) 0.3);

        Assert.assertEquals(comp, HE_result.toMap());
        Assert.assertEquals(0.8, HE_result.sum(), 0.000001);
    }

    @Test
    public void test_evaluate_AndOr__low_level_elements() {
        // Оценка выражения с низкоуровневым элементом и операцией И
        E E1 = new E("e1+e2+e3");
        E E2 = new E("e2+e3+e4");

        Vector<String> expression = parsing.parseString((E1.add(E2)).getValue());
        HElementSet HE_result = evaluator.eval(expression);

        HashMap<String, Float> comp = new HashMap<>();

        comp.put("e1", (float) 0.4);
        comp.put("e2", (float) 0.1);
        comp.put("e3", (float) 0.3);
        comp.put("e4", (float) 0.1);

        Assert.assertEquals(comp, HE_result.toMap());
        Assert.assertEquals(0.9, HE_result.sum(), 0.000001);
    }

    @Test
    public void test_evaluate_AndOr__high_level_elements() {
        // Оценка выражения с высокоуровневым элементом и операцией И
        E E1 = new E("E1");
        E E2 = new E("E2");

        Vector<String> expression = parsing.parseString((E1.add(E2)).getValue());

        HElementSet HE_result = evaluator.eval(expression);

        HashMap<String, Float> comp = new HashMap<>();
        comp.put("e1", (float) 0.4);
        comp.put("e2", (float) 0.1);
        comp.put("e3", (float) 0.3);
        comp.put("e4", (float) 0.1);

        Assert.assertEquals(comp, HE_result.toMap());

        float sum = (float) 0.9;
        Assert.assertEquals(sum, HE_result.sum(), 0.000001);
    }

    @Test
    public void pr(){
        E E1 = new E("E1");
        E E2 = new E("E2");

        Vector<String> expression = parsing.parseString((E1.add(E2)).getValue());

        Histogram he = new Histogram(data);
        HElementSet h = evaluator.eval(expression, he);
    }
}
