package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class SetAnd extends OperationBase {
    public SetAnd() {
        super("&", "");
    }

    @Override
    public HElementSet compute(HElementSet arg1, HElementSet arg2) {
        if (arg1.sum() > arg2.sum())
            return arg2;
        else
            return arg1;
    }
}
