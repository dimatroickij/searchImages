package ru.bmstu.iu6.histogram;

import ru.bmstu.iu6.Evaluator;
import ru.bmstu.iu6.Parsing;
import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.operation.OperationBase;

import java.util.HashMap;
import java.util.Vector;

public class HistogramModel {

    private Vector<Vector<String>> U;
    private String positioning;
    private String U_positions;
    private Evaluator evaluator;
    private Parsing parser;
    private HashMap<String, OperationBase> operations;

    public HistogramModel() {
        constructor(null, null, null);
    }

    public HistogramModel(String positioning) {
        constructor(null, positioning, null);
    }

    public void constructor(Vector<Vector<String>> U, String positioning, String U_positions) {
        this.U = U;
        this.positioning = positioning;
        this.U_positions = U_positions;
        evaluator = null;
        parser = null;
        operations = OperationBase.operations();
    }


    public HElementSet execute(String query, Object histogram) {
        if (parser == null)
            parser = new Parsing();

        if (evaluator == null)
            evaluator = new Evaluator(operations);

        Vector<String> query_array = parser.parseString(query);
        return evaluator.eval(query_array, (Histogram) histogram);
    }

    public Object transform(Vector<Vector<String>> data) throws Exception {
        if (data.size() == 0) {
            throw new Exception("Нет данных");
        }
        if (positioning == null)
            return transform2histogram(data);
        else if (positioning.equals("1d")) {
            return transform2histogram1D(data);
        } else if (positioning.equals("2d")) {
            return transform2histogram2D(data);
        } else
            throw new Exception("Неправильный формат входных данных");
    }

    private Histogram transform2histogram(Vector<Vector<String>> data) {
        return new Histogram(data);
    }

    private Histogram1D transform2histogram1D(Vector<Vector<String>> data) throws Exception {
        for (Vector<String> el : data) {
            if (el.size() <= 1)
                throw new Exception("Неправильный формат даты");
        }
        return new Histogram1D(data);
    }

    private Histogram2D transform2histogram2D(Vector<Vector<String>> data) throws Exception {
        for (Vector<String> el : data) {
            if (el.size() <= 1)
                throw new Exception("Неправильный формат даты");
        }
        return new Histogram2D(data);
    }
}
