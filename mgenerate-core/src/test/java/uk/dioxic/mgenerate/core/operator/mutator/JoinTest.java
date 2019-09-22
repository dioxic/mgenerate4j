package uk.dioxic.mgenerate.core.operator.mutator;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JoinTest {

    @Test
    void resolve_WithSeparator() {
        String sep = "|";
        List<String> array = Lists.newArrayList("fish", "gofer", "beaver");

        Join join = new JoinBuilder(ReflectiveTransformerRegistry.getInstance()).array(array).sep(sep).build();

        assertThat(join.resolveInternal()).as("check concaternation").isEqualTo("fish|gofer|beaver");
    }

    @Test
    void resolve() {
        List<String> array = Lists.newArrayList("fish", "gofer", "beaver");

        Join join = new JoinBuilder(ReflectiveTransformerRegistry.getInstance()).array(array).build();

        assertThat(join.resolveInternal()).as("check concaternation").isEqualTo("fishgoferbeaver");
    }

}
