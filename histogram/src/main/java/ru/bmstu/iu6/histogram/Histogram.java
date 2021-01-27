package ru.bmstu.iu6.histogram;

import ru.bmstu.iu6.element.HElement;
import ru.bmstu.iu6.element.HElementSet;

import java.util.*;

public class Histogram {

    private HashMap<Vector<String>, HElement> HE;
    private Float size;

    public Histogram() {
        this.HE = new HashMap<>();
        size = Float.parseFloat("0");
    }

    public Histogram(Vector data) {
        constructor(data, null, true);
    }

    public Histogram(Vector data, Float size) {
        constructor(data, size, true);
    }

    public Histogram(Vector data, boolean normalized) {
        constructor(data, null, normalized);
    }

    public Histogram(Vector data, Float size, boolean normalized) {
        constructor(data, size, normalized);
    }

    private void constructor(Vector data, Float size, boolean normalized) {
        this.HE = transform(data);
        if (size == null) {
            Float summ = (float) 0;
            for (HElement el : this.HE.values()) {
                summ += el.getValue();
            }
            this.size = summ;
        } else {
            this.size = size;
        }

        if (normalized)
            this.normalize();
    }

    public Float sum() {
        Float summ = (float) 0;

        for (HashMap.Entry<Vector<String>, HElement> el : this.HE.entrySet()) {
            summ += el.getValue().getValue();
        }
        return summ;
    }

    public Set<Vector<String>> elements() {
        return this.HE.keySet();
    }

    public HashMap<Vector<String>, HElement> histElements() {
        return this.HE;
    }

    public void add(HElement element) {
        if (!this.HE.containsKey(element.getKey()))
            this.HE.put(element.getKey(), new HElement(element.getKey(), (float) 0));
        HElement hElement = this.HE.get(element.getKey());
        hElement.setValue(hElement.getValue() + element.getValue());
        this.HE.replace(element.getKey(), hElement);
        size += element.getValue();
    }

    public HashMap toMap() {
        HashMap hashMap = new HashMap<>();
        for (HashMap.Entry<Vector<String>, HElement> el : this.HE.entrySet()) {
            if (el.getKey().size() == 1)
                hashMap.put(el.getKey().get(0), el.getValue().getValue());
            else
                hashMap.put(el.getKey(), el.getValue().getValue());
        }
        return hashMap;
    }

    public void normalize(Float size) {
        if (size != null)
            this.size = size;
        this.normalize();
    }

    public void normalize() {
        for (HashMap.Entry<Vector<String>, HElement> el : this.HE.entrySet()) {
            HElement hElement = el.getValue();
            hElement.setValue(hElement.getValue() / this.size);
            el.setValue(hElement);
        }
    }

    public HElementSet call(String element, HashMap<String, HashSet<String>> composition) {
        Vector<String> elv = new Vector<>();
        elv.add(element);
        if (this.contains(elv)) {
            HashSet<HElement> he = new HashSet<>();
            he.add(this.getItem(elv));
            return new HElementSet(he);
        } else if (element != null && composition != null && composition.containsKey(element) && composition.get(element) != null) {
            HashSet<HElement> he = new HashSet<>();

            for (String el : composition.get(element)) {
                Vector<String> el2 = new Vector<>();
                el2.add(el);
                if (this.contains(el2)) {
                    he.add(this.getItem(el2));
                }
            }

            return new HElementSet(he);
        }
        return new HElementSet();
    }

    public HElementSet call(String element) {
        return this.call(element, null);
    }

    public HElementSet call(Vector<String> element) {
        return call(element, null);
    }

