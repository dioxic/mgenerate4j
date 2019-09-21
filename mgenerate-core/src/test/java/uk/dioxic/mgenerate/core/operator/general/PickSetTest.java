package uk.dioxic.mgenerate.core.operator.general;

import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.common.exception.ResolveException;
import uk.dioxic.mgenerate.core.operator.numeric.NumberInt;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickSetTest {

    @Test
    void resolve_StaticValues() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip", "salad");

        PickSet pick = new PickSetBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(from)
                .quantity(2)
                .build();

        // make sure the choice is dynamic
        Set<Object> actual = new HashSet<>();
        Stream.generate(pick::resolve)
                .limit(100)
                .forEach(l -> {
                    assertThat(l).hasSize(2);
                    assertThat(l).isSubsetOf(from);
                    assertThat(l).allSatisfy(o -> assertThat(o).isInstanceOf(String.class));
                    actual.addAll(l);
                });

        assertThat(actual).as("all values").containsAll(from);
    }

    @Test
    void resolve_StaticWeightedValues() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip");
        List<Integer> weights = Lists.newArrayList(1, 1, 0);

        PickSet pick = new PickSetBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(from)
                .weights(weights)
                .quantity(2)
                .build();

        assertThat(pick.resolve()).containsExactlyInAnyOrderElementsOf(from.subList(0, 2));
    }

    @Test
    void resolve_DynamicValues() {
        Array array = new ArrayBuilder(ReflectiveTransformerRegistry.getInstance())
                .of(new Document("number", new NumberInt()))
                .number(5)
                .build();

        PickSet pick = new PickSetBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(array)
                .quantity(2)
                .build();

        // make sure the choice is dynamic
        Set<Object> actual = new HashSet<>();
        Stream.generate(pick::resolve)
                .limit(100)
                .forEach(actual::addAll);

        assertThat(actual).allSatisfy(o -> assertThat(o).isInstanceOf(Document.class));
        assertThat(actual).as("recursive resolve").hasSize(5);
    }

    @Test
    void resolve_QuantityLargerThanFromSize_throwResolvableException() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip", "salad");

        PickSet pick = new PickSetBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(from)
                .quantity(10)
                .build();

        assertThatThrownBy(pick::resolve).isInstanceOf(ResolveException.class);
    }

    @Test
    void resolve_QuantityLargerThanWeightedSize_throwResolvableException() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip", "salad");

        PickSet pick = new PickSetBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(from)
                .weights(Arrays.asList(1,0,0,0))
                .quantity(2)
                .build();

        assertThatThrownBy(pick::resolve).isInstanceOf(ResolveException.class);
    }
}
