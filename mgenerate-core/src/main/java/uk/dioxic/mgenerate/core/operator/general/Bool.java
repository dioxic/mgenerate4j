package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"bool", "boolean"})
public class Bool extends AbstractOperator<Boolean> {

    @Override
    public Boolean resolveInternal() {
        return FakerUtil.randomBoolean();
    }

}
