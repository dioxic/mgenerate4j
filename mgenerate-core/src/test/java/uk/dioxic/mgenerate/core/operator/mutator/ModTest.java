package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class ModTest {

    @Test
    void resolve_Double() {
        Mod mod = new ModBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(100d)
                .mod(11)
                .build();

        assertThat(mod.resolve()).isEqualTo(1);
    }

    @Test
    void resolve_Integer() {
        Mod mod = new ModBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(100)
                .mod(11)
                .build();

        assertThat(mod.resolve()).isEqualTo(1);
    }

    @Test
    void resolve_Long() {
        Mod mod = new ModBuilder(ReflectiveTransformerRegistry.getInstance())
                .input(100L)
                .mod(11)
                .build();

        assertThat(mod.resolve()).isEqualTo(1);
    }

}
