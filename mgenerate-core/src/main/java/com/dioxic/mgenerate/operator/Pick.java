package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.util.List;

@OperatorClass
public class Pick implements Operator<Object> {

    @OperatorProperty(required = true)
    Operator<List> array;

    @OperatorProperty
    Operator<Integer> element = OperatorFactory.wrap(Integer.valueOf(0));

    @Override
    public Object resolve() {
        return array.resolve().get(element.resolve());
    }

}
