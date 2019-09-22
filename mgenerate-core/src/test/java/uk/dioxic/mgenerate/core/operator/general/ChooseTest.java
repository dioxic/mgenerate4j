package uk.dioxic.mgenerate.core.operator.general;

import org.assertj.core.util.Lists;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.operator.numeric.NumberInt;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ChooseTest {

    @Test
    void resolve_StaticValues() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip");

        Choose choose = new ChooseBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(from)
                .build();

        // make sure the choice is dynamic
        Set<String> actual = new HashSet<>();
        Stream.generate(choose::resolveInternal)
                .limit(100)
                .map(Object::toString)
                .forEach(actual::add);

        assertThat(actual).as("an expected value").containsAll(from);
    }

    @Test
    void resolve_StaticWeightedValues() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip");
        List<Integer> weights = Lists.newArrayList(1, 0, 0);

        Choose choose = new ChooseBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(from)
                .weights(weights)
                .build();

        assertThat(choose.resolveInternal()).as("an expected value").isEqualTo(from.get(0));
    }

    @Test
    void resolve_DynamicValues() {

        Array array = new ArrayBuilder(ReflectiveTransformerRegistry.getInstance())
                .of(new Document("number", new NumberInt()))
                .number(5)
                .build();

        Choose choose = new ChooseBuilder(ReflectiveTransformerRegistry.getInstance())
                .from(array)
                .build();

        // make sure the choice is dynamic
        Set<Object> actual = new HashSet<>();
        Stream.generate(choose::resolveInternal)
                .limit(100)
                .map(Document.class::cast)
                .map(d -> d.get("number"))
                .forEach(actual::add);

        assertThat(actual).allSatisfy(o -> assertThat(0).isInstanceOf(Integer.class));
        assertThat(actual).as("recursive resolve is working").hasSize(5);
    }
}
