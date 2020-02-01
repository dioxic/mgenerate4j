package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.ByteUtil;

@Operator({"bitShift", "bitShiftLeft"})
public class BitShiftLeft extends AbstractOperator<Number> {

    @OperatorProperty(required = true)
    Resolvable<Number> input;

    @OperatorProperty(required = true)
    Resolvable<Integer> bits;

    @Override
    public Number resolveInternal() {
        Number num = input.resolve();
        if (num instanceof Integer) {
            return ByteUtil.bitShiftLeft((Integer) num, bits.resolve());
        } else if (num instanceof Long) {
            return ByteUtil.bitShiftLeft((Long) num, bits.resolve());
        }

        throw new IllegalArgumentException("bitshift operator only supports int or long types");
    }

}
