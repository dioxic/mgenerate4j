package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Letter implements Resolvable<java.lang.Character>, Initializable {

    private static final java.lang.String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final java.lang.String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @OperatorProperty
    java.lang.String casing;

    private java.lang.String pool;

    @Override
    public java.lang.Character resolve() {
        return pool.charAt(FakerUtil.numberBetween(0, pool.length()));
    }

    @Override
    public void initialize() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
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
