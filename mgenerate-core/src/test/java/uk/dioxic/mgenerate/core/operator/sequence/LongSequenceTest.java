package uk.dioxic.mgenerate.core.operator.sequence;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class LongSequenceTest {

    @Test
    void resolve() {
        long start = 3;
        long step = 2;

        LongSequence seq = new LongSequenceBuilder(ReflectiveTransformerRegistry.getInstance())
                .start(start)
                .step(step)
                .build();

        assertThat(seq.resolveInternal()).as("check starting value").isEqualTo(start);
        assertThat(seq.resolveInternal()).as("check next value").isEqualTo(start + step);
        assertThat(seq.resolveInternal()).as("check next value").isEqualTo(start + step + step);
    }
}
