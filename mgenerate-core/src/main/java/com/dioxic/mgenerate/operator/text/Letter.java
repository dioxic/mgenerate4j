package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Initializable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Letter implements Resolvable<java.lang.Character>, Initializable {

    private static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String pool;

    @OperatorProperty
    String casing;

    @Override
    public java.lang.Character resolve() {
        return pool.charAt(FakerUtil.numberBetween(0, pool.length()));
    }

    @Override
    public void initialize() {
        StringBuilder sb = new StringBuilder();
        if ("lower".equals(casing)) {
            sb.append(ALPHA_LOWER);
        }
        else if ("upper".equals(casing)) {
            sb.append(ALPHA_UPPER);
        }
        else {
            sb.append(ALPHA_UPPER);
            sb.append(ALPHA_LOWER);
        }

        pool = sb.toString();
    }
}
