package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class SetXSubtraction extends OperationBase {

    public SetXSubtraction() {
        super("#/", "");
    }

    @Override
    public HElementSet compute(HElementSet arg1, HElementSet arg2) {
        if (arg2.sum() > 0)
            return new HElementSet();
        else
            return arg1;
    }
}
