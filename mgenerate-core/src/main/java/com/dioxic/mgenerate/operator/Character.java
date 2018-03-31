package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Initializable;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

@Operator
public class Character implements Resolvable<java.lang.Character>, Initializable {

    private static final String SYMBOLS = "!@#$%^&*()";
    private static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC = "0123456789";

    @OperatorProperty
    String pool;

    @OperatorProperty
    Boolean alpha = Boolean.FALSE;

    @OperatorProperty
    Boolean symbols = Boolean.FALSE;

    @OperatorProperty
    Boolean numeric = Boolean.FALSE;

    @OperatorProperty
    String casing;

    @Override
    public java.lang.Character resolve() {
        return pool.charAt(FakerUtil.numberBetween(0, pool.length()));
    }

    @Override
    public void initialize() {
        if (pool == null) {
            StringBuilder sb = new StringBuilder();
            if (alpha || symbols || numeric || casing != null) {
                if ("lower".equals(casing)) {
                    sb.append(ALPHA_LOWER);
                }
                else if ("upper".equals(casing)) {
                    sb.append(ALPHA_UPPER);
                }
                else if (alpha) {
                    sb.append(ALPHA_UPPER);
                    sb.append(ALPHA_LOWER);
                }

                if (symbols) {
                    sb.append(SYMBOLS);
                }

                if (numeric) {
                    sb.append(NUMERIC);
                }
            }
            else {
                sb.append(ALPHA_UPPER);
                sb.append(ALPHA_LOWER);
                sb.append(SYMBOLS);
                sb.append(NUMERIC);
            }
            pool = sb.toString();
        }
    }
}
