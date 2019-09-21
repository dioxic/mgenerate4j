package uk.dioxic.mgenerate.core.operator.numeric;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.math.BigDecimal;

@Operator({"mgNumberDecimal", "decimal", "decimal128"})
public class NumberDecimal implements Resolvable<BigDecimal> {

    private static final Long DEFAULT_MIN = 0L;
    private static final Long DEFAULT_MAX = 1000L;
    private static final Resolvable<Integer> DEFAULT_FIXED = Wrapper.wrap(2);

    @OperatorProperty
    Long min = DEFAULT_MIN;

    @OperatorProperty
    Long max = DEFAULT_MAX;

    @OperatorProperty
    Resolvable<Integer> fixed = DEFAULT_FIXED;

    @Override
    public BigDecimal resolve() {
        return FakerUtil.randomDecimal(min, max, fixed.resolve());
    }
}
