package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class SetUnion extends OperationBase {

    public SetUnion() {
        super("+", "");
    }

    @Override
    public HElementSet compute(HElementSet arg1, HElementSet arg2) {
        return new HElementSet(arg1.union(arg2));
    }
}
