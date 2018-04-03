package uk.dioxic.mgenerate.operator.text;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator("string")
public class StringOp implements Resolvable<String> {

    @OperatorProperty
    java.lang.String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()[]";

    @OperatorProperty
    Resolvable<Integer> length = OperatorFactory.wrap(5);

    @Override
    public String resolve() {
        Integer length = this.length.resolve();

        StringBuilder sb = new StringBuilder(length);

        FakerUtil.random().ints(length, 0, pool.length())
                .forEach(c -> sb.append(pool.charAt(c)));

        return sb.toString();
    }

}
