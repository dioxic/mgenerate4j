package uk.dioxic.mgenerate.core.operator.general;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.operator.type.UuidType;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UuidTest {

    @Test
    void resolve() {
        Uuid uuid = new UuidBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(uuid.resolveInternal()).as("is uuid").isInstanceOf(UUID.class);
    }

    @Test
    void resolveAsString() {
        Uuid uuid = new UuidBuilder(ReflectiveTransformerRegistry.getInstance()).type(UuidType.STRING).build();

        assertThat(uuid.resolveInternal()).as("is string").isInstanceOf(String.class);
    }
}
