package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class IncTest {

    @Test
    void resolve_WithStep() {
        Inc inc = new IncBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(500)
                .build();

        assertThat(inc.resolveInternal()).isEqualTo(501);
    }

    @Test
    void resolve() {
        Inc inc = new IncBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(500)
                .step(10)
                .build();

        assertThat(inc.resolveInternal()).isEqualTo(510);
    }

}
