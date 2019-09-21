package uk.dioxic.mgenerate.core.operator.general;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectIdTest {

    @Test
    void resolve() {
        assertThat(new ObjectIdBuilder(ReflectiveTransformerRegistry.getInstance()).build().resolve()).isInstanceOf(ObjectId.class);
    }
}
