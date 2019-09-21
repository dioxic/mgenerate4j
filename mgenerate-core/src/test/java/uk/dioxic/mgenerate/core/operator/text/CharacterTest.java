package uk.dioxic.mgenerate.core.operator.text;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterTest {

    @Test
    void resolve() {
        java.lang.String pool = "ABCDE04[]";
        List<java.lang.Character> poolChars = pool.chars().mapToObj(i -> (char)i).collect(Collectors.toList());

        Character character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).pool(pool).build();
        assertThat(character.resolve()).as("check pool").isIn(poolChars);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).numeric(true).build();
        assertThat(character.resolve()).as("check numeric").matches(java.lang.Character::isDigit);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).casing("upper").build();
        assertThat(character.resolve()).as("check upper case").matches(java.lang.Character::isUpperCase);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).casing("lower").build();
        assertThat(character.resolve()).as("check lower case").matches(java.lang.Character::isLowerCase);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).alpha(true).build();
        assertThat(character.resolve()).as("check alpha").matches(java.lang.Character::isLetter);
    }
}
