package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

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
