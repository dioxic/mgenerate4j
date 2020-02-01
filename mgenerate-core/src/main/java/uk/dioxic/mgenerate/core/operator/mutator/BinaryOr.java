package uk.dioxic.mgenerate.core.operator.mutator;

import org.bson.types.ObjectId;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.operator.type.OutputType;
import uk.dioxic.mgenerate.core.util.ByteUtil;

import java.util.List;

@Operator
public class BinaryOr extends AbstractOperator<Number> {

    @OperatorProperty(required = true)
    Resolvable<List<Number>> input;

    @OperatorProperty
    OutputType outputType = OutputType.INT64;

    @Override
    public Number resolveInternal() {

        switch (outputType) {
            case INT32:
                return input.resolve().stream()
                        .map(Number::intValue)
                        .reduce(0, (a, b) -> a | b);
            case INT64:
                return input.resolve().stream()
                        .map(Number::longValue)
                        .reduce(0L, (a, b) -> a | b);
        }

        throw new IllegalArgumentException("binaryOr operator only supports int or long types");
    }

}
