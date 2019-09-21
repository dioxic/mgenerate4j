package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.type.Casing;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Character implements Resolvable<java.lang.Character>, Initializable {

    private static final java.lang.String SYMBOLS = "!@#$%^&*()";
    private static final java.lang.String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final java.lang.String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final java.lang.String NUMERIC = "0123456789";

    @OperatorProperty(primary = true)
    java.lang.String pool;

    @OperatorProperty
    Boolean alpha = Boolean.FALSE;

    @OperatorProperty
    Boolean symbols = Boolean.FALSE;

    @OperatorProperty
    Boolean numeric = Boolean.FALSE;

    @OperatorProperty
    Casing casing;

    @Override
    public java.lang.Character resolve() {
        return pool.charAt(FakerUtil.numberBetween(0, pool.length()));
    }

    @Override
    public void initialize() {
        if (pool == null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            if (alpha || symbols || numeric || casing != null) {
                switch (casing) {
                    case UPPER:
                        sb.append(ALPHA_UPPER);
                        break;
                    case LOWER:
                        sb.append(ALPHA_LOWER);
                        break;
                    default:
                        if (alpha) {
                            sb.append(ALPHA_UPPER);
                            sb.append(ALPHA_LOWER);
                        }
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
