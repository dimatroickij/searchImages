package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

import java.util.HashMap;

public abstract class OperationBase {

    private static String sign;
    private static String description;

    public OperationBase(String sign, String description) {
        OperationBase.sign = sign;
        OperationBase.description = description;
    }

    public static HashMap<String, OperationBase> operations() {
        HashMap<String, OperationBase> operations = new HashMap<>();

        operations.put(getSign(), new SetUnion());
        operations.put(getSign(), new SetIntersection());
        operations.put(SetAnd.getSign(), new SetAnd());
        operations.put(SetOr.getSign(), new SetOr());
        operations.put(SetAndOr.getSign(), new SetAndOr());
        operations.put(SetSubtraction.getSign(), new SetSubtraction());
        operations.put(getSign(), new SetXSubtraction());

        return operations;
    }

    public static String getSign() {
        return sign;
    }

    public abstract HElementSet compute(HElementSet arg1, HElementSet arg2);
}
