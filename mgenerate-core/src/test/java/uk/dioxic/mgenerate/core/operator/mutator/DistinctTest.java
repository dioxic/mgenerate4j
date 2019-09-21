package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class DistinctTest {

    @Test
    void resolve() {
        Distinct distinct = new DistinctBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList("badger", "badger", "badger"))
                .build();

        assertThat(distinct.resolve()).hasSize(1);
    }

}
