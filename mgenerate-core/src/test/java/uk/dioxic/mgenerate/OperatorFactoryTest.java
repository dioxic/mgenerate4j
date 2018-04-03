package uk.dioxic.mgenerate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.dioxic.faker.resolvable.Resolvable;

public class OperatorFactoryTest {

    @Test
    public void create() {
        Resolvable resolvable = OperatorFactory.create("objectid");

        Assertions.assertThat(resolvable).as("objectid operator").isNotNull();
    }
}
