package uk.dioxic.mgenerate.core.operator.chrono;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

class DateTest {

    @Test
    void resolve() {

        Date date = new DateBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        Assertions.assertThat(date.resolve()).isBetween(Date.DEFAULT_MIN, Date.DEFAULT_MAX);
    }

    @Test
    void resolve_WithBounds() {
        LocalDateTime min = LocalDateTime.parse("1950-01-01T00:00:00");
        LocalDateTime max = LocalDateTime.parse("1990-01-01T00:00:00");

        Date date = new DateBuilder(ReflectiveTransformerRegistry.getInstance())
                .min(min)
                .max(max)
                .build();

        Assertions.assertThat(date.resolve()).isBetween(min, max);
    }
}
