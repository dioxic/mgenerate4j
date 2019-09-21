package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MaxTest {

    @Test
    @SuppressWarnings("unchecked")
    void resolve_Double() {
        Max max = new MaxBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1d, 2d, 3d))
                .build();

        assertThat(max.resolve()).isEqualTo(3d);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_Integer() {
        Max max = new MaxBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1, 2, 3))
                .build();

        assertThat(max.resolve()).isEqualTo(3);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_Long() {
        Max max = new MaxBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1L, 2L, 3L))
                .build();

        assertThat(max.resolve()).isEqualTo(3L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_String() {
        Max max = new MaxBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList("alice", "bob", "badger"))
                .build();

        assertThat(max.resolve()).isEqualTo("bob");
    }
}
