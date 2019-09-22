package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DayOfMonthTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        DayOfMonth dom = new DayOfMonthBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(dom.resolveInternal()).isBetween(1,32);
    }

    @Test
    void resolve_FromDate() {
        DayOfMonth dow = new DayOfMonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(dow.resolveInternal()).isEqualTo(ldt.getDayOfMonth());
    }

}
