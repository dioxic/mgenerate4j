package uk.dioxic.mgenerate.core.operator.sequence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class DateSequenceTest {

    @Test
    void resolve() {
        LocalDateTime start = LocalDateTime.parse("1900-01-01T00:00:00");
        long step = 5;
        ChronoUnit chrono = ChronoUnit.MINUTES;

        DateSequence inc = new DateSequenceBuilder(ReflectiveTransformerRegistry.getInstance())
                .start(start)
                .chronoUnit(chrono)
                .step(step)
                .build();

        Assertions.assertThat(inc.resolveInternal()).as("check starting value").isEqualTo(start);
        Assertions.assertThat(inc.resolveInternal()).as("check next value").isEqualTo(start.plus(step, chrono));
        Assertions.assertThat(inc.resolveInternal()).as("check next value").isEqualTo(start.plus(step, chrono).plus(step, chrono));
    }
}
