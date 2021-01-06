package ru.bmstu.iu6.element;

import java.util.Vector;

public class HElement {

//    Low Level Histogram Element
//
//    This element must corresponds to one of elements from the universal set.
//
//    Note: The universal set is one from which data is made up. Think of it as
//    a dictionary of terms.
//
//    Parameters
//    ----------
//    key         an element id
//    value       a value of the element
//    properties  additional parameters that can be used in evaluation phase,
//    e.g. mean var

    private final Vector<String> key;
    private Float value;
    private Vector<String> properties;

    public HElement(Vector<String> key, Float value){
        this.key = key;
        this.value = value;
    }

    public HElement(Vector<String> key, Float value, Vector<String> properties) {
        this.key = key;
        this.value = value;
        this.properties = properties;
    }

    public Vector<String> getKey() {
        return key;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer hash() {
        return this.key.hashCode();
    }

    public Vector<String> getProperties() {
        return properties;
    }
}