package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MinTest {

    @Test
    @SuppressWarnings("unchecked")
    void resolve_Double() {
        Min min = new MinBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1d, 2d, 3d))
                .build();

        assertThat(min.resolveInternal()).isEqualTo(1d);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_Integer() {
        Min min = new MinBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1, 2, 3))
                .build();

        assertThat(min.resolveInternal()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_Long() {
        Min min = new MinBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1L, 2L, 3L))
                .build();

        assertThat(min.resolveInternal()).isEqualTo(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_String() {
        Min min = new MinBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList("alice", "bob", "badger"))
                .build();

        assertThat(min.resolveInternal()).isEqualTo("alice");
    }
}
