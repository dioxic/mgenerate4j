package uk.dioxic.mgenerate.core;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.common.OperatorFactory;

import static org.assertj.core.api.Assertions.*;

public class OperatorFactoryTest {

    @Test
    public void createOperator() {
        Resolvable resolvable = OperatorFactory.create("$objectid");

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).as("objectid result").isInstanceOf(ObjectId.class);
    }

}
