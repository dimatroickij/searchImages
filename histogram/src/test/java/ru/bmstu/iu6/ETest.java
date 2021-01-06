package ru.bmstu.iu6;

import org.junit.Assert;
import org.junit.Test;
import ru.bmstu.iu6.element.HElement;
import ru.bmstu.iu6.element.HElementSet;
import ru.bmstu.iu6.histogram.Histogram;

import java.util.HashSet;
import java.util.Vector;

public class ETest {

    Vector<String> e1 = new Vector<>();
    Vector<String> e2 = new Vector<>();
    Vector<String> e3 = new Vector<>();
    Vector<String> e4 = new Vector<>();
    Vector<String> e5 = new Vector<>();
    Vector<String> e6 = new Vector<>();
    Vector<String> e7 = new Vector<>();
    Vector<String> e8 = new Vector<>();
    Vector<String> e9 = new Vector<>();
    Vector<String> e10 = new Vector<>();
    Vector<String> e11 = new Vector<>();
    Vector<String> e12 = new Vector<>();
    Vector<String> e13 = new Vector<>();
    Vector<String> e14 = new Vector<>();
    Vector<String> e15 = new Vector<>();
    Vector<String> e16 = new Vector<>();
    Vector<String> e17 = new Vector<>();
    Vector<String> e18 = new Vector<>();
    Vector<String> e19 = new Vector<>();
    Vector<String> e20 = new Vector<>();
    Vector<String> e21 = new Vector<>();
    Vector<String> e22 = new Vector<>();
    Vector<String> e23 = new Vector<>();
    Vector<String> e24 = new Vector<>();
    Vector<String> e25 = new Vector<>();
    Vector<String> e26 = new Vector<>();
    Vector<String> e27 = new Vector<>();
    Vector<String> e28 = new Vector<>();
    Vector<String> e29 = new Vector<>();
    Vector<String> e30 = new Vector<>();
    Vector<String> e31 = new Vector<>();
    Vector<String> e32 = new Vector<>();
    Vector<String> e33 = new Vector<>();
    Vector<String> e34 = new Vector<>();
    Vector<String> e35 = new Vector<>();
    Vector<String> e36 = new Vector<>();
    Vector<String> e37 = new Vector<>();
    Vector<String> e38 = new Vector<>();
    Vector<String> e39 = new Vector<>();
    Vector<String> e40 = new Vector<>();

    Vector<Vector<String>> U = new Vector<>();

    public ETest(){

        e1.add("e1");
        e2.add("e2");
        e3.add("e3");
        e4.add("e4");
        e5.add("e5");
        e6.add("e6");
        e7.add("e7");
        e8.add("e8");
        e9.add("e9");
        e10.add("e10");
        e11.add("e11");
        e12.add("e12");
        e13.add("e13");
        e14.add("e14");
        e15.add("e15");
        e16.add("e16");
        e17.add("e17");
        e18.add("e18");
        e19.add("e19");
        e20.add("e20");
        e21.add("e21");
        e22.add("e22");
        e23.add("e23");
        e24.add("e24");
        e25.add("e25");
        e26.add("e26");
        e27.add("e27");
        e28.add("e28");
        e29.add("e29");
        e30.add("e30");
        e31.add("e31");
        e32.add("e32");
        e33.add("e33");
        e34.add("e34");
        e35.add("e35");
        e36.add("e36");
        e37.add("e37");
        e38.add("e38");
        e39.add("e39");
        e40.add("e40");

        U.add(e1);
        U.add(e2);
        U.add(e3);
        U.add(e4);
        U.add(e5);
        U.add(e6);
        U.add(e7);
        U.add(e8);
        U.add(e9);
        U.add(e10);
        U.add(e11);
        U.add(e12);
        U.add(e13);
        U.add(e14);
        U.add(e15);
        U.add(e16);
        U.add(e17);
        U.add(e18);
        U.add(e19);
        U.add(e20);
        U.add(e21);
        U.add(e22);
        U.add(e23);
        U.add(e24);
        U.add(e25);
        U.add(e26);
        U.add(e27);
        U.add(e28);
        U.add(e29);
        U.add(e30);
        U.add(e31);
        U.add(e32);
        U.add(e33);
        U.add(e34);
        U.add(e35);
        U.add(e36);
        U.add(e37);
        U.add(e38);
        U.add(e39);
        U.add(e40);
    }

    @Test
    public void E_Histogram() {


        Vector<Vector<String>> data = new Vector<>();
        data.add(e28);
        data.add(e28);
        data.add(e35);
        data.add(e38);
        data.add(e31);
        data.add(e26);
        data.add(e26);
        data.add(e10);
        data.add(e23);
        data.add(e23);
        data.add(e14);
        data.add(e14);
        data.add(e39);
        data.add(e39);
        data.add(e7);
        data.add(e27);
        data.add(e17);
        data.add(e29);
        data.add(e30);
        data.add(e24);
        data.add(e3);
        data.add(e22);
        data.add(e15);
        data.add(e2);
        data.add(e2);

        Vector<Float> correct = new Vector<>();
        for (int i = 0; i < 40; i++) {
            correct.add((float) 0);
        }

        correct.set(1, (float) 0.08);
        correct.set(2, (float) 0.04);
        correct.set(6, (float) 0.04);
        correct.set(9, (float) 0.04);
        correct.set(13, (float) 0.08);
        correct.set(14, (float) 0.04);
        correct.set(16, (float) 0.04);
        correct.set(21, (float) 0.04);
        correct.set(22, (float) 0.08);
        correct.set(23, (float) 0.04);
        correct.set(25, (float) 0.08);
        correct.set(26, (float) 0.04);
        correct.set(27, (float) 0.08);
        correct.set(28, (float) 0.04);
        correct.set(29, (float) 0.04);
        correct.set(30, (float) 0.04);
        correct.set(34, (float) 0.04);
        correct.set(37, (float) 0.04);
        correct.set(38, (float) 0.08);

        Histogram hist = new Histogram(data);
        Vector<Float> elements_all = E.convertHistToAllValues(U, hist);
        Assert.assertEquals(correct, elements_all);
    }

    @Test
    public void E_HElementSet() {
        HashSet<HElement> HE = new HashSet<>();

        HE.add(new HElement(e3, (float) 0.04));
        HE.add(new HElement(e10, (float) 0.04));
        HE.add(new HElement(e17, (float) 0.04));
        HE.add(new HElement(e15, (float) 0.04));
        HE.add(new HElement(e7, (float) 0.04));
        HE.add(new HElement(e2, (float) 0.08));
        HE.add(new HElement(e14, (float) 0.08));

        HElementSet HE1 = new HElementSet(HE);

        Vector<Float> HE1_all = E.convertHistToAllValues(U, HE1);

        Vector<Float> correct = new Vector<>();
        for (int i = 0; i < 40; i++) {
            correct.add((float) 0);
        }

        correct.set(1, (float) 0.08);
        correct.set(2, (float) 0.04);
        correct.set(6, (float) 0.04);
        correct.set(9, (float) 0.04);
        correct.set(13, (float) 0.08);
        correct.set(14, (float) 0.04);
        correct.set(16, (float) 0.04);

        Assert.assertEquals(correct, HE1_all);
    }
}
