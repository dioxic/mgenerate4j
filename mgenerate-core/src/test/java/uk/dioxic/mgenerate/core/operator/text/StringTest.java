package uk.dioxic.mgenerate.core.operator.text;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StringTest {

    @Test
    void resolve() {
        java.lang.String pool = "ABCDE04[]";
        List<java.lang.Character> poolChars = pool.chars().mapToObj(i -> (char)i).collect(Collectors.toList());
        int length = 7;

        String string = new StringBuilder(ReflectiveTransformerRegistry.getInstance()).pool(pool).length(length).build();

        assertThat(string.resolveInternal()).as("size").hasSize(length);

        for (char c : string.resolveInternal().toCharArray()) {
            assertThat(c).isIn(poolChars);
        }

    }
}
