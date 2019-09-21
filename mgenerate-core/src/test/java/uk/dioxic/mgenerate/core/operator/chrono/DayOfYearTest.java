package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DayOfYearTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        DayOfYear doy = new DayOfYearBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(doy.resolve()).isBetween(1, 365);
    }

    @Test
    void resolve_FromDate() {
        DayOfYear doy = new DayOfYearBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(doy.resolve()).isEqualTo(ldt.getDayOfYear());
    }

}
