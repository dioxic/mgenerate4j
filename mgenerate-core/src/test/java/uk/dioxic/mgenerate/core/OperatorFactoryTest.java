package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.operator.OperatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class OperatorFactoryTest {

    @Test
    void create_Default() {
        Resolvable<?> resolvable = OperatorFactory.create("$objectid");

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).as("objectid result").isInstanceOf(ObjectId.class);
    }

    @Test
    void create_FromDocument() {
        Document doc = new Document("min", 1);

        Resolvable<?> resolvable = OperatorFactory.create("$number", doc);

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).as("integer result").isInstanceOf(Integer.class);
    }

    @Test
    void create_FromSingleValue() {
        Resolvable<?> resolvable = OperatorFactory.create("$optional", "something");

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).isEqualTo("something");
    }

    @Test
    void create_FromSingleValue2() {
        Resolvable<?> resolvable = OperatorFactory.create("$sentence", 0);

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).asString().isEqualTo(".");
    }

    @Test
    void create_Null_OperatorKeyNotFound() {
        Resolvable<?> resolvable = OperatorFactory.create("$xxx");

        assertThat(resolvable).as("is null").isNull();
    }

}
