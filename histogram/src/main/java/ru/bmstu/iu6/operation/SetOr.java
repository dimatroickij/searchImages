package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class SetOr extends OperationBase {
    public SetOr() {
        super("#|", "");
    }

    @Override
    public HElementSet compute(HElementSet arg1, HElementSet arg2) {
        if (arg1.sum() > arg2.sum())
            return arg1;
        else
            return arg2;
    }
}
