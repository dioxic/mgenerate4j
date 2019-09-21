package uk.dioxic.mgenerate.core.operator.numeric;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class NumberDecimalTest {

    @Test
    void resolve() {
        long min = -20L;
        long max = 25L;
        int fixed = 2;

        NumberDecimal decimal = new NumberDecimalBuilder(ReflectiveTransformerRegistry.getInstance()).fixed(fixed).min(min).max(max).build();

        assertThat(decimal.resolve()).as("check value").isBetween(BigDecimal.valueOf(min), BigDecimal.valueOf(max));
        assertThat(decimal.resolve().scale()).as("check scale").isEqualTo(fixed);
    }
}
