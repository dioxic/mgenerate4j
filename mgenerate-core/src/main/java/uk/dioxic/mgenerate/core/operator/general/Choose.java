package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.core.util.ResolverUtil;

import java.util.List;

@Operator
public class Choose extends AbstractOperator<Object> implements Initializable {

    @OperatorProperty(required = true)
    List<?> from;

    @OperatorProperty
    List<Integer> weights;

    @Override
    public Object resolveInternal() {
        return from.get(FakerUtil.random().nextInt(from.size()));
    }

    @Override
    public void initialize() {
        from = ResolverUtil.getWeightedArray(from, weights);
    }
}
