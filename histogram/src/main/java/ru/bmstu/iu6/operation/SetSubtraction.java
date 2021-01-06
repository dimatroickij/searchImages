package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class SetSubtraction extends OperationBase {
    public SetSubtraction() {
        super("/", "");
    }

    @Override
    public HElementSet compute(HElementSet arg1, HElementSet arg2) {
        return new HElementSet(arg1.difference(arg2));
    }
}
