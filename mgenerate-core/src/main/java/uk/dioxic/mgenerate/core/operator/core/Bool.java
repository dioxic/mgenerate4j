package uk.dioxic.mgenerate.core.operator.core;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Bool implements Resolvable<Boolean> {

    @Override
    public Boolean resolve() {
        return FakerUtil.randomBoolean();
    }

}
