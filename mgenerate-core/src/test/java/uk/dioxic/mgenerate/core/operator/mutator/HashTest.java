package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class HashTest {

    @Test
    void resolve() {
        Hash hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").build();
        assertThat(hash.resolveInternal()).isEqualTo(-855357176);
    }
}
