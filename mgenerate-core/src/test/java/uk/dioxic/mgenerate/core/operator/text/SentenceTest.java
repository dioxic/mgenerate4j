package uk.dioxic.mgenerate.core.operator.text;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class SentenceTest {

    @Test
    void reolve() {
        Sentence sentence = new SentenceBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(sentence.resolve()).isNotNull();
        assertThat(sentence.resolve()).isInstanceOf(java.lang.String.class);
    }
}
