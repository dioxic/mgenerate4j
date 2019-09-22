package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class YearTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        Year year = new YearBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(year.resolveInternal()).isPositive();
    }

    @Test
    void resolve_FromDate() {
        Year year = new YearBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(year.resolveInternal()).isEqualTo(ldt.getYear());
    }
}
