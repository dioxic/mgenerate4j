package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.codec.OperatorTransformer;

import static org.assertj.core.api.Assertions.assertThat;

public class OperatorTransformerTest {

    @Test
    public void createFaker() {
        OperatorTransformer transformer = new OperatorTransformer();

        Object object = transformer.transform("Dr. #{name.first_name} von #{name.last_name} esq.");

        assertThat(object).as("not null").isNotNull();
        assertThat(object).as("resolvable type").isInstanceOf(Resolvable.class);

        Resolvable resolvable = (Resolvable)object;

        assertThat(resolvable.resolve()).as("string return type").isInstanceOf(String.class);
        assertThat(resolvable.resolve().toString()).as("placeholder replaced").doesNotContain("#{");
    }

    @Test
    public void createSimpleOperator() {
        OperatorTransformer transformer = new OperatorTransformer();

        Object object = transformer.transform("$number");

        assertThat(object).as("not null").isNotNull();
        assertThat(object).as("resolvable type").isInstanceOf(Resolvable.class);
        assertThat(((Resolvable)object).resolve()).as("int return type").isInstanceOf(Integer.class);
    }

    @Test
    public void createDocumentOperator() {
        OperatorTransformer transformer = new OperatorTransformer();
        Document args = new Document();
        args.put("min", 1);
        args.put("max", 5);

        Object object = transformer.transform(new Document("$number", args));

        assertThat(object).as("not null").isNotNull();
        assertThat(object).as("resolvable type").isInstanceOf(Resolvable.class);
        Resolvable resolvable = (Resolvable)object;
        assertThat(resolvable.resolve()).as("int return type").isInstanceOf(Integer.class);
        assertThat((Integer)resolvable.resolve()).as("in range").isBetween(1, 5);
    }
}
