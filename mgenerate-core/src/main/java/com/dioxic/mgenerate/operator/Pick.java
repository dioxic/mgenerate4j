package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.util.List;

@Operator
public class Pick implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<List> array;

    @OperatorProperty
    Resolvable<Integer> element = OperatorFactory.wrap(Integer.valueOf(0));

    @Override
    public Object resolve() {
        return array.resolve().get(element.resolve());
    }

}
