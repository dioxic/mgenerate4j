package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class TimestampTest {

    @Test
    void resolve() {
        int i = 333;
        Timestamp timestamp = new TimestampBuilder(ReflectiveTransformerRegistry.getInstance()).i(i).build();

        assertThat(timestamp.resolveInternal().getInc()).as("check increment value").isEqualTo(i);
    }
}
