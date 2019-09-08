package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

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
