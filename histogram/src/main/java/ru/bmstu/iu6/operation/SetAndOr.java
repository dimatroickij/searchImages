package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class SetAndOr extends OperationBase {

    public SetAndOr() {
        super("|", "");
    }

    @Override
    public HElementSet compute(HElementSet op1, HElementSet op2) {
        return new HElementSet(op1.union(op2));
    }
}
