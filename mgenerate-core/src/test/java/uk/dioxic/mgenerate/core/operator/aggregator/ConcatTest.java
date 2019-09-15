package uk.dioxic.mgenerate.core.operator.aggregator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcatTest {

    @Test
    @SuppressWarnings("unchecked")
    void resolve_SingleValues() {
        List<Object> list = Arrays.asList("bob", "gertrude");
        Concat concat = new ConcatBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(list)
                .build();

        assertThat(concat.resolve()).containsExactlyElementsOf(list);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_ArrayValues() {
        List<Object> list = Arrays.asList("bob", "gertrude", "kevin", "perry");

        Concat concat = new ConcatBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(list.subList(0, 2), list.subList(2, 4)))
                .build();

        assertThat(concat.resolve()).containsExactlyElementsOf(list);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_MixedValues() {
        List<Object> list = Arrays.asList("bob", "gertrude", "kevin", "perry");

        Concat concat = new ConcatBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(list.subList(0, 1), list.get(1), list.subList(2, 4)))
                .build();

        assertThat(concat.resolve()).containsExactlyElementsOf(list);
    }

}
