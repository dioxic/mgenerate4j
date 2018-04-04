package uk.dioxic.mgenerate;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import uk.dioxic.faker.resolvable.Resolvable;

import static org.assertj.core.api.Assertions.*;

public class OperatorFactoryTest {

    @Test
    public void createOperator() {
        Resolvable resolvable = OperatorFactory.create("$objectid");

        assertThat(resolvable).as("not null").isNotNull();
        assertThat(resolvable.resolve()).as("objectid result").isInstanceOf(ObjectId.class);
    }

}
