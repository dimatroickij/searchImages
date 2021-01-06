package ru.bmstu.iu6.histogram;

import java.util.Vector;

public class Histogram1D extends Histogram {
//        Create a histogram of elements
//
//        Parameters
//        ----------
//        element         a low- or high-level element
//        composition     used for a high-level element to define a set of low-level elements
//
//        Returns
//        -------
//        histogram of elements (HE) -> HElementSet

//        element

    public Histogram1D() {
        super();
    }

    public Histogram1D(Vector<Vector<String>> data) {
        super(data);
    }
}
