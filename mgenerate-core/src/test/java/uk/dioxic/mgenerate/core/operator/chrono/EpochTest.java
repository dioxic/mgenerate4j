package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class EpochTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_Random() {
        Epoch epoch = new EpochBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(epoch.resolve().longValue()).isPositive();
    }

    @Test
    void resolve_FromDate() {
        Epoch epoch = new EpochBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(ldt)
                .build();

        assertThat(epoch.resolve()).isEqualTo(ldt.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Test
    void resolve_FromDateWithChronoUnit() {
        Epoch epoch = new EpochBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(ldt)
                .unit(ChronoUnit.DAYS)
                .build();

        assertThat(epoch.resolve()).isEqualTo(ChronoUnit.DAYS.between(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC), ldt));
    }
}
