package uk.dioxic.mgenerate.core.operator.core;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.List;

@Operator
public class Pick implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<List> array;

    @OperatorProperty
    Resolvable<Integer> element = Wrapper.wrap(Integer.valueOf(0));

    @Override
    public Object resolve() {
        return array.resolve().get(element.resolve());
    }



}
