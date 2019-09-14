package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator("string")
public class StringOp implements Resolvable<String> {

    @OperatorProperty
    java.lang.String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()[]";

    @OperatorProperty(primary = true)
    Resolvable<Integer> length = Wrapper.wrap(5);

    @Override
    public String resolve() {
        Integer length = this.length.resolve();

        StringBuilder sb = new StringBuilder(length);

        FakerUtil.random().ints(length, 0, pool.length())
                .forEach(c -> sb.append(pool.charAt(c)));

        return sb.toString();
    }

}
