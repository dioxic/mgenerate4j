package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HourTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        Hour hour = new HourBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(hour.resolve()).isBetween(0, 24);
    }

    @Test
    void resolve_FromDate() {
        Hour hour = new HourBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(hour.resolve()).isEqualTo(ldt.getHour());
    }
}