    public HElementSet call(Vector<String> element, HashMap<String, HashMap<String, HashSet<String>>> composition) {
        int element_dim = element.size();
        HashMap<Integer, HashSet<String>> Es = new HashMap<>();
        boolean has_compound = false;

        for (int i = 0; i < element_dim; i++) {
            HashSet<String> el = new HashSet<>();
            el.add(element.get(i));
            Es.put(i, el);
            if (composition != null && composition.containsKey(String.valueOf(i)) && composition.get(String.valueOf(i)).containsKey(element.get(i))) {
                HashSet<String> el1 = composition.get(String.valueOf(i)).get(element.get(i));
                Es.put(i, el1);
                has_compound = true;
            }
        }
        if (!has_compound && this.contains(element)) {
            HashSet<HElement> he = new HashSet<>();
            he.add(this.getItem(element));
            return new HElementSet(he);
        } else {
            HashSet<HElement> he = new HashSet<>();
            Collection<HElement> collection = this.histElements().values();
            for (HElement el : collection) {
                if (condition(el.getKey(), element_dim, Es))
                    he.add(el);
            }
            return new HElementSet(he);
        }
    }

    private boolean condition(Vector<String> key, int size, HashMap<Integer, HashSet<String>> Es) {
        HashSet<HElement> he = new HashSet<>();
        boolean response = true;
        for (int i = 0; i < size; i++) {
            if (!(Es.get(i).contains(key.get(i)) || Es.get(i).contains("all"))) {
                response = false;
            }
        }
        return response;
    }

    private void setItem(Vector<String> key, Float value) {
        HElement hElement = new HElement(key, value);
        this.HE.put(key, hElement);
    }

    public HElement getItem(Vector<String> item) {
        return this.HE.get(item);
    }

    public boolean contains(Vector<String> item) {
        return this.HE.containsKey(item);
    }

    private Integer len() {
        return this.HE.size();
    }

    public Histogram HistUnion(Histogram other) {
        HashMap<Vector<String>, HElement> opn1, opn2;
        Histogram hist = new Histogram();
        if (this.HE.size() > other.histElements().size()) {
            opn1 = other.histElements();
            opn2 = this.HE;
        } else {
            opn1 = this.HE;
            opn2 = other.histElements();
        }

        for (HashMap.Entry<Vector<String>, HElement> el : opn2.entrySet()) {
            hist.setItem(el.getKey(), el.getValue().getValue());
        }

        for (HashMap.Entry<Vector<String>, HElement> el : opn1.entrySet()) {
            if (!hist.contains(el.getKey())) {
                hist.setItem(el.getKey(), (float) 0);
            }
            Float last_value = hist.getItem(el.getKey()).getValue();
            hist.setItem(el.getKey(), el.getValue().getValue() + last_value);
        }

        return hist;
    }

    public Histogram HistIntersection(Histogram other) {
        HashMap<Vector<String>, HElement> opn1, opn2;
        Histogram hist = new Histogram();
        if (this.HE.size() > other.histElements().size()) {
            opn1 = other.histElements();
            opn2 = this.HE;
        } else {
            opn1 = this.HE;
            opn2 = other.histElements();
        }

        for (HashMap.Entry<Vector<String>, HElement> el : opn1.entrySet()) {
            if (opn2.containsKey(el.getKey())) {
                hist.setItem(el.getKey(), Math.min(el.getValue().getValue(), opn2.get(el.getKey()).getValue()));
            }
        }

        return hist;
    }

    public static HashMap<Vector<String>, HElement> transform(Vector data) {

        //Convert data to histogram
        //
        //        Parameters
        //        ----------
        //        data            a data composed from elements of the universal set
        //
        //        Returns
        //        -------
        //        dictionary      {element id : value}

        HashMap<Vector<String>, HElement> histogram = new HashMap<>();

        if (data.get(0) instanceof Vector) {
            for (Vector<String> el : (Vector<Vector<String>>) data) {
                if (!histogram.containsKey(el))
                    histogram.put(el, new HElement(el, (float) 0));
                Float last_value = histogram.get(el).getValue();
                HElement hElement = histogram.get(el);
                hElement.setValue(last_value + 1);
                histogram.replace(el, hElement);
            }
        }
        else {
            for (String el : (Vector<String>) data) {
                Vector<String> elv = new Vector<>();
                elv.add(el);
                if (!histogram.containsKey(elv))
                    histogram.put(elv, new HElement(elv, (float) 0));
                Float last_value = histogram.get(elv).getValue();
                HElement hElement = histogram.get(elv);
                hElement.setValue(last_value + 1);
                histogram.replace(elv, hElement);
            }
        }
        return histogram;
    }

}
