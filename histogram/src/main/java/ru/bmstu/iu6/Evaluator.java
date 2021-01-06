package ru.bmstu.iu6;

import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.histogram.Histogram;
import ru.bmstu.iu6.histogram.Histogram1D;
import ru.bmstu.iu6.operation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Vector;

public class Evaluator {

    private final Object H;
    private final HashMap<String, OperationBase> O;
    private final HashMap<String, HashSet<String>> extendedE;
    private final HashMap<Integer, HashMap<String, HashSet<String>>> extendedE1D;

    public Evaluator(HashMap<String, OperationBase> o) {
        O = o;
        H = null;
        extendedE = new HashMap<>();
        extendedE1D = new HashMap<>();
    }

    public Evaluator(HashMap<String, OperationBase> o, Histogram h) {
        O = o;
        H = h;
        extendedE = new HashMap<>();
        extendedE1D = new HashMap<>();
    }

    public Evaluator(HashMap<String, OperationBase> o, Histogram1D h) {
        O = o;
        H = h;
        extendedE = new HashMap<>();
        extendedE1D = new HashMap<>();
    }

    public Evaluator(HashMap<String, OperationBase> o, HashMap<String, HashSet<String>> high_level_elements) {
        O = o;
        H = null;
        extendedE = high_level_elements;
        extendedE1D = new HashMap<>();
    }

    public Evaluator(HashMap<String, OperationBase> o, Histogram h, HashMap<String, HashSet<String>> high_level_elements) {
        O = o;
        H = h;
        extendedE = high_level_elements;
        extendedE1D = new HashMap<>();
    }

    public Evaluator(HashMap<String, OperationBase> o, Histogram1D h,
                     HashMap<Integer, HashMap<String, HashSet<String>>> high_level_elements) {
        O = o;
        H = h;
        extendedE = new HashMap<>();
        extendedE1D = high_level_elements;
    }

    public HElementSet eval(Vector<String> expression) {
        return eval(expression, null, true);
    }

    public HElementSet eval1D(Vector<Vector<String>> expression) {
        return eval1D(expression, null, true);
    }

    public HElementSet eval(Vector<String> expression, Histogram data_histogram) {
        return eval(expression, data_histogram, true);
    }

    public HElementSet eval1D(Vector<Vector<String>> expression, Histogram1D data_histogram) {
        return eval1D(expression, data_histogram, true);
    }

    public HElementSet eval(Vector<String> expression, boolean copy_expression) {
        return eval(expression, null, copy_expression);
    }

    public HElementSet eval1D(Vector<Vector<String>> expression, boolean copy_expression) {
        return eval1D(expression, null, copy_expression);
    }

    public HElementSet eval(Vector<String> expression, Histogram data_histogram, boolean copy_expression) {
        Vector<String> expr;

        if (copy_expression)
            //должна быть копия expression
            expr = expression;
        else
            expr = expression;

        Histogram hist;
        if (data_histogram != null)
            hist = data_histogram;
        else
            hist = (Histogram) H;

        return postfixEvaluate(expr, hist);
    }

    public HElementSet eval1D(Vector<Vector<String>> expression, Histogram1D data_histogram, boolean copy_expression){
        Vector<Vector<String>> expr;

        if (copy_expression)
            expr = new Vector<>(expression);
        else
            expr = expression;

        Histogram1D hist;
        if (data_histogram != null)
            hist = data_histogram;
        else
            hist = (Histogram1D) H;

        return postfixEvaluate(expr, hist);
    }

    private HElementSet postfixEvaluate(Vector<String> expression, Histogram histogram) {
        String op = expression.remove(expression.size() - 1);
        int num_args = 0;

        //ДОДЕЛАТЬ
        if (Objects.equals(op, "unary -"))
            return postfixEvaluate(expression, histogram);

        if (O.containsKey(op)) {
            HElementSet op2 = postfixEvaluate(expression, histogram);
            HElementSet op1 = postfixEvaluate(expression, histogram);

            switch (op) {
                case ("+"):
                    return new SetUnion().compute(op1, op2);
                case ("*"):
                    return new SetIntersection().compute(op1, op2);
                case ("&"):
                    return new SetAnd().compute(op1, op2);
                case ("#|"):
                    return new SetOr().compute(op1, op2);
                case ("|"):
                    return new SetAndOr().compute(op1, op2);
                case ("/"):
                    return new SetSubtraction().compute(op1, op2);
                case ("#/"):
                    return new SetXSubtraction().compute(op1, op2);
                default:
                    return new HElementSet();
            }

        } else {
            Vector<String> opp = new Vector<>();
            opp.add(op);
            HashMap<Vector<String>, HashSet<Vector<String>>> extendedETransform = new HashMap<>();
            for (HashMap.Entry<String, HashSet<String>> el: extendedE.entrySet()){
                Vector<String> key = new Vector<>();
                HashSet<Vector<String>> value = new HashSet<>();
                key.add(el.getKey());
                for (String val: el.getValue()){
                    Vector<String> val2 = new Vector<>();
                    val2.add(val);
                    value.add(val2);
                }
                extendedETransform.put(key, value);
            }
            return histogram.call(op, extendedE);
        }
    }

    private HElementSet postfixEvaluate(Vector<Vector<String>> expression, Histogram1D histogram) {
        Vector<String> op = expression.remove(expression.size() - 1);
        int num_args = 0;

        //ДОДЕЛАТЬ
        if (Objects.equals(op.get(0), "unary -"))
            return postfixEvaluate(expression, histogram);

        if (O.containsKey(op.get(0))) {
            HElementSet op2 = postfixEvaluate(expression, histogram);
            HElementSet op1 = postfixEvaluate(expression, histogram);

            switch (op.get(0)) {
                case ("+"):
                    return new SetUnion().compute(op1, op2);
                case ("*"):
                    return new SetIntersection().compute(op1, op2);
                case ("&"):
                    return new SetAnd().compute(op1, op2);
                case ("#|"):
                    return new SetOr().compute(op1, op2);
                case ("|"):
                    return new SetAndOr().compute(op1, op2);
                case ("/"):
                    return new SetSubtraction().compute(op1, op2);
                case ("#/"):
                    return new SetXSubtraction().compute(op1, op2);
                default:
                    return new HElementSet();
            }

        } else {
            // Добавить проверку на классы Historgam и HistogramModel
            return histogram.call(op, extendedE1D);
        }
    }
}
