package uk.dioxic.mgenerate.core.operator.general;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.core.operator.mutator.Concat;
import uk.dioxic.mgenerate.core.operator.mutator.ConcatBuilder;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArrayTest {

    @Test
    void resolve_SingleValues() {
        List<Object> list = Arrays.asList("bob", "gertrude");
        Concat concat = new ConcatBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(list)
                .build();

        assertThat(concat.resolveInternal()).containsExactlyElementsOf(list);
    }

    @Test
    void resolve_StaticValues() {
        String of = "turnip";
        int number = 4;

        Array array = new ArrayBuilder(ReflectiveTransformerRegistry.getInstance())
                .of(of)
                .number(number)
                .build();

        assertThat(array).as("null check").isNotNull();
        assertThat(array.resolveInternal()).as("has correct size").hasSize(number);
        assertThat(array.resolveInternal()).as("is subset of expected values").containsOnly(of);
    }

    @Test
    void resolve_DynamicValues() {
        Resolvable<String> of = Wrapper.wrap("turnip");
        int number = 4;

        Array array = new ArrayBuilder(ReflectiveTransformerRegistry.getInstance())
                .of(of)
                .number(number)
                .build();

        assertThat(array).as("null check").isNotNull();
        assertThat(array.resolveInternal()).as("has correct size").hasSize(number);
        assertThat(array.resolveInternal()).as("is subset of expected values").containsOnly(of.resolve());
    }

}
