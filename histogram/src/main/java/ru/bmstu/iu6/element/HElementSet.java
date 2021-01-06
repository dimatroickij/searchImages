package ru.bmstu.iu6.element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class HElementSet {
    private final HashSet<HElement> HE;

    public HElementSet(HashSet<HElement> HE) {
        this.HE = HE;
    }

    public HElementSet() {
        this.HE = new HashSet<>();
    }

    public HashSet<HElement> getHE() {
        return HE;
    }

    public void add(HElement he) {
        this.HE.add(he);
    }

    public void discard(HElement he) {
        this.HE.remove(he);
    }

    public HashSet<HElement> union(HElementSet other) {
        HashSet<HElement> union = new HashSet<>();
        union.addAll(this.HE);
        union.addAll(other.getHE());
        return union;
    }

    public HashSet<HElement> intersection(HElementSet other) {
        HashSet<HElement> intersection = new HashSet<>();
        intersection.addAll(this.HE);
        intersection.retainAll(other.getHE());
        return intersection;
    }

    public HashSet<HElement> difference(HElementSet other) {
        HashSet<HElement> difference = new HashSet<>();
        difference.addAll(this.HE);
        difference.removeAll(other.getHE());
        return difference;
    }

    public Float sum() {
        Float summa = (float) 0;
        for (HElement he : this.HE) {
            summa += he.getValue();
        }
        return summa;
    }

    public Float prod() {
        Float result = (float) 1;

        for (HElement he : this.HE) {
            result *= he.getValue();
        }

        return result;
    }

    public Vector<Vector<String>> elements() {
        Vector<Vector<String>> el = new Vector<>();
        for (HElement he : this.HE) {
            el.add(he.getKey());
        }
        return el;
    }

    public Vector<Float> values() {
        Vector<Float> val = new Vector<>();
        for (HElement he : this.HE) {
            val.add(he.getValue());
        }
        return val;
    }

    public HashMap toMap() {
        HashMap hashMap = new HashMap<>();
        for (HElement he : this.HE) {
            if (he.getKey().size() == 1)
                hashMap.put(he.getKey().get(0), he.getValue());
            else
                hashMap.put(he.getKey(), he.getValue());
        }
        return hashMap;
    }

    public int len() {
        return this.HE.size();
    }
}
