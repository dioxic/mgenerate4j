package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Word extends AbstractOperator<java.lang.String> {

    @Override
    public java.lang.String resolveInternal() {
        return FakerUtil.getValue("lorem.words");
    }

}
