package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SecondTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        Second second = new SecondBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(second.resolveInternal()).isBetween(0, 59);
    }

    @Test
    void resolve_FromDate() {
        Second second = new SecondBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(second.resolveInternal()).isEqualTo(ldt.getSecond());
    }
}
