package uk.dioxic.mgenerate.core.operator.general;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PickTest {

    @Test
    void resolve() {
        List<String> array = Lists.newArrayList("fish", "turtle", "badger");
        int element = 2;

        Pick pick = new PickBuilder(ReflectiveTransformerRegistry.getInstance()).array(array).element(element).build();

        assertThat(pick.resolve()).as("correct element").isEqualTo(array.get(element));
    }
}
