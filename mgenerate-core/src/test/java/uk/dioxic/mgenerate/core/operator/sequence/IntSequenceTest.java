package uk.dioxic.mgenerate.core.operator.sequence;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class IntSequenceTest {

    @Test
    void resolve() {
        int start = 3;
        int step = 2;

        IntSequence seq = new IntSequenceBuilder(ReflectiveTransformerRegistry.getInstance())
                .start(start)
                .step(step)
                .build();

        assertThat(seq.resolveInternal()).as("check starting value").isEqualTo(start);
        assertThat(seq.resolveInternal()).as("check next value").isEqualTo(start + step);
        assertThat(seq.resolveInternal()).as("check next value").isEqualTo(start + step + step);
    }
}
