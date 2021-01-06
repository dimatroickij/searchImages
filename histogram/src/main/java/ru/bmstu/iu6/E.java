package ru.bmstu.iu6;

import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.histogram.Histogram;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class E {

    private final String value;

    public String getValue() {
        return value;
    }

    public E(String expression) {
        this.value = "(" + expression + ")";
    }

    public E Union(E other) {
        return this.compose(other.getValue(), "+");
    }

    public E Intersection(E other) {
        return this.compose(other.getValue(), "*");
    }

    public E Sub(E other) {
        return this.compose(other.getValue(), "/");
    }

    public E Or(E other) {
        return this.compose(other.getValue(), "|");
    }

    public E And(E other) {
        return this.compose(other.getValue(), "&");
    }

    public E Xor(E other) {
        return this.compose(other.getValue(), "#|");
    }

    public E Xsub(E other) {
        return this.compose(other.getValue(), "#/");
    }

    public E compose(String other, String op) {
        return new E(this.value + op + other);
    }

    public E compose(E other, String op) {
        return new E(this.value + op + other.getValue());
    }

    public E add(E other) {
        return Union(other);
    }

    public E mul(E other) {
        return Intersection(other);
    }

    public E sub(E other) {
        return Sub(other);
    }

    public E and(E other) {
        return And(other);
    }

    public E or(E other) {
        return Or(other);
    }

    public E xor(E other) {
        return Xor(other);
    }

    public static Vector<Float> convertHistToAllValues(Vector<Vector<String>> U, Histogram H, boolean to_sort) {

        Vector<Float> hist_val_full = new Vector<>();

        for (Vector<String> el : U) {
            hist_val_full.add((float) 0);
        }

        for (int i = 0; i < U.size(); i++) {
            if (H.contains(U.get(i)))
                hist_val_full.set(i, H.getItem(U.get(i)).getValue());
        }

        if (to_sort) {
            Collections.sort(hist_val_full);
        }

        return hist_val_full;
    }

    public static Vector<Float> convertHistToAllValues(Vector<Vector<String>> U, Histogram H) {
        return convertHistToAllValues(U, H, false);
    }

    public static Vector<Float> convertHistToAllValues(Vector<Vector<String>> U, HElementSet H, boolean to_sort) {
        Vector<Float> hist_val_full = new Vector<>();

        for (Vector<String> el : U) {
            hist_val_full.add((float) 0);
        }


       //HashMap<Vector<String>, Float> elements = H.to_dict();
        HashMap elements = H.toMap();
        Vector z = new Vector(elements.keySet());
        if (z.get(0) instanceof String){
            for (int i = 0; i < U.size(); i++) {
                if (elements.keySet().contains(U.get(i).get(0)))
                    hist_val_full.set(i, (Float) elements.get(U.get(i).get(0)));
            }
        }
        else {
            for (int i = 0; i < U.size(); i++) {
                if (elements.containsKey(U.get(i)))
                    hist_val_full.set(i, (Float) elements.get(U.get(i)));
            }
        }



        if (to_sort) {
            Collections.sort(hist_val_full);
        }

        return hist_val_full;
    }

    public static Vector<Float> convertHistToAllValues(Vector<Vector<String>> U, HElementSet H) {
        return convertHistToAllValues(U, H, false);
    }
}
