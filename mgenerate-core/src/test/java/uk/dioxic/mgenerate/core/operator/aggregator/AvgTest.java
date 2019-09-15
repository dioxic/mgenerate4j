package uk.dioxic.mgenerate.core.operator.aggregator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AvgTest {

    @Test
    void resolve_Double() {
        Avg avg = new AvgBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1d, 2d, 3d))
                .build();

        assertThat(avg.resolve()).isEqualTo(2d);
    }

    @Test
    void resolve_Integer() {
        Avg avg = new AvgBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1, 2, 3))
                .build();

        assertThat(avg.resolve()).isEqualTo(2d);
    }

    @Test
    void resolve_Long() {
        Avg avg = new AvgBuilder(ReflectiveTransformerRegistry.getInstance())
                .values(Arrays.asList(1L, 2L, 3L))
                .build();

        assertThat(avg.resolve()).isEqualTo(2d);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolve_NonNumeric_throwsResolveException() {
        List<?> array = Arrays.asList("badger","badger","badger");
        Avg avg = new AvgBuilder(ReflectiveTransformerRegistry.getInstance())
                .values((List)array)
                .build();

        assertThatThrownBy(avg::resolve).isInstanceOf(ClassCastException.class);
    }
}
