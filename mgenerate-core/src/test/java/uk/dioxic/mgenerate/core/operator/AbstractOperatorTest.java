package uk.dioxic.mgenerate.core.operator;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.core.operator.numeric.NumberInt;
import uk.dioxic.mgenerate.core.operator.numeric.NumberIntBuilder;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractOperatorTest {

    @Test
    void resolve_Probability_Null() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull(1)
                .build();

        assertThat(numberInt.resolve()).isNull();
    }

    @Test
    void resolve_Probability_NotNull() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull(0)
                .build();

        assertThat(numberInt.resolve()).isNotNull();
    }

    @Test
    void resolve_Probability_MaybeNull() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull(0.5)
                .build();

        long nullCount = Stream.generate(numberInt::resolve)
                .limit(1000)
                .filter(Objects::isNull)
                .count();

        assertThat(nullCount).isGreaterThan(0).isLessThan(1000);
    }

    @Test
    void resolve_Null_Null() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull(Wrapper.wrap(null))
                .build();

        assertThat(numberInt.resolve()).isNull();
    }

    @Test
    void resolve_String_NotNull() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull("something")
                .build();

        assertThat(numberInt.resolve()).isNotNull();
    }

    @Test
    void resolve_Boolean_Null() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull(true)
                .build();

        assertThat(numberInt.resolve()).isNull();
    }

    @Test
    void resolve_Boolean_NotNull() {
        NumberInt numberInt = new NumberIntBuilder(ReflectiveTransformerRegistry.getInstance())
                .isNull(false)
                .build();

        assertThat(numberInt.resolve()).isNotNull();
    }
}
