package ru.bmstu.iu6.operation;

import ru.bmstu.iu6.element.HElementSet;

public class DictUnion extends OperationBase {
    public DictUnion() {
        super("", "");
    }

    @Override
    public HElementSet compute(HElementSet arg1, HElementSet arg2) {
        HElementSet opn1;
        HElementSet opn2;
        if (arg1.len() > arg2.len()) {
            opn1 = arg2;
            opn2 = arg1;
        } else {
            opn1 = arg1;
            opn2 = arg2;
        }


        return opn2;
    }

    //    def compute(self, arg1, arg2):
    //        opn1, opn2 = (arg2, arg1) if len(arg1) > len(arg2) else (arg1, arg2)
    //        for k, v in opn1:
    //            opn2.add(v)
    //        return opn2
}
