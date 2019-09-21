package uk.dioxic.mgenerate.core.operator.mutator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.operator.type.HashType;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class HashTest {

    @Test
    void resolve() {
        Hash hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").build();
        assertThat(hash.resolve()).as("INT32").isEqualTo(-855357176);

        hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").output(HashType.INT64).build();
        assertThat(hash.resolve()).as("INT64").isEqualTo(-3673731096897361255L);

        hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").output(HashType.HEX).build();
        assertThat(hash.resolve()).as("HEX").isEqualTo("cd04490819206a990ed5b165a35598a4");
    }
}
