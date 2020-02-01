package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.operator.type.HashAlgorithm;

@Operator
public class Hash extends AbstractOperator<Object> {

    @OperatorProperty(required = true)
    Resolvable<Object> input;

    @OperatorProperty
    HashAlgorithm algorithm = HashAlgorithm.MD5;

    @Override
    public Integer resolveInternal() {
        return algorithm.hash(input.resolve());
    }

}
