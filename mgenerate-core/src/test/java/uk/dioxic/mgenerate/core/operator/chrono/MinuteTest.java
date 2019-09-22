package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MinuteTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        Minute minute = new MinuteBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(minute.resolveInternal()).isBetween(0, 59);
    }

    @Test
    void resolve_FromDate() {
        Minute minute = new MinuteBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(minute.resolveInternal()).isEqualTo(ldt.getMinute());
    }
}
