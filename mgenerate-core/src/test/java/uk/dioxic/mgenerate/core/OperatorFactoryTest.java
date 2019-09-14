package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.operator.OperatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class OperatorFactoryTest {

    @Test
    public void create_Resolvable_OperatorKeyExists() {
        Resolvable resolvable = OperatorFactory.create("$objectid");

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).as("objectid result").isInstanceOf(ObjectId.class);
    }

    @Test
    public void create_Resolvable_OperatorWithDocument() {
        Document doc = new Document("min", 1);

        Resolvable resolvable = OperatorFactory.create("$number", doc);

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).as("integer result").isInstanceOf(Integer.class);
    }

    @Test
    public void create_Null_OperatorKeyNotFound() {
        Resolvable resolvable = OperatorFactory.create("$xxx");

        assertThat(resolvable).as("is null").isNull();
    }

}
